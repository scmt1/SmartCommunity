/**
 * yammii.com Inc.
 * Copyright (c) 2018-2020 All Rights Reserved.
 */
package me.zhengjie.service.party;

import java.util.Map;

import me.zhengjie.dto.UserInfomation;

/**
 * 
 * @author yongyan.pu
 * @version $Id: IUserInfoService.java, v 1.0 2020年8月14日 上午9:54:32 yongyan.pu Exp $
 */
public interface IUserInfoService {

    /**
     * 根据token获取当前用户信息
     * 
     * @param accessToken
     * @return
     */
    public UserInfomation getCurrentUserInfo(String accessToken);

    public UserInfomation getCurrentUserInfo(Map<String, String> headerMap);

}
