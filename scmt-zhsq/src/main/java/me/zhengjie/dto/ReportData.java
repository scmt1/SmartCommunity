/**
 * yammii.com Inc.
 * Copyright (c) 2018-2020 All Rights Reserved.
 */
package me.zhengjie.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * @author yongyan.pu
 * @version $Id: ReportData.java, v 1.0 2020年9月22日 下午8:49:45 yongyan.pu Exp $
 */
@Data
public class ReportData implements Serializable {

    /**  */
    private static final long                 serialVersionUID = 1L;

    @ApiModelProperty("党员概况")
    private ReportPartyMemberAageStatistics   partyMemberAgeAnalysis;

    @ApiModelProperty("学习情况统计")
    private ReportLearn                       learnAnalysis;

    @ApiModelProperty("党群活动情况统计")
    private List<ReportPartyMasses>           partyMassesAnalysis;

    @ApiModelProperty("三会一课分布")
    private List<ReportThreeLessons>          threeLessonsAnalysis;

    @ApiModelProperty("转入、转入党员统计")
    private List<ReportPartyInOut>            partyInOutAnalysis;

    @ApiModelProperty("党组织分布情况")
    private List<ReportPartyOrganization>     partyOrganizationAnalysis;

    @ApiModelProperty("党员分布")
    private List<ReportPartyMemberDistribute> partyMemberDistributeAnalysis;

}
