package me.zhengjie.service.party.impl;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.common.utils.ResultCode;
import me.zhengjie.entity.party.PartyCommend;
import me.zhengjie.common.BusinessException;
import me.zhengjie.mapper.party.PartyCommendMapper;
import me.zhengjie.req.PartyCommendReq;
import me.zhengjie.service.party.IPartyCommendService;

import lombok.AllArgsConstructor;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-13
 */
@Service
@AllArgsConstructor
public class PartyCommendServiceImpl extends ServiceImpl<PartyCommendMapper, PartyCommend>
                                     implements IPartyCommendService {

    private final PartyCommendMapper partyCommendMapper;

    @Override
    public IPage<PartyCommend> getList(Long partyMemberId, String startDate, String endDate, Integer current,
                                       Integer size) {
        IPage<PartyCommend> page = new Page<>(current, size);
        QueryWrapper<PartyCommend> queryWrapper = new QueryWrapper<>();

        if (!StringUtils.isEmpty(startDate)) {
            queryWrapper.ge("commend_date", startDate);
        }
        if (!StringUtils.isEmpty(endDate)) {
            queryWrapper.le("commend_date", endDate);
        }
        if (partyMemberId != null && partyMemberId != 0) {
            queryWrapper.eq("party_member_id", partyMemberId);
        }
        queryWrapper.orderByDesc("create_time");
        return partyCommendMapper.selectPage(page, queryWrapper);
    }

    @Override
    public void add(Long userId, PartyCommendReq partyCommendReq) {
        PartyCommend partyCommend = new PartyCommend();
        partyCommend.setCommendDate(partyCommendReq.getCommendDate());
        partyCommend.setContent(partyCommendReq.getContent());
        partyCommend.setCreateTime(new Date());
        partyCommend.setCreateUser(userId);
        partyCommend.setPartyMemberId(partyCommendReq.getPartyMemberId());
        partyCommend.setTitle(partyCommendReq.getTitle());
        super.save(partyCommend);
    }

    @Override
    public void edit(Long id, Long userId, PartyCommendReq partyCommendReq) {
        PartyCommend partyCommend = getById(id);
        if (partyCommend == null) {
            throw new BusinessException(ResultCode.FAILURE);
        }
        partyCommend.setCommendDate(partyCommendReq.getCommendDate());
        partyCommend.setContent(partyCommendReq.getContent());
        partyCommend.setUpdateTime(new Date());
        partyCommend.setCreateUser(userId);
        partyCommend.setPartyMemberId(partyCommendReq.getPartyMemberId());
        partyCommend.setTitle(partyCommendReq.getTitle());
        super.updateById(partyCommend);
    }

}
