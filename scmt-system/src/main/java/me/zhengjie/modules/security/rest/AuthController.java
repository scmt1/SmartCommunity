package me.zhengjie.modules.security.rest;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

import com.wf.captcha.ArithmeticCaptcha;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.aop.log.Log;
import me.zhengjie.common.utils.CommonUtil;
import me.zhengjie.common.utils.CreateVerifyCode;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.Result;
import me.zhengjie.entity.VerifyCode;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.security.config.SecurityProperties;
import me.zhengjie.modules.security.security.TokenProvider;
import me.zhengjie.modules.security.security.vo.AuthUser;
import me.zhengjie.modules.security.security.vo.JwtUser;
import me.zhengjie.modules.security.service.OnlineUserService;
import me.zhengjie.modules.security.utils.BufferedImageUtil;
import me.zhengjie.service.IVerifyCodeGenService;
import me.zhengjie.serviceimpl.SimpleCharVerifyCodeGenImpl;
import me.zhengjie.utils.RedisUtils;
import me.zhengjie.utils.SecurityUtils;
import me.zhengjie.utils.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Zheng Jie
 * @date 2018-11-23
 * ???????????????token????????????????????????
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@Api(tags = "???????????????????????????")
public class AuthController {

    @Value("${loginCode.expiration}")
    private Long expiration;
    @Value("${rsa.private_key}")
    private String privateKey;
    @Value("${single.login:false}")
    private Boolean singleLogin;
    private final SecurityProperties properties;
    private final RedisUtils redisUtils;
    private final UserDetailsService userDetailsService;
    private final OnlineUserService onlineUserService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;


    public AuthController(SecurityProperties properties, RedisUtils redisUtils, UserDetailsService userDetailsService, OnlineUserService onlineUserService, TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.properties = properties;
        this.redisUtils = redisUtils;
        this.userDetailsService = userDetailsService;
        this.onlineUserService = onlineUserService;
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }


    @Log("????????????")
    @ApiOperation("????????????")
    @AnonymousAccess
    @PostMapping(value = "/login")
    public Result<Object> login(@Validated @RequestBody AuthUser authUser, HttpServletRequest request){
        // ????????????

        RSA rsa = new RSA(privateKey, null);
        String password = new String(rsa.decrypt(authUser.getPassword(), KeyType.PrivateKey));
        // ???????????????
//        String code = (String) redisUtils.get(authUser.getUuid());
//        // ???????????????
//        redisUtils.del(authUser.getUuid());
//        if (StringUtils.isBlank(code)) {
//            return new ResultUtil<>().setErrorMsg("??????????????????????????????");
//        }
//        if (StringUtils.isBlank(authUser.getCode()) || !authUser.getCode().equalsIgnoreCase(code)) {
//            return new ResultUtil<>().setErrorMsg("???????????????");
//        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authUser.getUsername(), password);

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        // ????????????
        String token = tokenProvider.createToken(authentication);
        final JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        // ??????????????????
        onlineUserService.save(jwtUser, token, request);
        // ?????? token ??? ????????????
        Map<String,Object> authInfo = new HashMap<String,Object>(2){{
            put("token", properties.getTokenStartWith() + token);
            put("user", jwtUser);
        }};
        if(singleLogin){
            //???????????????????????????token
            onlineUserService.checkLoginOnUser(authUser.getUsername(),token);
        }
        return new ResultUtil<>().setData(authInfo);
    }

    @ApiOperation("??????????????????")
    @GetMapping(value = "/info")
    public ResponseEntity<Object> getUserInfo(){
        JwtUser jwtUser = (JwtUser)userDetailsService.loadUserByUsername(SecurityUtils.getUsername());
        return ResponseEntity.ok(jwtUser);
    }

    @ApiOperation("??????????????????")
    @AnonymousAccess
    @GetMapping(value = "/loginMap")
    public  Result<Object>  loginMap( @RequestParam("username") String username,@RequestParam("password") String passwords ){
        // ????????????
        RSA rsa = new RSA(privateKey, null);
        String password = new String(rsa.decrypt(passwords, KeyType.PrivateKey));

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        // ????????????
        String token = tokenProvider.createToken(authentication);
        final JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        // ?????? token ??? ????????????
        Map<String,Object> authInfo = new HashMap<String,Object>(2){{
            put("token", properties.getTokenStartWith() + token);
            put("user", jwtUser);
        }};
        return new ResultUtil<>().setData(authInfo);
    }

    @SuppressWarnings("unused")
	@AnonymousAccess
    @ApiOperation("???????????????")
    @GetMapping(value = "/code")
    public ResponseEntity<Object> getCode() throws IOException{
    	IVerifyCodeGenService iVerifyCodeGen =  new SimpleCharVerifyCodeGenImpl() ;
        String result = "";
        Map<String,Object> imgResult = new HashMap<String,Object>();
        String uuid = properties.getCodeKey() + IdUtil.simpleUUID();
        if(iVerifyCodeGen!=null){
        	 result = new CreateVerifyCode().randomStr(4);
        	 CreateVerifyCode vCode = new CreateVerifyCode(107,36,4,10, result);
//        	 VerifyCode verifyCode = iVerifyCodeGen.generate(111, 36);
//        	 result = verifyCode.getCode();
        	// ???????????????
        	 imgResult.put("img",BufferedImageUtil.BufferedImageToBase64(vCode.getBuffImg()) );
        	 imgResult.put("uuid", uuid);
        }
        else{
        	// ???????????? https://gitee.com/whvse/EasyCaptcha
            ArithmeticCaptcha captcha = new ArithmeticCaptcha(111, 36);
            // ?????????????????????????????????
            captcha.setLen(2);
            // ?????????????????????
            result = captcha.text();
            // ???????????????
            imgResult.put("img", captcha.toBase64());
            imgResult. put("uuid", uuid);
        }

        // ??????
        redisUtils.set(uuid, result, expiration, TimeUnit.MINUTES);

        return ResponseEntity.ok(imgResult);
    }

    @ApiOperation("????????????")
    @AnonymousAccess
    @DeleteMapping(value = "/logout")
    public ResponseEntity<Object> logout(HttpServletRequest request){
        onlineUserService.logout(tokenProvider.getToken(request));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
