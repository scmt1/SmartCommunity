package me.zhengjie.service.party.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.common.utils.ResultCode;
import me.zhengjie.dto.MyDuesDto;
import me.zhengjie.dto.StatisticalAnalysisDto;
import me.zhengjie.entity.party.PartyDues;
import me.zhengjie.entity.party.PartyMember;
import me.zhengjie.common.BusinessException;
import me.zhengjie.mapper.party.PartyDuesMapper;
import me.zhengjie.mapper.party.PartyMemberMapper;
import me.zhengjie.req.PartyDuesReq;
import me.zhengjie.service.party.IPartyDuesService;
import me.zhengjie.service.party.IPartyMemberService;

import lombok.AllArgsConstructor;

/**
 * <p>
 * 党费 服务实现类
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-13
 */
@Service
@AllArgsConstructor
public class PartyDuesServiceImpl extends ServiceImpl<PartyDuesMapper, PartyDues> implements IPartyDuesService {

    private final PartyDuesMapper     partyDuesMapper;

    private final PartyMemberMapper   partyMemberMapper;

    private final IPartyMemberService partyMemberService;

    @Override
    public IPage<PartyDues> getList(Long partyMemberId, Date startDate, Date enDate, Integer current, Integer size) {
        IPage<PartyDues> page = new Page<>(current, size);
        QueryWrapper<PartyDues> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("party_member_id", partyMemberId);
        queryWrapper.gt(startDate != null, "pay_date", startDate);
        queryWrapper.lt(startDate != null, "pay_date", enDate);
        queryWrapper.orderByDesc("create_time");
        return partyDuesMapper.selectPage(page, queryWrapper);
    }

    @Override
    public void add(Long userId, PartyDuesReq partyDuesReq) {
        PartyDues partyDues = new PartyDues();
        partyDues.setAmount(partyDuesReq.getAmount());
        partyDues.setCreateTime(new Date());
        partyDues.setCreateUser(userId);
        partyDues.setPartyMemberId(partyDuesReq.getPartyMemberId());
        partyDues.setPayDate(partyDuesReq.getPayDate());
        PartyMember member = partyMemberMapper.selectById(partyDuesReq.getPartyMemberId());
        if (member != null) {
            partyDues.setPartyCategoryId(member.getPartyCategoryId());
            partyDues.setPartyCategoryName(member.getPartyCategoryName());
            partyDues.setPartyCommitteeId(member.getPartyCommitteeId());
            partyDues.setPartyCommitteeName(member.getPartyCommitteeName());
            partyDues.setPartyBranchId(member.getPartyBranchId());
            partyDues.setPartyBranchName(member.getPartyBranchName());
        }
        //        partyDues.setQuarter(partyDuesReq.getQuarter());
        partyDues.setRemark(partyDuesReq.getRemark());
        super.save(partyDues);
    }

    @Override
    public void edit(Long id, Long userId, PartyDuesReq partyDuesReq) {
        PartyDues partyDues = getById(id);
        if (partyDues == null) {
            throw new BusinessException(ResultCode.FAILURE);
        }
        partyDues.setAmount(partyDuesReq.getAmount());
        partyDues.setUpdateTime(new Date());
        partyDues.setUpdateUser(userId);
        partyDues.setPartyMemberId(partyDuesReq.getPartyMemberId());
        partyDues.setPayDate(partyDuesReq.getPayDate());
        //        partyDues.setQuarter(partyDuesReq.getQuarter());
        partyDues.setRemark(partyDuesReq.getRemark());
        super.updateById(partyDues);
    }

    @Override
    public IPage<MyDuesDto> getDuesDtoByIdCardNo(String idCardNo, Integer current, Integer size) {
        IPage<MyDuesDto> page = new Page<>(current, size);
        PartyMember partyMember = partyMemberService.getPartyMemberByIdCardNo(idCardNo);
        if (partyMember == null) {
            return page;
        }
        List<MyDuesDto> records = partyDuesMapper.getMyMyDues(page, partyMember.getId());
        page.setRecords(records);
        return page;
    }

    @Override
    public List<StatisticalAnalysisDto> partyPaymentAnalysis(Long partyCommitteeId, Long partyBranchId, String time) {

        return partyDuesMapper.partyPaymentAnalysis(partyCommitteeId, partyBranchId, time);
    }

}
