/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2020 All Rights Reserved.
 */
package me.zhengjie.controller.party.admin;

import java.util.List;

import com.baomidou.mybatisplus.extension.api.R;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import me.zhengjie.controller.party.AbstractController;
import me.zhengjie.dto.StatisticalAnalysisDto;
import me.zhengjie.service.party.IPartyDuesService;
import me.zhengjie.service.party.IPartyLearnCenterEnrollService;
import me.zhengjie.service.party.IPartyMassessEnrollService;
import me.zhengjie.service.party.IPartyMemberService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;

/**
 * 
 * @author zoutao
 * @version $Id: PartyReportFormController.java, v 0.1 2020年8月16日 下午5:13:12 zoutao Exp $
 */

@Api(value = "统计分析", tags = "统计分析")
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/party")
public class PartyStatisticalAnalysisController extends AbstractController {

    private final IPartyMemberService            partyMemberService;

    private final IPartyLearnCenterEnrollService partyLearnCenterEnrollService;

    private final IPartyMassessEnrollService     partyMassessEnrollService;

    private final IPartyDuesService              IpartyDuesService;

    @ApiOperation("党员类别统计分析")
    @GetMapping("/categoriesanalysis")
    public Result<Object> partyCategoriesAnalysis(@ApiParam(value = "党委Id") @RequestParam(value = "partyCommitteeId", defaultValue = "") Long partyCommitteeId,
                                                  @ApiParam(value = "党支部Id") @RequestParam(value = "partyBranchId", defaultValue = "") Long partyBranchId) {

        List<StatisticalAnalysisDto> list = partyMemberService
            .partyCategoriesAnalysis(partyCommitteeId, partyBranchId);
        return ResultUtil.data(list);
    }

    @ApiOperation("党员学习情况分析")
    @GetMapping("/learnanalysis")
    public Result<Object> learnAnalysis(@ApiParam(value = "党委Id") @RequestParam(value = "partyCommitteeId", defaultValue = "") Long partyCommitteeId) {
        List<StatisticalAnalysisDto> list = partyLearnCenterEnrollService
            .learnAnalysis(partyCommitteeId);
        return ResultUtil.data(list);
    }

    @ApiOperation("活动推送人群分析")
    @GetMapping("/pushcrowdanalysis")
    public Result<Object> pushcrowdAnalysis(@ApiParam(value = "党委Id") @RequestParam(value = "partyCommitteeId", defaultValue = "") Long partyCommitteeId,
                                                             @ApiParam(value = "党支部Id") @RequestParam(value = "partyBranchId", defaultValue = "") Long partyBranchId) {
        List<StatisticalAnalysisDto> list = partyMassessEnrollService
            .pushcrowdAnalysis(partyCommitteeId, partyBranchId);
        return ResultUtil.data(list);
    }

    @ApiOperation("网格党员")
    @GetMapping("/gridpartyanalysis")
    public Result<Object> gridPartyanalysis(@ApiParam(value = "党委Id") @RequestParam(value = "partyCommitteeId", defaultValue = "") Long partyCommitteeId) {
        List<StatisticalAnalysisDto> list = partyMemberService.gridPartyanalysis(partyCommitteeId);
        return ResultUtil.data(list);
    }

    @ApiOperation("党费缴纳")
    @GetMapping("/partypaymentanalysis")
    public Result<Object> partyPaymentAnalysis(@ApiParam(value = "党委Id") @RequestParam(value = "partyCommitteeId", defaultValue = "") Long partyCommitteeId,
                                                                @ApiParam(value = "党支部Id") @RequestParam(value = "partyBranchId", defaultValue = "") Long partyBranchId,
                                                                @ApiParam(value = "时间（年+月）") @RequestParam(value = "time", defaultValue = "") String time) {

        List<StatisticalAnalysisDto> list = IpartyDuesService.partyPaymentAnalysis(partyCommitteeId,
            partyBranchId, time);
        return ResultUtil.data(list);
    }

}
