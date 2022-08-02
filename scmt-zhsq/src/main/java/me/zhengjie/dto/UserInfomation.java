/**
 * yammii.com Inc.
 * Copyright (c) 2018-2020 All Rights Reserved.
 */
package me.zhengjie.dto;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

/**
 * 
 * @author yongyan.pu
 * @version $Id: UserInfomation.java, v 1.0 2020年8月13日 下午8:31:59 yongyan.pu Exp $
 */

@Data
public class UserInfomation implements Serializable {

    private static final long serialVersionUID = 1L;

    @JSONField(name = "id")
    private Long              id;
    @JSONField(name = "gridName")
    private String            gridName;
    @JSONField(name = "nickName")
    private String            nickName;
    @JSONField(name = "sex")
    private String            sex;
    @JSONField(name = "avatar")
    private String            avatar;
    //    @JSONField(name = "label")
    //    private Integer           label;
    //    @JSONField(name = "type")
    //    private String            type;
    @JSONField(name = "idNumber")
    private String            idNumber;
    //    @JSONField(name = "birthDate")
    //    private Date              birthDate;
    //    @JSONField(name = "politicalFace")
    //    private String            politicalFace;
    @JSONField(name = "realName")
    private String            realName;
    //    @JSONField(name = "streetName")
    //    private String            streetName;
    //    @JSONField(name = "post")
    //    private String            post;
    @JSONField(name = "phone")
    private String            phone;
    //    @JSONField(name = "roleName")
    //    private String            roleName;
    //    @JSONField(name = "communityName")
    //    private String            communityName;
    //    @JSONField(name = "gridId")
    //    private String            gridId;
    //    @JSONField(name = "populationId")
    //    private String            populationId;
    //    @JSONField(name = "account")
    //    private String            account;
    //    @JSONField(name = "hobby")
    //    private String            hobby;
}
