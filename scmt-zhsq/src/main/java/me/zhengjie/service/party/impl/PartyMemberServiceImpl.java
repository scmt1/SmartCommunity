package me.zhengjie.service.party.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.common.utils.ResultCode;
import me.zhengjie.dto.MyArchivesDto;
import me.zhengjie.dto.PartyBaseInformaction;
import me.zhengjie.dto.PartyMemberAgeInfoDto;
import me.zhengjie.dto.ReportPartyInOut;
import me.zhengjie.dto.ReportPartyMemberAageStatistics;
import me.zhengjie.dto.ReportPartyMemberDistribute;
import me.zhengjie.dto.StatisticalAnalysisDto;
import me.zhengjie.entity.party.PartyBranch;
import me.zhengjie.entity.party.PartyInOutApply;
import me.zhengjie.entity.party.PartyMember;
import me.zhengjie.enums.EducationTypeEnum;
import me.zhengjie.enums.PartyCategoryEnum;
import me.zhengjie.common.BusinessException;
import me.zhengjie.mapper.party.PartyInOutApplyMapper;
import me.zhengjie.mapper.party.PartyMemberMapper;
import me.zhengjie.req.AuditReq;
import me.zhengjie.req.PartyMemberReq;
import me.zhengjie.service.party.IPartyMemberService;

import lombok.AllArgsConstructor;

/**
 * <p>
 * 党员基本信息 服务实现类
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-13
 */
@AllArgsConstructor
@Service
public class PartyMemberServiceImpl extends ServiceImpl<PartyMemberMapper, PartyMember> implements IPartyMemberService {

    private final PartyMemberMapper      partyMemberMapper;

    private final PartyBranchServiceImpl partyBranchServiceImpl;

    private final PartyInOutApplyMapper  partyInOutApplyMapper;

    @Override
    public void edit(Long id, Long userId, PartyMemberReq partyMemberReq) {
        PartyMember partyMember = getById(id);
        if (partyMember == null) {
            throw new BusinessException(ResultCode.FAILURE);
        }
        partyMember.setHeadSculpture(partyMemberReq.getHeadSculpture());
        partyMember.setBirthday(partyMemberReq.getBirthday());
        partyMember.setUpdateTime(new Date());
        partyMember.setUpdateUser(userId);
        partyMember.setEducationId(partyMemberReq.getEducationId());
        partyMember.setEducationName(EducationTypeEnum.getDescByCode(partyMemberReq.getEducationId()));
        partyMember.setGender(partyMemberReq.getGender());
        partyMember.setIdCardNo(partyMemberReq.getIdCardNo());
        partyMember.setName(partyMemberReq.getName());
        partyMember.setNation(partyMemberReq.getNation());
        partyMember.setNativePlace(partyMemberReq.getNativePlace());
        partyMember.setPartyBranchId(partyMemberReq.getPartyBranchId());
        partyMember.setPartyBranchName(partyMemberReq.getPartyBranchName());
        partyMember.setPartyCategoryId(partyMemberReq.getPartyCategoryId());
        partyMember.setPartyCategoryName(PartyCategoryEnum.getNameByCode(partyMemberReq.getPartyCategoryId()));
        partyMember.setPartyCommitteeId(partyMemberReq.getPartyCommitteeId());
        partyMember.setPartyCommitteeName(partyMemberReq.getPartyCommitteeName());
        partyMember.setPartyDate(partyMemberReq.getPartyDate());
        partyMember.setPhoneNumber(partyMemberReq.getPhoneNumber());
        partyMember.setResidentialAddress(partyMemberReq.getResidentialAddress());
        partyMember.setUnitAddress(partyMemberReq.getUnitAddress());
        super.updateById(partyMember);
    }

    @Override
    public void add(Long userId, PartyMemberReq partyMemberReq) {
        PartyMember partyMember = getPartyMemberByIdCardNo(partyMemberReq.getIdCardNo());
        if (partyMember != null) {
            throw new BusinessException(ResultCode.FAILURE.getCode(), "身份证号码已经存在");
        }
        partyMember = new PartyMember();
        partyMember.setHeadSculpture(partyMemberReq.getHeadSculpture());
        partyMember.setBirthday(partyMemberReq.getBirthday());
        partyMember.setCreateTime(new Date());
        partyMember.setCreateUser(userId);
        partyMember.setEducationId(partyMemberReq.getEducationId());
        partyMember.setEducationName(EducationTypeEnum.getDescByCode(partyMemberReq.getEducationId()));
        partyMember.setGender(partyMemberReq.getGender());
        partyMember.setIdCardNo(partyMemberReq.getIdCardNo());
        partyMember.setName(partyMemberReq.getName());
        partyMember.setNation(partyMemberReq.getNation());
        partyMember.setNativePlace(partyMemberReq.getNativePlace());
        partyMember.setPartyBranchId(partyMemberReq.getPartyBranchId());
        partyMember.setPartyBranchName(partyMemberReq.getPartyBranchName());
        partyMember.setPartyCategoryId(partyMemberReq.getPartyCategoryId());
        partyMember.setPartyCategoryName(PartyCategoryEnum.getNameByCode(partyMemberReq.getPartyCategoryId()));
        partyMember.setPartyCommitteeId(partyMemberReq.getPartyCommitteeId());
        partyMember.setPartyCommitteeName(partyMemberReq.getPartyCommitteeName());
        partyMember.setPartyDate(partyMemberReq.getPartyDate());
        partyMember.setPhoneNumber(partyMemberReq.getPhoneNumber());
        partyMember.setResidentialAddress(partyMemberReq.getResidentialAddress());
        partyMember.setUnitAddress(partyMemberReq.getUnitAddress());
        super.save(partyMember);
    }

    @Override
    public IPage<PartyMember> getList(String name, Integer gender, Integer educationId, String idCardNo,
                                      String phoneNumber, Long partBranchId, Long partyCommitteeId,
                                      Integer partyCategoryId, Integer current, Integer size) {
        IPage<PartyMember> page = new Page<>(current, size);
        QueryWrapper<PartyMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.eq(gender != null && gender != 0, "gender", gender);
        queryWrapper.eq(educationId != null && educationId != 0, "education_id", educationId);
        queryWrapper.like(StringUtils.isNotBlank(idCardNo), "id_card_no", idCardNo);
        queryWrapper.like(StringUtils.isNotBlank(phoneNumber), "phone_number", phoneNumber);
        queryWrapper.eq(partBranchId != null && partBranchId != 0, "party_branch_id", partBranchId);
        queryWrapper.eq(partyCommitteeId != null && partyCommitteeId != 0, "party_committee_id", partyCommitteeId);
        queryWrapper.eq(partyCategoryId != null && partyCategoryId != 0, "party_category_id", partyCategoryId);
        //        queryWrapper.eq("is_deleted", 0);
        //        create_time
        queryWrapper.orderByDesc("create_time");
        IPage<PartyMember> pageResult = partyMemberMapper.selectPage(page, queryWrapper);
        return pageResult;
    }

    @Override
    public PartyBaseInformaction getbaseInfomation(Long partyCommitteeId, Long partBranchId) {
        PartyBranch partyBranch = partyBranchServiceImpl.getById(partBranchId);
        PartyBaseInformaction baseInformaction = new PartyBaseInformaction();
        if (partyBranch != null) {
            baseInformaction.setSecretary(partyBranch.getSecretary());
            baseInformaction.setAddress(partyBranch.getAddress());
            baseInformaction.setGender(partyBranch.getGender());
            baseInformaction.setPhoneNumber(partyBranch.getPhoneNumber());
        }
        List<PartyMemberAgeInfoDto> partyMemberAgeInfos = partyMemberMapper.getPartyMemberAgeInfo(partyCommitteeId,
            partBranchId);
        if (CollectionUtils.isEmpty(partyMemberAgeInfos)) {
            baseInformaction.setTotalManPartyMember(0);
            baseInformaction.setTotalPartyMember(0);
            baseInformaction.setTotalWomanPartyMember(0);
            baseInformaction.setYear35To45Total(0);
            baseInformaction.setYear46To54Total(0);
            baseInformaction.setYear55To59Total(0);
            baseInformaction.setYearover60Total(0);
            baseInformaction.setYearunder35Total(0);
        }
        Integer totalPartyMember = partyMemberAgeInfos.size();
        List<PartyMemberAgeInfoDto> manList = partyMemberAgeInfos.stream().filter(ageInfo -> ageInfo.getGender() == 1)
            .collect(Collectors.toList());
        Integer totalManPartyMember = CollectionUtils.isEmpty(manList) ? 0 : manList.size();
        Integer totalWomanPartyMember = totalPartyMember - totalManPartyMember;
        baseInformaction.setTotalManPartyMember(totalManPartyMember);
        baseInformaction.setTotalPartyMember(totalPartyMember);
        baseInformaction.setTotalWomanPartyMember(totalWomanPartyMember);

        Integer yearunder35Total = partyMemberAgeInfos.stream().filter(ageInfo -> ageInfo.getAge() < 35)
            .collect(Collectors.toList()).size();
        Integer year35To45Total = partyMemberAgeInfos.stream()
            .filter(ageInfo -> ageInfo.getAge() >= 35 && ageInfo.getAge() <= 45).collect(Collectors.toList()).size();
        Integer year46To54Total = partyMemberAgeInfos.stream()
            .filter(ageInfo -> ageInfo.getAge() >= 46 && ageInfo.getAge() <= 54).collect(Collectors.toList()).size();
        Integer year55To59Total = partyMemberAgeInfos.stream()
            .filter(ageInfo -> ageInfo.getAge() >= 55 && ageInfo.getAge() <= 59).collect(Collectors.toList()).size();
        Integer yearover60Total = partyMemberAgeInfos.stream().filter(ageInfo -> ageInfo.getAge() >= 60)
            .collect(Collectors.toList()).size();
        baseInformaction.setYear35To45Total(year35To45Total);
        baseInformaction.setYear46To54Total(year46To54Total);
        baseInformaction.setYear55To59Total(year55To59Total);
        baseInformaction.setYearover60Total(yearover60Total);
        baseInformaction.setYearunder35Total(yearunder35Total);
        return baseInformaction;
    }

    @Override
    public PartyMember getPartyMemberByIdCardNo(String idCardNo) {
        QueryWrapper<PartyMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id_card_no", idCardNo);
        return getOne(queryWrapper);
    }

    @Override
    public MyArchivesDto getArchivesByIdCardNo(String idCardNo) {
        PartyMember partyMember = getPartyMemberByIdCardNo(idCardNo);
        if (partyMember == null) {
            return null;
        }
        MyArchivesDto myArchivesDto = new MyArchivesDto();
        myArchivesDto.setHeadSculpture(partyMember.getHeadSculpture());
        myArchivesDto.setGender(partyMember.getGender());
        myArchivesDto.setIdCardNo(idCardNo);
        myArchivesDto.setName(partyMember.getName());
        myArchivesDto.setPartyBranchName(partyMember.getPartyBranchName());
        myArchivesDto.setPartyCategoryName(partyMember.getPartyCategoryName());
        myArchivesDto.setPartyCommitteeName(partyMember.getPartyCommitteeName());
        myArchivesDto.setPartyDate(partyMember.getPartyDate());
        myArchivesDto.setPhoneNumber(partyMember.getPhoneNumber());
        myArchivesDto.setResidentialAddress(partyMember.getResidentialAddress());
        myArchivesDto.setUnitAddress(partyMember.getUnitAddress());
        return myArchivesDto;
    }

    @Override
    public void savePartyIn(PartyInOutApply partyInOutApply, AuditReq audit, Long userId) {
        PartyMember partyMember = getPartyMemberByIdCardNo(partyInOutApply.getIdCardNo());
        if (partyMember == null) {
            partyMember = new PartyMember();
            partyMember.setCreateTime(new Date());
            partyMember.setCreateUser(userId);
        }
        partyMember.setHeadSculpture(partyInOutApply.getHeadSculpture());
        partyMember.setBirthday(partyInOutApply.getBirthday());
        partyMember.setIntoTime(new Date());
        partyMember.setIsInto(1);
        partyMember.setEducationId(partyInOutApply.getEducationId());
        partyMember.setEducationName(partyInOutApply.getEducationName());
        partyMember.setGender(partyInOutApply.getGender());
        partyMember.setIdCardNo(partyInOutApply.getIdCardNo());
        partyMember.setName(partyInOutApply.getName());
        partyMember.setNation(partyInOutApply.getNation());
        partyMember.setNativePlace(partyInOutApply.getNativePlace());
        partyMember.setPartyBranchId(audit.getPartyBranchId());
        partyMember.setPartyBranchName(audit.getPartyBranchName());
        partyMember.setPartyCategoryId(partyInOutApply.getPartyCategoryId());
        partyMember.setPartyCategoryName(partyInOutApply.getPartyCategoryName());
        partyMember.setPartyCommitteeId(audit.getPartyCommitteeId());
        partyMember.setPartyCommitteeName(audit.getPartyCommitteeName());
        partyMember.setPartyDate(partyInOutApply.getPartyDate());
        partyMember.setPhoneNumber(partyInOutApply.getPhoneNumber());
        partyMember.setResidentialAddress(partyInOutApply.getResidentialAddress());
        partyMember.setUnitAddress(partyInOutApply.getUnitAddress());
        super.saveOrUpdate(partyMember);
    }

    @Override
    public List<StatisticalAnalysisDto> partyCategoriesAnalysis(Long partyCommitteeId, Long partyBranchId) {
        return partyMemberMapper.partyCategoriesAnalysis(partyCommitteeId, partyBranchId);
    }

    @Override
    public List<StatisticalAnalysisDto> gridPartyanalysis(Long partyCommitteeId) {
        return partyMemberMapper.gridPartyanalysis(partyCommitteeId);
    }

    @Override
    public void updateOutPass(String idCardNo) {
        QueryWrapper<PartyMember> updateWrapper = new QueryWrapper<>();
        updateWrapper.eq("id_card_no", idCardNo);
        PartyMember partyMember = new PartyMember();
        partyMember.setIsInto(2);
        super.update(partyMember, updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        PartyMember partyMember = getById(id);
        String idCardNo = partyMember.getIdCardNo();
        partyInOutApplyMapper.deleteIdCardNo(idCardNo);
        super.removeById(id);
    }

    @Override
    public ReportPartyMemberAageStatistics getPartyMemberStatisticsAge(List<Long> committeeIds) {
        ReportPartyMemberAageStatistics memberAageStatistics = new ReportPartyMemberAageStatistics();
        List<PartyMemberAgeInfoDto> partyMemberAgeInfos = partyMemberMapper.getPartyMemberStatisticsAge(committeeIds);
        if (CollectionUtils.isEmpty(partyMemberAgeInfos)) {
            memberAageStatistics.setTotalPartyMember(0);
            memberAageStatistics.setYear35To45Total(0);
            memberAageStatistics.setYear46To54Total(0);
            memberAageStatistics.setYear55To59Total(0);
            memberAageStatistics.setYearover60Total(0);
            memberAageStatistics.setYearunder35Total(0);
            return memberAageStatistics;
        }
        Integer totalPartyMember = partyMemberAgeInfos.size();
        memberAageStatistics.setTotalPartyMember(totalPartyMember);
        Integer yearunder35Total = partyMemberAgeInfos.stream().filter(ageInfo -> ageInfo.getAge() < 35)
            .collect(Collectors.toList()).size();
        Integer year35To45Total = partyMemberAgeInfos.stream()
            .filter(ageInfo -> ageInfo.getAge() >= 35 && ageInfo.getAge() <= 45).collect(Collectors.toList()).size();
        Integer year46To54Total = partyMemberAgeInfos.stream()
            .filter(ageInfo -> ageInfo.getAge() >= 46 && ageInfo.getAge() <= 54).collect(Collectors.toList()).size();
        Integer year55To59Total = partyMemberAgeInfos.stream()
            .filter(ageInfo -> ageInfo.getAge() >= 55 && ageInfo.getAge() <= 59).collect(Collectors.toList()).size();
        Integer yearover60Total = partyMemberAgeInfos.stream().filter(ageInfo -> ageInfo.getAge() >= 60)
            .collect(Collectors.toList()).size();
        memberAageStatistics.setYear35To45Total(year35To45Total);
        memberAageStatistics.setYear46To54Total(year46To54Total);
        memberAageStatistics.setYear55To59Total(year55To59Total);
        memberAageStatistics.setYearover60Total(yearover60Total);
        memberAageStatistics.setYearunder35Total(yearunder35Total);
        return memberAageStatistics;
    }

    @Override
    public List<ReportPartyMemberDistribute> reportPartyMemberDistribute() {
        return partyMemberMapper.getReportPartyMemberDistribute();

    }

    @Override
    public List<ReportPartyInOut> reportPartyInOut(List<Long> committeeIds) {
        return partyMemberMapper.getReportPartyInOut(committeeIds);
    }

}
