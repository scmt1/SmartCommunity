/**
 * yammii.com Inc.
 * Copyright (c) 2018-2020 All Rights Reserved.
 */
package me.zhengjie.controller.party.admin;

import com.baomidou.mybatisplus.extension.api.R;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import me.zhengjie.dto.ReportData;
import me.zhengjie.service.party.impl.ReportService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;

/**
 * 
 * @author yongyan.pu
 * @version $Id: ReportController.java, v 1.0 2020年9月22日 下午5:14:58 yongyan.pu Exp $
 */
@Api(value = "大屏报表数据", tags = "大屏报表数据")
@RestController
@AllArgsConstructor
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;

    @ApiOperation("党员概况")
    @GetMapping("/analysis")
    public Result<Object> list(@ApiParam(value = "社区Id") @RequestParam(value = "communityId", defaultValue = "") Long communityId) {
        ReportData data = reportService.getReportData(communityId);
        return ResultUtil.data(data);
    }

    //    @ApiOperation("党员概况")
    //    @GetMapping("/partyMember/age/analysis")
    //    public R<ReportPartyMemberAageStatistics> list() {
    //        ReportPartyMemberAageStatistics partyMemberAage = partyMemberService.getPartyMemberStatisticsAge();
    //        return ResultUtil.data(partyMemberAage);
    //    }
    //
    //    @ApiOperation("学习情况统计")
    //    @GetMapping("/partyMember/learn/analysis")
    //    public R<ReportLearn> learnanalysis() {
    //        ReportLearn reportLearn = partyLearningCenterService.getLearnanalysis();
    //        return ResultUtil.data(reportLearn);
    //    }
    //
    //    @ApiOperation("党群活动情况统计")
    //    @GetMapping("/partyMasses/analysis")
    //    public R<List<ReportPartyMasses>> partyMemberanalysis() {
    //        List<ReportPartyMasses> resultList = partyMassesService.getPartyMassesStatistics();
    //        return ResultUtil.data(resultList);
    //    }
    //
    //    @ApiOperation("三会一课分布")
    //    @GetMapping("/threeLessons/analysis")
    //    public R<List<ReportThreeLessons>> reportThreeLessons() {
    //        List<ReportThreeLessons> resultList = partyThreeLessonsService.reportThreeLessons();
    //        return ResultUtil.data(resultList);
    //    }
    //
    //    @ApiOperation("转入、转入党员统计")
    //    @GetMapping("/partyInOut/analysis")
    //    public R<List<ReportPartyInOut>> reportPartyInOut() {
    //        List<ReportPartyInOut> resultList = partyInOutApplyService.reportPartyInOut();
    //        return ResultUtil.data(resultList);
    //    }
    //
    //    @ApiOperation("党员分布")
    //    @GetMapping("/partyMember/distribute/analysis")
    //    public R<List<ReportPartyMemberDistribute>> reportPartyMemberDistribute() {
    //        List<ReportPartyMemberDistribute> resultList = partyMemberService.reportPartyMemberDistribute();
    //        return ResultUtil.data(resultList);
    //    }
    //
    //    @ApiOperation("党组织分布情况")
    //    @GetMapping("/community/distribute/analysis")
    //    public R<List<ReportPartyOrganization>> reportPartyOrganization() {
    //        List<ReportPartyOrganization> resultList = partyCommitteeService.reportPartyOrganization();
    //        return ResultUtil.data(resultList);
    //    }

}
