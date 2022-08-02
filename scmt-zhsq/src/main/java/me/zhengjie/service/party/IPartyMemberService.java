package me.zhengjie.service.party;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.dto.MyArchivesDto;
import me.zhengjie.dto.PartyBaseInformaction;
import me.zhengjie.dto.ReportPartyInOut;
import me.zhengjie.dto.ReportPartyMemberAageStatistics;
import me.zhengjie.dto.ReportPartyMemberDistribute;
import me.zhengjie.dto.StatisticalAnalysisDto;
import me.zhengjie.entity.party.PartyInOutApply;
import me.zhengjie.entity.party.PartyMember;
import me.zhengjie.req.AuditReq;
import me.zhengjie.req.PartyMemberReq;

/**
 * <p>
 * 党员基本信息 服务类
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-13
 */
public interface IPartyMemberService extends IService<PartyMember> {

    /**
     * 修改党员
     * 
     * @param id
     * @param userId
     * @param partyMemberReq
     */
    void edit(Long id, Long userId, PartyMemberReq partyMemberReq);

    /**
     * 新增党员
     * 
     * @param userId
     * @param partyMemberReq
     */
    void add(Long userId, PartyMemberReq partyMemberReq);

    /**
     * 党员列表
     * 
     * @param name
     * @param gender
     * @param educationId
     * @param idCardNo
     * @param phoneNumber
     * @param partBranchId
     * @param partyCommitteeId
     * @param partyCategoryId
     * @param current
     * @param size
     * @return
     */
    IPage<PartyMember> getList(String name, Integer gender, Integer educationId, String idCardNo, String phoneNumber,
                               Long partBranchId, Long partyCommitteeId, Integer partyCategoryId, Integer current,
                               Integer size);

    /**
     * 基本信息
     * 
     * @param partyCommitteeId
     * @param partBranchId
     * @return
     */
    PartyBaseInformaction getbaseInfomation(Long partyCommitteeId, Long partBranchId);

    /**
     * 根据身份证号码查询档案信息
     * 
     * @param idCardNo
     * @return
     */
    MyArchivesDto getArchivesByIdCardNo(String idCardNo);

    /**
     * 根据身份证号码查询党员
     *
     * @param idCardNo
     * @return
     */
    PartyMember getPartyMemberByIdCardNo(String idCardNo);

    /**
     * copy通过转入的对象
     * 
     * @param partyInOutApply
     * @param audit
     */
    void savePartyIn(PartyInOutApply partyInOutApply, AuditReq audit, Long userId);

    /**
     * 按党员类别分类统计
     * 
     * @param partyCommitteeId
     * @param partyBranchId
     * @return
     */
    List<StatisticalAnalysisDto> partyCategoriesAnalysis(Long partyCommitteeId, Long partyBranchId);

    /**
     * 
     * 网格党员
     * @param partyCommitteeId
     * @param partyBranchId
     * @return
     */
    List<StatisticalAnalysisDto> gridPartyanalysis(Long partyCommitteeId);

    void updateOutPass(String idCardNo);

    void deleteById(Long id);

    ReportPartyMemberAageStatistics getPartyMemberStatisticsAge(List<Long> committeeIds);

    List<ReportPartyMemberDistribute> reportPartyMemberDistribute();

    List<ReportPartyInOut> reportPartyInOut(List<Long> committeeIds);

}
