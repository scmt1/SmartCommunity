package me.zhengjie.service.party.impl;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.common.utils.ResultCode;
import me.zhengjie.dto.LessonsMemberDto;
import me.zhengjie.dto.PartyThreeLessonsDto;
import me.zhengjie.dto.PartyThreeLessonsListDto;
import me.zhengjie.dto.ReportThreeLessons;
import me.zhengjie.entity.party.PartyLessonsMember;
import me.zhengjie.entity.party.PartyThreeLessons;
import me.zhengjie.common.BusinessException;
import me.zhengjie.mapper.party.PartyThreeLessonsMapper;
import me.zhengjie.req.PartyMemberReq;
import me.zhengjie.req.PartyThreeLessonsSeq;
import me.zhengjie.service.party.IPartyLessonsMemberService;
import me.zhengjie.service.party.IPartyThreeLessonsService;

import lombok.AllArgsConstructor;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-12
 */
@AllArgsConstructor
@Service
public class PartyThreeLessonsServiceImpl extends ServiceImpl<PartyThreeLessonsMapper, PartyThreeLessons>
                                          implements IPartyThreeLessonsService {

    private final PartyThreeLessonsMapper    partyThreeLessonsMapper;

    private final IPartyLessonsMemberService partyLessonsMemberService;

    @Override
    public IPage<PartyThreeLessons> getList(String topic, Long partBranchId, Long partyCommitteeId,
                                            Integer partyCategoryId, Integer current, Integer size) {
        IPage<PartyThreeLessons> page = new Page<>(current, size);
        QueryWrapper<PartyThreeLessons> queryWrapper = new QueryWrapper<PartyThreeLessons>();
        if (!StringUtils.isEmpty(topic)) {
            queryWrapper.like("topic", topic);
        }
        if (partBranchId != null && partBranchId != 0) {
            queryWrapper.eq("part_branch_id", partBranchId);
        }
        if (partyCommitteeId != null && partyCommitteeId != 0) {
            queryWrapper.eq("party_committee_id", partyCommitteeId);
        }
        if (partyCategoryId != null && partyCategoryId != 0) {
            queryWrapper.eq("party_category_id", partyCategoryId);
        }
        queryWrapper.orderByDesc("create_time");
        return partyThreeLessonsMapper.selectPage(page, queryWrapper);
    }

    @Override
    @Transactional
    public void add(Long userId, PartyThreeLessonsSeq partyThreeLessonsReq) {
        List<PartyMemberReq> lessonMembers = partyThreeLessonsReq.getLessonsMembers();
        if (lessonMembers == null || lessonMembers.size() < 1) {
            throw new BusinessException(ResultCode.FAILURE);
        }
        PartyThreeLessons ptl = seq2Entity(partyThreeLessonsReq);
        ptl.setCreateTime(Calendar.getInstance().getTime());
        ptl.setCreateUser(userId);
        ptl.setUpdateUser(userId);
        partyThreeLessonsMapper.insert(ptl);
        Long partyLessonsId = ptl.getId();
        List<PartyLessonsMember> entityList = lessonMembers.stream().map(item -> {
            PartyLessonsMember partyLessonsMember = new PartyLessonsMember();
            partyLessonsMember.setPartyLessonsId(partyLessonsId);
            partyLessonsMember.setPartyMemberId(item.getId());
            partyLessonsMember.setCreateUser(userId);
            partyLessonsMember.setUpdateUser(userId);
            partyLessonsMember.setMemberHeadSculpture(item.getHeadSculpture());
            partyLessonsMember.setPartyMemberName(item.getName());
            partyLessonsMember.setCreateTime(Calendar.getInstance().getTime());
            partyLessonsMember.setUpdateTime(Calendar.getInstance().getTime());
            return partyLessonsMember;
        }).collect(Collectors.toList());
        partyLessonsMemberService.saveBatch(entityList);
    }

    private PartyThreeLessons seq2Entity(PartyThreeLessonsSeq partyThreeLessonsReq) {
        PartyThreeLessons ptl = new PartyThreeLessons();
        //        ptl.setCreateUser(createUser);
        ptl.setInitiator(partyThreeLessonsReq.getInitiator());
        ptl.setLessonsRequire(partyThreeLessonsReq.getLessonsRequire());
        ptl.setLessonsSite(partyThreeLessonsReq.getLessonsSite());
        ptl.setLessonsTime(partyThreeLessonsReq.getLessonsTime());
        ptl.setLessonsSite(partyThreeLessonsReq.getLessonsSite());
        ptl.setMeetingResults(partyThreeLessonsReq.getMeetingResults());
        ptl.setPartyBranchId(partyThreeLessonsReq.getPartyBranchId());
        ptl.setPartyBranchName(partyThreeLessonsReq.getPartyBranchName());
        ptl.setPartyCategoryId(partyThreeLessonsReq.getPartyCategoryId());
        ptl.setPartyCommitteeId(partyThreeLessonsReq.getPartyCommitteeId());
        ptl.setPartyCommitteeName(partyThreeLessonsReq.getPartyCommitteeName());
        ptl.setTopic(partyThreeLessonsReq.getTopic());
        ptl.setMeetingResults(partyThreeLessonsReq.getMeetingResults());
        ptl.setUpdateTime(Calendar.getInstance().getTime());
        return ptl;
    }

    @Override
    public void edit(Long userId, Long id, PartyThreeLessonsSeq partyThreeLessonsReq) {
        List<PartyMemberReq> lessonMembers = partyThreeLessonsReq.getLessonsMembers();
        if (lessonMembers == null || lessonMembers.size() < 1) {
            throw new BusinessException(ResultCode.FAILURE);
        }
        PartyThreeLessons ptl = seq2Entity(partyThreeLessonsReq);
        ptl.setId(id);
        ptl.setUpdateUser(userId);
        partyThreeLessonsMapper.updateById(ptl);
        partyLessonsMemberService.deletByLessonsId(id);
        Long partyLessonsId = ptl.getId();
        List<PartyLessonsMember> entityList = lessonMembers.stream().map(item -> {
            PartyLessonsMember partyLessonsMember = new PartyLessonsMember();
            partyLessonsMember.setPartyLessonsId(partyLessonsId);
            partyLessonsMember.setPartyMemberId(item.getId());
            partyLessonsMember.setUpdateUser(userId);
            partyLessonsMember.setMemberHeadSculpture(item.getHeadSculpture());
            partyLessonsMember.setPartyMemberName(item.getName());
            partyLessonsMember.setCreateTime(Calendar.getInstance().getTime());
            partyLessonsMember.setUpdateTime(Calendar.getInstance().getTime());
            return partyLessonsMember;
        }).collect(Collectors.toList());
        partyLessonsMemberService.saveBatch(entityList);
    }

    @Override
    public PartyThreeLessonsDto detail(Long id) {
        PartyThreeLessons ptl = partyThreeLessonsMapper.selectById(id);
        if (ptl == null) {
            return new PartyThreeLessonsDto();
        }

        PartyThreeLessonsDto dto = entity2Dto(ptl);
        QueryWrapper<PartyLessonsMember> queryWrapper = new QueryWrapper<PartyLessonsMember>();
        queryWrapper.eq("party_lessons_id", id);
        List<PartyLessonsMember> list = partyLessonsMemberService.list(queryWrapper);
        dto.setLessonsMembers(list.stream().map(item -> {
            return LessonsMemberDto.builder().id(item.getPartyMemberId()).name(item.getPartyMemberName())
                .headSculpture(item.getMemberHeadSculpture()).build();
        }).collect(Collectors.toList()));

        return dto;
    }

    private PartyThreeLessonsDto entity2Dto(PartyThreeLessons ptl) {
        PartyThreeLessonsDto dto = new PartyThreeLessonsDto();
        dto.setId(ptl.getId());
        dto.setInitiator(ptl.getInitiator());
        dto.setLessonsRequire(ptl.getLessonsRequire());
        dto.setLessonsSite(ptl.getLessonsSite());
        dto.setLessonsTime(ptl.getLessonsTime());
        dto.setLessonsSite(ptl.getLessonsSite());
        dto.setMeetingResults(ptl.getMeetingResults());
        dto.setPartyBranchId(ptl.getPartyBranchId());
        dto.setPartyBranchName(ptl.getPartyBranchName());
        dto.setPartyCategoryId(ptl.getPartyCategoryId());
        dto.setPartyCommitteeId(ptl.getPartyCommitteeId());
        dto.setPartyCommitteeName(ptl.getPartyCommitteeName());
        dto.setTopic(ptl.getTopic());
        dto.setPartyCategoryName(ptl.getPartyCategoryName());
        dto.setMeetingResults(ptl.getMeetingResults());
        return dto;
    }

    @Override
    public IPage<PartyThreeLessonsListDto> getListByCategory(Integer partyCategoryId, Integer current, Integer size) {

        IPage<PartyThreeLessons> page = new Page<>(current, size);
        return partyThreeLessonsMapper.getListByCategory(page, partyCategoryId);
    }

    @Override
    public List<ReportThreeLessons> reportThreeLessons(@Param("committeeIds") List<Long> committeeIds) {
        return partyThreeLessonsMapper.getReportThreeLessons(committeeIds);
    }

}
