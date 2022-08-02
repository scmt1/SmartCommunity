/**
 * yammii.com Inc.
 * Copyright (c) 2018-2020 All Rights Reserved.
 */
package me.zhengjie.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 1、未申请转入状态：点击【我的档案】直接跳转到申请转入页面。
2、点击了申请转入，后台未通过也未驳回：点击【我的档案】后，直接toast提示“后台审核中……”，无任何跳转。
3、点击了申请转入，后台申请通过：直接就进入我的档案页面。
4、点击申请转入，被驳回：点击【我的档案】，弹出提示框，显示驳回原因和再次申请按钮，点击再次申请按钮进入申请转入页面。


有党员档案数据情况：
1、未申请转出状态：点击【我的档案】直接跳转到我的档案页面。
2、点击了申请转出，后台未通过也未驳回：点击【我的档案】直接跳转到我的档案页面，【申请转出】按钮不可点，并显示“后台审核中……”。
3、点击了申请转出，后台申请通过：点击【我的档案】直接跳转到我的档案页面，【申请转出】按钮不可点，并显示“转出申请已通过”。
4、点击了申请转出，被驳回：点击【我的档案】，弹出提示框，显示驳回原因和再次申请按钮，点击按钮进入【我的档案】页面。
 * @author yongyan.pu
 * @version $Id: Userinfo.java, v 1.0 2020年8月14日 下午10:47:44 yongyan.pu Exp $
 */
@Data
public class Userinfo implements Serializable {
    /**  */
    private static final long serialVersionUID = 1L;

    private String            phoneNumber;

    @ApiModelProperty("1、已转入 2、未申请转入 3、申请转入中 4、申请转入驳回 5、申请转出中  6、申请转出驳回 7、已转出 ")
    private Integer           status;

    @ApiModelProperty("转入或转出申请驳回原因")
    private String            reject;

    @ApiModelProperty(value = "头像")
    private String            headSculpture;

    @ApiModelProperty(value = "党支部名称")
    private String            partyBranchName;

    @ApiModelProperty(value = "身份证号码")
    private String            idCardNo;

    @ApiModelProperty("姓名")
    private String            name;

    @ApiModelProperty("昵称")
    private String            nickName;

    @ApiModelProperty("1、男 2、女")
    private Integer           gender;

}
