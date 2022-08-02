package me.zhengjie.mapper.party;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.zhengjie.dto.PartyMemberAgeInfoDto;
import me.zhengjie.dto.ReportPartyInOut;
import me.zhengjie.dto.ReportPartyMemberDistribute;
import me.zhengjie.dto.StatisticalAnalysisDto;
import me.zhengjie.entity.party.PartyMember;

/**
 * <p>
 * 党员基本信息 Mapper 接口
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-13
 */
@Repository
@Mapper
public interface PartyMemberMapper extends BaseMapper<PartyMember> {

    /**获取党员的年龄和性别
     * @param partyCommitteeId
     * @param partBranchId
     * @return
     */
    List<PartyMemberAgeInfoDto> getPartyMemberAgeInfo(@Param("partyCommitteeId") Long partyCommitteeId,
                                                      @Param("partBranchId") Long partBranchId);

    /**
     * 分类统计各个党员类型人数
     * @param partyCommitteeId
     * @param partyBranchId
     * @return
     */
    List<StatisticalAnalysisDto> partyCategoriesAnalysis(@Param("partyCommitteeId") Long partyCommitteeId,
                                                         @Param("partBranchId") Long partBranchId);

    /**
     * 
     * 网格党员名称
     * @param partyCommitteeId
     * @return
     */
    List<StatisticalAnalysisDto> gridPartyanalysis(@Param("partyCommitteeId") Long partyCommitteeId);

    List<PartyMemberAgeInfoDto> getPartyMemberStatisticsAge(@Param("partyCommitteeIds") List<Long> list);

    //    List<ReportPartyMemberDistribute> getReportPartyMemberDistribute(@Param("partyCommitteeIds") List<Long> list);
    List<ReportPartyMemberDistribute> getReportPartyMemberDistribute();

    List<ReportPartyInOut> getReportPartyInOut(@Param("partyCommitteeIds") List<Long> committeeIds);
}
