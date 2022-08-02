package me.zhengjie.service.party.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.common.utils.ResultCode;
import me.zhengjie.dto.UserInfomation;
import me.zhengjie.dto.Userinfo;
import me.zhengjie.entity.party.PartyInOutApply;
import me.zhengjie.entity.party.PartyMember;
import me.zhengjie.enums.EducationTypeEnum;
import me.zhengjie.enums.PartyCategoryEnum;
import me.zhengjie.enums.PartyInOutApplyStatus;
import me.zhengjie.enums.PartyInOutApplyTypeEnum;
import me.zhengjie.common.BusinessException;
import me.zhengjie.mapper.party.PartyInOutApplyMapper;
import me.zhengjie.req.ArchivesInReq;
import me.zhengjie.req.AuditReq;
import me.zhengjie.service.party.IPartyInOutApplyService;
import me.zhengjie.service.party.IPartyMemberService;

import lombok.AllArgsConstructor;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-14
 */
@AllArgsConstructor
@Service
public class PartyInOutApplyServiceImpl extends ServiceImpl<PartyInOutApplyMapper, PartyInOutApply>
                                        implements IPartyInOutApplyService {

    private final IPartyMemberService   partyMemberService;

    private final PartyInOutApplyMapper partyInOutApplyMapper;

    @Override
    public void outApply(UserInfomation userInfomation) {
        PartyMember partMember = partyMemberService.getPartyMemberByIdCardNo(userInfomation.getIdNumber());
        //        PartyInOutApply entity = getByIdCardNo(userInfomation.getIdNumber());
        PartyInOutApply entity = new PartyInOutApply();
        //        if (entity == null) {
        //            entity = new PartyInOutApply();
        //            entity.setRejectReason("");
        //        }
        entity.setApplyType(PartyInOutApplyTypeEnum.OUT.getCode());
        entity.setApplyStatus(PartyInOutApplyStatus.OUT.getCode());
        entity.setBirthday(partMember.getBirthday());
        entity.setCreateTime(Calendar.getInstance().getTime());
        entity.setCreateUser(partMember.getId());
        entity.setEducationId(partMember.getEducationId());
        entity.setEducationName(EducationTypeEnum.getDescByCode(partMember.getEducationId()));
        entity.setGender(partMember.getGender());
        entity.setIdCardNo(partMember.getIdCardNo());
        entity.setName(partMember.getName());
        entity.setNation(partMember.getNation());
        entity.setNativePlace(partMember.getNativePlace());
        entity.setOrganizationInfo(partMember.getPartyBranchName());
        entity.setPhoneNumber(partMember.getPhoneNumber());
        entity.setResidentialAddress(partMember.getResidentialAddress());
        entity.setUnitAddress(partMember.getUnitAddress());
        entity.setCreateTime(Calendar.getInstance().getTime());
        entity.setUpdateTime(Calendar.getInstance().getTime());
        entity.setUpdateUser(userInfomation.getId());
        entity.setCreateUser(userInfomation.getId());
        this.save(entity);

    }

    @Override
    public void inApply(UserInfomation userInfomation, ArchivesInReq archivesInReq) {
        //        String idCardNo = archivesInReq.getIdCardNo();
        PartyInOutApply entity = new PartyInOutApply();// getByIdCardNo(idCardNo);
        //        if (entity == null) {
        //            entity = new PartyInOutApply();
        //            entity.setRejectReason("");
        //        }
        entity.setApplyType(PartyInOutApplyTypeEnum.IN.getCode());
        entity.setApplyStatus(PartyInOutApplyStatus.IN.getCode());
        entity.setBirthday(archivesInReq.getBirthday());
        entity.setPartyDate(archivesInReq.getPartyDate());
        entity.setCreateTime(Calendar.getInstance().getTime());
        entity.setCreateUser(userInfomation.getId());
        entity.setEducationId(archivesInReq.getEducationId());
        entity.setEducationName(EducationTypeEnum.getDescByCode(archivesInReq.getEducationId()));
        entity.setHeadSculpture(archivesInReq.getHeadSculpture());
        entity.setPartyCategoryId(archivesInReq.getPartyCategoryId());
        entity.setPartyCategoryName(PartyCategoryEnum.getNameByCode(archivesInReq.getPartyCategoryId()));
        entity.setGender(archivesInReq.getGender());
        entity.setIdCardNo(archivesInReq.getIdCardNo());
        entity.setName(archivesInReq.getName());
        entity.setNation(archivesInReq.getNation());
        entity.setNativePlace(archivesInReq.getNativePlace());
        entity.setOrganizationInfo(archivesInReq.getOrganizationInfo());
        entity.setPhoneNumber(userInfomation.getPhone());
        entity.setResidentialAddress(archivesInReq.getResidentialAddress());
        entity.setUnitAddress(archivesInReq.getUnitAddress());
        entity.setCreateTime(Calendar.getInstance().getTime());
        entity.setUpdateTime(Calendar.getInstance().getTime());
        entity.setUpdateUser(userInfomation.getId());
        entity.setCreateUser(userInfomation.getId());
        saveOrUpdate(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void adopt(Long userId, AuditReq audit, Long id) {
        PartyInOutApply partyInOutApply = getById(id);
        if (partyInOutApply == null) {
            throw new BusinessException(ResultCode.FAILURE.getCode());
        }
        partyInOutApply.setApplyStatus(PartyInOutApplyStatus.ADOPT.getCode());
        partyInOutApply.setUpdateUser(userId);
        partyInOutApply.setUpdateTime(Calendar.getInstance().getTime());
        this.updateById(partyInOutApply);
        if (partyInOutApply.getApplyType() == PartyInOutApplyTypeEnum.IN.getCode()) {
            partyMemberService.savePartyIn(partyInOutApply, audit, userId);
        } else {
            partyMemberService.updateOutPass(partyInOutApply.getIdCardNo());
        }
    }

    @Override
    public void reject(Long userId, Long id, String rejectReason) {
        PartyInOutApply entity = new PartyInOutApply();
        entity.setId(id);
        entity.setApplyStatus(PartyInOutApplyStatus.REJECT.getCode());
        entity.setUpdateUser(userId);
        entity.setUpdateTime(Calendar.getInstance().getTime());
        entity.setRejectReason(rejectReason);
        this.updateById(entity);
    }

    @Override
    public IPage<PartyInOutApply> getList(String name, Integer gender, Integer educationId, String idCardNo,
                                          String phoneNumber, Integer applyStatus, Date startTime, Date endTime,
                                          Integer current, Integer size) {

        IPage<PartyInOutApply> page = new Page<>(current, size);
        QueryWrapper<PartyInOutApply> queryWrapper = new QueryWrapper<PartyInOutApply>();
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like("name", name);
        }
        if (!StringUtils.isEmpty(idCardNo)) {
            queryWrapper.like("id_card_no", idCardNo);
        }
        if (!StringUtils.isEmpty(phoneNumber)) {
            queryWrapper.like("phone_number", phoneNumber);
        }
        if (gender != null && gender != 0) {
            queryWrapper.eq("gender", gender);
        }
        if (applyStatus != null && applyStatus != 0) {
            queryWrapper.eq("apply_status", applyStatus);
        }
        if (educationId != null && educationId != 0) {
            queryWrapper.eq("education_id", educationId);
        }
        if (startTime != null) {
            queryWrapper.ge("create_time", startTime);
        }
        if (endTime != null) {
            queryWrapper.le("create_time", endTime);
        }
        queryWrapper.orderByDesc("create_time");
        return partyInOutApplyMapper.selectPage(page, queryWrapper);
    }

    @Override
    public PartyInOutApply getByIdCardNo(String idCardNo) {
        QueryWrapper<PartyInOutApply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id_card_no", idCardNo);
        queryWrapper.orderByDesc("create_time");
        List<PartyInOutApply> list = partyInOutApplyMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public Userinfo getUserInfo(UserInfomation userInfomation) {
        String idCardNo = userInfomation.getIdNumber();
        Userinfo userinfo = new Userinfo();
        PartyInOutApply partyInOutApply = getByIdCardNo(idCardNo);
        PartyMember partyMember = partyMemberService.getPartyMemberByIdCardNo(idCardNo);
        String sex = userInfomation.getSex();
        if (partyInOutApply == null && partyMember == null) {
            userinfo.setStatus(2);
            return userinfo;
        }

        if (StringUtils.isNotBlank(sex) && partyMember != null) {
            Integer gender = sex.equals("男") ? 1 : 0;
            if (partyMember.getGender() != gender) {
                partyMember.setGender(gender);
                partyMemberService.updateById(partyMember);
            }
        }

        if (partyMember != null && partyInOutApply != null
            && partyInOutApply.getApplyType() == PartyInOutApplyTypeEnum.OUT.getCode()
            && partyInOutApply.getApplyStatus() == PartyInOutApplyStatus.OUT.getCode()) {
            userinfo.setGender(partyMember.getGender());
            userinfo.setPartyBranchName(partyMember.getPartyBranchName());
            userinfo.setStatus(5);
            return userinfo;
        }

        if (partyMember != null && partyMember.getStatus() == null) {
            userinfo.setGender(partyMember.getGender());
            userinfo.setPartyBranchName(partyMember.getPartyBranchName());
            userinfo.setStatus(1);
            return userinfo;
        }

        if (partyMember != null && partyMember.getIsInto() != null && partyMember.getIsInto() == 1) {
            userinfo.setGender(partyMember.getGender());
            userinfo.setPartyBranchName(partyMember.getPartyBranchName());
            userinfo.setStatus(1);
            return userinfo;
        }

        if (partyMember != null && partyMember.getIsInto() == 2
            && (partyInOutApply != null && partyInOutApply.getApplyType() == PartyInOutApplyTypeEnum.OUT.getCode()
                && partyInOutApply.getApplyStatus() == PartyInOutApplyStatus.ADOPT.getCode())) {
            userinfo.setGender(partyMember.getGender());
            userinfo.setPartyBranchName(partyMember.getPartyBranchName());
            userinfo.setStatus(2);
            return userinfo;
        }

        if (partyMember == null
            && (partyInOutApply != null && partyInOutApply.getApplyType() == PartyInOutApplyTypeEnum.OUT.getCode()
                && partyInOutApply.getApplyStatus() == PartyInOutApplyStatus.REJECT.getCode())) {
            userinfo.setGender(partyInOutApply.getGender());
            userinfo.setStatus(2);
            return userinfo;
        }

        if (partyMember == null && partyInOutApply != null
            && partyInOutApply.getApplyType() == PartyInOutApplyTypeEnum.IN.getCode()
            && partyInOutApply.getApplyStatus() == PartyInOutApplyStatus.ADOPT.getCode()) {
            userinfo.setGender(partyInOutApply.getGender());
            userinfo.setStatus(2);
            return userinfo;
        }

        if (partyInOutApply != null) {
            //转入
            if (partyInOutApply.getApplyType() == PartyInOutApplyTypeEnum.IN.getCode()) {
                if (partyInOutApply.getApplyStatus() == PartyInOutApplyStatus.IN.getCode()) {
                    userinfo.setGender(partyInOutApply.getGender());
                    userinfo.setStatus(3);
                    return userinfo;
                }
                if (partyInOutApply.getApplyStatus() == PartyInOutApplyStatus.ADOPT.getCode()) {
                    if (partyMember != null) {
                        userinfo.setStatus(1);
                        userinfo.setPhoneNumber(partyMember.getPhoneNumber());
                        userinfo.setPartyBranchName(partyMember.getPartyBranchName());
                        userinfo.setHeadSculpture(partyMember.getHeadSculpture());
                        userinfo.setPartyBranchName(partyMember.getPartyBranchName());
                        userinfo.setGender(partyMember.getGender());
                        return userinfo;
                    }
                }
                if (partyInOutApply.getApplyStatus() == PartyInOutApplyStatus.REJECT.getCode()) {
                    userinfo.setGender(partyInOutApply.getGender());
                    userinfo.setStatus(4);
                    userinfo.setReject(partyInOutApply.getRejectReason());
                    return userinfo;
                }
            } else {
                if (partyInOutApply.getApplyStatus() == PartyInOutApplyStatus.OUT.getCode()) {
                    userinfo.setPhoneNumber(partyInOutApply.getPhoneNumber());
                    userinfo.setGender(partyInOutApply.getGender());
                    userinfo.setStatus(5);
                    return userinfo;
                }
                if (partyInOutApply.getApplyStatus() == PartyInOutApplyStatus.ADOPT.getCode()) {
                    userinfo.setPhoneNumber(partyInOutApply.getPhoneNumber());
                    userinfo.setGender(partyInOutApply.getGender());
                    userinfo.setStatus(7);
                    return userinfo;
                }
                if (partyInOutApply.getApplyStatus() == PartyInOutApplyStatus.REJECT.getCode()) {
                    userinfo.setPhoneNumber(partyInOutApply.getPhoneNumber());
                    userinfo.setGender(partyInOutApply.getGender());
                    userinfo.setStatus(6);
                    userinfo.setReject(partyInOutApply.getRejectReason());
                    return userinfo;
                }
            }
            return userinfo;
        }
        return userinfo;
    }

    public void deleteIdCardNo(String idCardNo) {
        QueryWrapper<PartyInOutApply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id_card_no", idCardNo);
        super.remove(queryWrapper);
    }

}
