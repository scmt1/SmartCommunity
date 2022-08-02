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
import me.zhengjie.dto.ReportPartyOrganization;
import me.zhengjie.entity.party.PartyCommittee;
import me.zhengjie.common.BusinessException;
import me.zhengjie.mapper.party.PartyCommitteeMapper;
import me.zhengjie.req.PartyCommitteeReq;
import me.zhengjie.service.party.IPartyCommitteeService;

import lombok.AllArgsConstructor;

/**
 * <p>
 * 党委 服务实现类
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-12
 */
@Service
@AllArgsConstructor
public class PartyCommitteeServiceImpl extends ServiceImpl<PartyCommitteeMapper, PartyCommittee>
                                       implements IPartyCommitteeService {

    private final PartyCommitteeMapper partyCommitteeMapper;

    @Override
    public List<Long> getCommitteeIdsByCommunityId(Long communityId) {
        QueryWrapper<PartyCommittee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("community_id", communityId);
        List<PartyCommittee> list = super.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.stream().map(PartyCommittee::getId).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(Long userId, PartyCommitteeReq partyCommitteeReq) {
        PartyCommittee partyCommittee = new PartyCommittee();
        partyCommittee.setAddress(partyCommitteeReq.getAddress());
        partyCommittee.setCreateTime(new Date());
        partyCommittee.setCreateUser(userId);
        partyCommittee.setGender(partyCommitteeReq.getGender());
        partyCommittee.setName(partyCommitteeReq.getName());
        partyCommittee.setPhoneNumber(partyCommitteeReq.getPhoneNumber());
        partyCommittee.setSecretary(partyCommitteeReq.getSecretary());
        partyCommittee.setUpdateTime(new Date());
        partyCommittee.setStreetId(partyCommitteeReq.getStreetId());
        partyCommittee.setCommunityId(partyCommitteeReq.getCommunityId());
        partyCommittee.setCommunityName(partyCommitteeReq.getCommunityName());
        partyCommittee.setStreetName(partyCommitteeReq.getStreetName());
        super.save(partyCommittee);
    }

    public void edit(Long id, Long userId, PartyCommitteeReq partyCommitteeReq) {
        PartyCommittee partyCommittee = getPartyCommitteeById(id);
        if (partyCommittee == null) {
            throw new BusinessException(ResultCode.FAILURE);
        }
        partyCommittee.setAddress(partyCommitteeReq.getAddress());
        partyCommittee.setCreateTime(new Date());
        partyCommittee.setGender(partyCommitteeReq.getGender());
        partyCommittee.setStreetId(partyCommitteeReq.getStreetId());
        partyCommittee.setCommunityId(partyCommitteeReq.getCommunityId());
        partyCommittee.setCommunityName(partyCommitteeReq.getCommunityName());
        partyCommittee.setStreetName(partyCommitteeReq.getStreetName());
        partyCommittee.setName(partyCommitteeReq.getName());
        partyCommittee.setPhoneNumber(partyCommitteeReq.getPhoneNumber());
        partyCommittee.setSecretary(partyCommitteeReq.getSecretary());
        partyCommittee.setUpdateTime(new Date());
        partyCommittee.setUpdateUser(userId);
        super.updateById(partyCommittee);
    }

    public PartyCommittee getPartyCommitteeById(Long id) {
        return super.getById(id);
    }

    public void deleteById(Long id) {
        super.removeById(id);
    }

    public IPage<PartyCommittee> getList(String name, Long communityId, Long streetId, String secretary,
                                         String phoneNumber, Integer current, Integer size) {
        IPage<PartyCommittee> page = new Page<>(current, size);
        QueryWrapper<PartyCommittee> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(name), "name", name);
        queryWrapper.eq(communityId != null && communityId != 0, "community_id", communityId);
        queryWrapper.eq(streetId != null && streetId != 0, "street_id", streetId);
        queryWrapper.like(!StringUtils.isEmpty(secretary), "secretary", secretary);
        queryWrapper.like(!StringUtils.isEmpty(phoneNumber), "phone_number", phoneNumber);
        queryWrapper.orderByDesc("create_time");
        IPage<PartyCommittee> pageRult = partyCommitteeMapper.selectPage(page, queryWrapper);
        return pageRult;
    }

    @Override
    public List<ReportPartyOrganization> reportPartyOrganization() {

        return partyCommitteeMapper.getReportPartyOrganization();

    }

}
