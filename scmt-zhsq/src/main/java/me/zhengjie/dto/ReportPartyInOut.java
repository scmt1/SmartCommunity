/**
 * yammii.com Inc.
 * Copyright (c) 2018-2020 All Rights Reserved.
 */
package me.zhengjie.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * @author yongyan.pu
 * @version $Id: ReportPartyInOut.java, v 1.0 2020年9月22日 下午6:04:06 yongyan.pu Exp $
 */
@Data
public class ReportPartyInOut implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("时间")
    private String            date;
    @ApiModelProperty("转入数量")
    private Integer           partyInNumber;
    @ApiModelProperty("转出数量")
    private Integer           partyOutNumber;

}
