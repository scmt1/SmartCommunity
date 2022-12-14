package me.zhengjie.modules.system.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import me.zhengjie.aop.log.Log;
import me.zhengjie.common.constant.CommonConstant;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.Result;
import me.zhengjie.config.DataScope;
import me.zhengjie.domain.VerificationCode;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.service.IGridDeptService;
import me.zhengjie.service.LogService;
import me.zhengjie.service.VerificationCodeService;
import me.zhengjie.system.domain.User;
import me.zhengjie.system.domain.vo.UserPassVo;
import me.zhengjie.system.service.DeptService;
import me.zhengjie.system.service.RoleService;
import me.zhengjie.system.service.UserService;
import me.zhengjie.system.service.dto.RoleSmallDto;
import me.zhengjie.system.service.dto.UserDto;
import me.zhengjie.system.service.dto.UserQueryCriteria;
import me.zhengjie.utils.ElAdminConstant;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.SecurityUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

/**
 * @author Zheng Jie
 * @date 2018-11-23
 */
@Api(tags = "?????????????????????")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private LogService logService;

    @Autowired
    private IGridDeptService gridDeptService;

    @Value("${rsa.private_key}")
    private String privateKey;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final DataScope dataScope;
    private final DeptService deptService;
    private final RoleService roleService;
    private final VerificationCodeService verificationCodeService;

    public UserController(PasswordEncoder passwordEncoder, UserService userService, DataScope dataScope, DeptService deptService, RoleService roleService, VerificationCodeService verificationCodeService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.dataScope = dataScope;
        this.deptService = deptService;
        this.roleService = roleService;
        this.verificationCodeService = verificationCodeService;
    }

    @Log("??????????????????")
    @ApiOperation("??????????????????")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('user:list')")
    public void download(HttpServletResponse response, UserQueryCriteria criteria) throws IOException {
        userService.download(userService.queryAll(criteria), response);
    }

    @Log("????????????")
    @ApiOperation("????????????")
    @GetMapping
    @PreAuthorize("@el.check('user:list')")
    public Result<Object> getUsers(UserQueryCriteria criteria, Pageable pageable){
        Set<Long> deptSet = new HashSet<>();
        Set<Long> result = new HashSet<>();
        if (!ObjectUtils.isEmpty(criteria.getDeptId())) {
            deptSet.add(criteria.getDeptId());
            deptSet.addAll(dataScope.getDeptChildren(deptService.findByPid(criteria.getDeptId())));
        }
        // ????????????
        Set<Long> deptIds = dataScope.getDeptIds();
        // ????????????????????????????????????????????????????????????
        if (!CollectionUtils.isEmpty(deptIds) && !CollectionUtils.isEmpty(deptSet)){
            // ?????????
            result.addAll(deptSet);
            result.retainAll(deptIds);
            // ???????????????????????????????????????
            criteria.setDeptIds(result);
            if(result.size() == 0){
                return new ResultUtil<>().setData(PageUtil.toPage(null,0));
            } else {
                return new ResultUtil<>().setData(userService.queryAll(criteria,pageable));
            }
        // ???????????????
        } else {
            result.addAll(deptSet);
            result.addAll(deptIds);
            criteria.setDeptIds(result);
            return new ResultUtil<>().setData(userService.queryAll(criteria,pageable));
        }
    }

    @Log("????????????")
    @ApiOperation("????????????")
    @GetMapping(value = "/getAllUsers")
//    @PreAuthorize("@el.check('user:list')")
    public Result<Object> getAllUsers(UserQueryCriteria criteria, Pageable pageable){
        Set<Long> deptSet = new HashSet<>();
        Set<Long> result = new HashSet<>();
        Pageable pageable1 =  PageRequest.of(pageable.getPageNumber()-1,pageable.getPageSize(),pageable.getSort());
        if (!ObjectUtils.isEmpty(criteria.getDeptId())) {
            deptSet.add(criteria.getDeptId());
            deptSet.addAll(dataScope.getDeptChildren(deptService.findByPid(criteria.getDeptId())));
        }
        // ????????????
        Set<Long> deptIds = dataScope.getDeptIds();
        // ????????????????????????????????????????????????????????????
        if (!CollectionUtils.isEmpty(deptIds) && !CollectionUtils.isEmpty(deptSet)){
            // ?????????
            result.addAll(deptSet);
            result.retainAll(deptIds);
            // ???????????????????????????????????????
            criteria.setDeptIds(result);
            if(result.size() == 0){
                return new ResultUtil<Object>().setData(userService.queryAll(criteria,pageable1));
                		//ResponseEntity<>(PageUtil.toPage(null,0),HttpStatus.OK);
            } else {
                return new ResultUtil<Object>().setData(userService.queryAll(criteria,pageable1));
                		//new ResponseEntity<>(userService.queryAll(criteria,pageable),HttpStatus.OK);
            }
        // ???????????????
        } else {
            result.addAll(deptSet);
            result.addAll(deptIds);
            criteria.setDeptIds(result);
            return new ResultUtil<Object>().setData(userService.queryAll(criteria,pageable1));
            		//new ResponseEntity<>(userService.queryAll(criteria,pageable),HttpStatus.OK);
        }
    }

    /**
     * ????????????????????????
     * @param username
     * @return
     * @throws UnsupportedEncodingException
     */
    @Log("??????????????????")
    @RequestMapping(value = "/searchByName/{username}", method = RequestMethod.GET)
    @ApiOperation(value = "???????????????????????????")
    @PreAuthorize("@el.check('user:list')")
    public Result<List<UserDto>> searchByName(@PathVariable String username) throws UnsupportedEncodingException {
        List<UserDto> list = userService.findByUsernameLikeAndStatus("%"+ URLDecoder.decode(username, "utf-8")+"%", CommonConstant.STATUS_NORMAL);
        list.forEach(u -> {
            u.setPassword(null);
        });
        return new ResultUtil<List<UserDto>>().setData(list);
    }

    @Log("????????????")
    @ApiOperation("????????????")
    @PostMapping
    @PreAuthorize("@el.check('user:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody User resources){
        checkLevel(resources);
        // ???????????? 123456
        resources.setPassword(passwordEncoder.encode("123456"));
        resources.setCreateTime(new Timestamp(System.currentTimeMillis()));
        Long region = deptService.findById(resources.getDept().getId()).getPid();
        resources.setRegion(region);
        return new ResponseEntity<>(userService.create(resources),HttpStatus.CREATED);
    }

    @Log("????????????")
    @ApiOperation("????????????")
    @PutMapping
    @PreAuthorize("@el.check('user:edit')")
    public ResponseEntity<Object> update(@Validated(User.Update.class) @RequestBody User resources){
        checkLevel(resources);
        userService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("???????????????????????????")
    @ApiOperation("???????????????????????????")
    @PutMapping(value = "center")
    public ResponseEntity<Object> center(@Validated(User.Update.class) @RequestBody User resources){
        UserDto userDto = userService.findByName(SecurityUtils.getUsername());
        if(!resources.getId().equals(userDto.getId())){
            throw new BadRequestException("????????????????????????");
        }
        userService.updateCenter(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("????????????")
    @ApiOperation("????????????")
    @DeleteMapping
    @PreAuthorize("@el.check('user:del')")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids){
        UserDto user = userService.findByName(SecurityUtils.getUsername());
        for (Long id : ids) {
            Integer currentLevel =  Collections.min(roleService.findByUsersId(user.getId()).stream().map(RoleSmallDto::getLevel).collect(Collectors.toList()));
            Integer optLevel =  Collections.min(roleService.findByUsersId(id).stream().map(RoleSmallDto::getLevel).collect(Collectors.toList()));
            if (currentLevel > optLevel) {
                throw new BadRequestException("????????????????????????????????????" + userService.findByName(SecurityUtils.getUsername()).getUsername());
            }
        }
        userService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("????????????")
    @PostMapping(value = "/updatePass")
    public ResponseEntity<Object> updatePass(@RequestBody UserPassVo passVo){
        // ????????????
        RSA rsa = new RSA(privateKey, null);
        String oldPass = new String(rsa.decrypt(passVo.getOldPass(), KeyType.PrivateKey));
        String newPass = new String(rsa.decrypt(passVo.getNewPass(), KeyType.PrivateKey));
        UserDto user = userService.findByName(SecurityUtils.getUsername());
        if(!passwordEncoder.matches(oldPass, user.getPassword())){
            throw new BadRequestException("??????????????????????????????");
        }
        if(passwordEncoder.matches(newPass, user.getPassword())){
            throw new BadRequestException("?????????????????????????????????");
        }
        userService.updatePass(user.getUsername(),passwordEncoder.encode(newPass));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("????????????")
    @PostMapping(value = "/updateAvatar")
    public ResponseEntity<Object> updateAvatar(@RequestParam MultipartFile file){
        userService.updateAvatar(file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("????????????")
    @ApiOperation("????????????")
    @PostMapping(value = "/updateEmail/{code}")
    public ResponseEntity<Object> updateEmail(@PathVariable String code, User user){
        // ????????????
        RSA rsa = new RSA(privateKey, null);
        String password = new String(rsa.decrypt(user.getPassword(), KeyType.PrivateKey));
        UserDto userDto = userService.findByName(SecurityUtils.getUsername());
        if(!passwordEncoder.matches(password, userDto.getPassword())){
            throw new BadRequestException("????????????");
        }
        VerificationCode verificationCode = new VerificationCode(code, ElAdminConstant.RESET_MAIL,"email",user.getEmail());
        verificationCodeService.validated(verificationCode);
        userService.updateEmail(userDto.getUsername(),user.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????????????????????????????
     * @param resources /
     */
    private void checkLevel(User resources) {
        UserDto user = userService.findByName(SecurityUtils.getUsername());
        Integer currentLevel =  Collections.min(roleService.findByUsersId(user.getId()).stream().map(RoleSmallDto::getLevel).collect(Collectors.toList()));
        Integer optLevel = roleService.findByRoles(resources.getRoles());
        if (currentLevel > optLevel) {
            throw new BadRequestException("??????????????????");
        }
    }

    /**
     * ??????????????????????????????????????????
     * @param ids ????????????
     * @return ??????????????????
     */
    @Log("?????????????????????company??????")
    @ApiOperation("?????????????????????company??????")
    @PostMapping("deleteUser")
    public Result<Object> deleteUser(@RequestParam(name = "ids[]")Set<Long> ids){
        if (ids == null ) {
            return ResultUtil.error("???????????????????????????????????????");
        }
        Long time = System.currentTimeMillis();
//        Collection<TCompany> resultList= new ArrayList(Arrays.asList(ids));
        try {
            UserDto user = userService.findByName(SecurityUtils.getUsername());
            for (Long id : ids) {
                Integer currentLevel =  Collections.min(roleService.findByUsersId(user.getId()).stream().map(RoleSmallDto::getLevel).collect(Collectors.toList()));
                Integer optLevel =  Collections.min(roleService.findByUsersId(id).stream().map(RoleSmallDto::getLevel).collect(Collectors.toList()));
                if (currentLevel > optLevel) {
                    throw new BadRequestException("????????????????????????????????????" + userService.findByName(SecurityUtils.getUsername()).getUsername());
                }
            }
            userService.delete(ids);
            return ResultUtil.data(null, "????????????");
        } catch (Exception e) {
            logService.addErrorLog("????????????", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("????????????:" + e.getMessage());
        }
    }

    /**
     * ??????????????????????????????????????????
     * @param ids ????????????
     * @return ??????????????????
     */
    @Log("??????????????????")
    @ApiOperation("?????????????????????company??????")
    @PostMapping("updataUserState")
    public Result<Object> updataUserState(@RequestParam(name = "ids[]")Long[] ids, @RequestParam(name = "enabled")boolean enabled){
        if (ids == null ) {
            return ResultUtil.error("???????????????????????????????????????");
        }
        Long time = System.currentTimeMillis();
        try {

            Integer res = userService.updateStateById(ids,enabled);
            return ResultUtil.data(res, "??????????????????");
        } catch (Exception e) {
            logService.addErrorLog("??????????????????", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("??????????????????:" + e.getMessage());
        }
    }

    /**
     * ??????????????????????????????????????????
     * @return ???????????????
     */
    @Log("???????????????????????????")
    @ApiOperation("???????????????????????????")
    @GetMapping(value = "/getDeptTree")
    public Result<Object> getDeptTree(){
        Long time = System.currentTimeMillis();
        try {
            return ResultUtil.data(gridDeptService.selectDeptTree(), "????????????");
        } catch (Exception e) {
            e.printStackTrace();
            logService.addErrorLog("????????????", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("????????????:" + e.getMessage());
        }
    }
}
