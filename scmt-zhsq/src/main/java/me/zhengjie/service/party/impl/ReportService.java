/**
 * yammii.com Inc.
 * Copyright (c) 2018-2020 All Rights Reserved.
 */
package me.zhengjie.service.party.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import me.zhengjie.dto.ReportData;
import me.zhengjie.dto.ReportLearn;
import me.zhengjie.dto.ReportPartyInOut;
import me.zhengjie.dto.ReportPartyMasses;
import me.zhengjie.dto.ReportPartyMemberAageStatistics;
import me.zhengjie.dto.ReportPartyMemberDistribute;
import me.zhengjie.dto.ReportPartyOrganization;
import me.zhengjie.dto.ReportThreeLessons;
import me.zhengjie.service.party.IPartyCommitteeService;
import me.zhengjie.service.party.IPartyLearningCenterService;
import me.zhengjie.service.party.IPartyMassesService;
import me.zhengjie.service.party.IPartyMemberService;
import me.zhengjie.service.party.IPartyThreeLessonsService;

import lombok.AllArgsConstructor;

/**
 * 
 * @author yongyan.pu
 * @version $Id: ReportService.java, v 1.0 2020年9月22日 下午9:49:04 yongyan.pu Exp $
 */
@AllArgsConstructor
@Service
public class ReportService {

    private final IPartyMemberService         partyMemberService;

    private final IPartyCommitteeService      partyCommitteeService;

    private final IPartyThreeLessonsService   partyThreeLessonsService;

    private final IPartyMassesService         partyMassesService;

    private final IPartyLearningCenterService partyLearningCenterService;

    public ReportData getReportData(Long communityId) {
        ReportData data = new ReportData();
        List<Long> committeeIds = null;
        if (communityId != null) {
            committeeIds = partyCommitteeService.getCommitteeIdsByCommunityId(communityId);
        }

        List<ReportPartyMasses> partyMassesAnalysis = partyMassesService.getPartyMassesStatistics();
        List<ReportPartyOrganization> partyOrganizationAnalysis = partyCommitteeService.reportPartyOrganization();
        data.setPartyMassesAnalysis(partyMassesAnalysis);
        data.setPartyOrganizationAnalysis(partyOrganizationAnalysis);

        ReportPartyMemberAageStatistics partyMemberAgeAnalysis = partyMemberService
            .getPartyMemberStatisticsAge(committeeIds);
        List<ReportPartyMemberDistribute> partyMemberDistributeAnalysis = partyMemberService
            .reportPartyMemberDistribute();
        ReportLearn learnAnalysis = partyLearningCenterService.getLearnanalysis(committeeIds);

        List<ReportThreeLessons> threeLessonsAnalysis = partyThreeLessonsService.reportThreeLessons(committeeIds);
        List<ReportPartyInOut> partyInOutAnalysis = partyMemberService.reportPartyInOut(committeeIds);

        data.setPartyMemberAgeAnalysis(partyMemberAgeAnalysis);
        data.setPartyMemberDistributeAnalysis(partyMemberDistributeAnalysis);
        data.setLearnAnalysis(learnAnalysis);

        data.setThreeLessonsAnalysis(threeLessonsAnalysis);
        data.setPartyInOutAnalysis(partyInOutAnalysis);

        return data;
    }

}
