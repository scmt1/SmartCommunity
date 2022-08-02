/**
 * yammii.com Inc.
 * Copyright (c) 2018-2020 All Rights Reserved.
 */
package me.zhengjie.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * 
 * @author yongyan.pu
 * @version $Id: PartyMemberAgeInfoDto.java, v 1.0 2020年8月13日 下午2:55:13 yongyan.pu Exp $
 */

@Data
public class PartyMemberAgeInfoDto implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    private Integer           age;

    private Integer           gender;

}
