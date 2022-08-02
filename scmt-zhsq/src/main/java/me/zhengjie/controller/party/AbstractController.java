
package me.zhengjie.controller.party;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import me.zhengjie.common.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;

import me.zhengjie.common.utils.ResultCode;
import me.zhengjie.dto.UserInfomation;
import me.zhengjie.common.BusinessException;
import me.zhengjie.service.party.IUserInfoService;

import lombok.extern.slf4j.Slf4j;

/**
 *  公共组件
 * @author yongyan.pu
 * @version $Id: AbstractController.java, v 1.0 2020年3月30日 下午3:40:08 yongyan.pu Exp $
 */
@Slf4j
public abstract class AbstractController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private IUserInfoService   userInfoService;

    @Autowired
    private  SecurityUtil securityUtil;

    protected UserInfomation getUserInfomation() {
        //        UserInfomation userInfomaion = new UserInfomation();
        //        userInfomaion.setId(1L);
        //        userInfomaion.setPhone("15982102635");
        //        userInfomaion.setIdNumber("510726198810281234");
        //        userInfomaion.setRealName("pyy");
        //        userInfomaion.setSex("1");
        //        return userInfomaion;

        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String> headers = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String name = (String) headerNames.nextElement();
            String value = request.getHeader(name);
            headers.put(name, value);
        }
        //        String accessToken = request.getHeader("Authorization");
        if (headers.isEmpty()) {
            throw new BusinessException(ResultCode.UN_AUTHORIZED);
        }
        UserInfomation userInfomation = userInfoService.getCurrentUserInfo(headers);
        return userInfomation;
    }

    protected Long getUserId() {
        return securityUtil.getCurrUser().getId();
    }

    protected String getUserIdNumber() {
        return getUserInfomation().getIdNumber();
    }
}
