package me.zhengjie.service.party.impl;

import java.util.Date;

import me.zhengjie.common.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.common.utils.ResultCode;
import me.zhengjie.entity.party.PartyBranch;

import me.zhengjie.mapper.party.PartyBranchMapper;
import me.zhengjie.req.PartyBranchReq;
import me.zhengjie.service.party.IPartyBranchService;

import lombok.AllArgsConstructor;

/**
 * <p>
 * 党支部 服务实现类
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-12
 */
@Service
@AllArgsConstructor
public class PartyBranchServiceImpl extends ServiceImpl<PartyBranchMapper, PartyBranch> implements IPartyBranchService {

    private final PartyBranchMapper partyBranchMapper;

    @Override
    public void add(Long userId, PartyBranchReq partyBranchReq) {
        PartyBranch partyBranch = new PartyBranch();
        partyBranch.setAddress(partyBranchReq.getAddress());
        partyBranch.setCreateTime(new Date());
        partyBranch.setCreateUser(userId);
        partyBranch.setGender(partyBranchReq.getGender());
        partyBranch.setName(partyBranchReq.getName());
        partyBranch.setPartyCommitteeId(partyBranchReq.getPartyCommitteeId());
        partyBranch.setPartyCommitteeName(partyBranchReq.getPartyCommitteeName());
        partyBranch.setPhoneNumber(partyBranchReq.getPhoneNumber());
        partyBranch.setSecretary(partyBranchReq.getSecretary());
        save(partyBranch);
    }

    @Override
    public void edit(Long id, Long userId, PartyBranchReq partyBranchReq) {
        PartyBranch partyBranch = getById(id);
        if (partyBranch == null) {
            throw new BusinessException(ResultCode.FAILURE);
        }
        partyBranch.setAddress(partyBranchReq.getAddress());
        partyBranch.setGender(partyBranchReq.getGender());
        partyBranch.setName(partyBranchReq.getName());
        partyBranch.setPartyCommitteeId(partyBranchReq.getPartyCommitteeId());
        partyBranch.setPartyCommitteeName(partyBranchReq.getPartyCommitteeName());
        partyBranch.setPhoneNumber(partyBranchReq.getPhoneNumber());
        partyBranch.setSecretary(partyBranchReq.getSecretary());
        partyBranch.setUpdateTime(new Date());
        partyBranch.setUpdateUser(userId);
        updateById(partyBranch);
    }

    @Override
    public IPage<PartyBranch> getList(String name, Long jurisdictionId, Long partyCommitteeId, String secretary,
                                      String phoneNumber, Integer current, Integer size) {
        IPage<PartyBranch> page = new Page<>(current, size);
        QueryWrapper<PartyBranch> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(name), "name", name);
        queryWrapper.eq(partyCommitteeId != null && partyCommitteeId != 0, "party_committee_id", partyCommitteeId);
        queryWrapper.like(!StringUtils.isEmpty(secretary), "secretary", secretary);
        queryWrapper.like(!StringUtils.isEmpty(phoneNumber), "phone_number", phoneNumber);
        queryWrapper.orderByDesc("create_time");
        IPage<PartyBranch> pageRult = partyBranchMapper.selectPage(page, queryWrapper);
        return pageRult;
    }

}
