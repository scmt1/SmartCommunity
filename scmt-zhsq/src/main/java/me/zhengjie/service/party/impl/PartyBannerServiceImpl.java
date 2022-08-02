package me.zhengjie.service.party.impl;

import java.util.Date;
import java.util.List;

import me.zhengjie.common.BusinessException;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.common.utils.ResultCode;
import me.zhengjie.entity.party.PartyBanner;

import me.zhengjie.mapper.party.PartyBannerMapper;
import me.zhengjie.req.PartyBannerReq;
import me.zhengjie.service.party.IPartyBannerService;

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
public class PartyBannerServiceImpl extends ServiceImpl<PartyBannerMapper, PartyBanner> implements IPartyBannerService {

    @Override
    public void add(Long userId, PartyBannerReq partyBannerReq) {

        PartyBanner partyBanner = new PartyBanner();
        partyBanner.setCreateTime(new Date());
        partyBanner.setCreateUser(userId);
        partyBanner.setImgUrl(partyBannerReq.getImgUrl());
        partyBanner.setTitle(partyBannerReq.getTitle());
        super.save(partyBanner);
    }

    @Override
    public void edit(Long id, Long userId, PartyBannerReq partyBannerReq) {
        PartyBanner partyBanner = getById(id);
        if (partyBanner == null) {
            throw new BusinessException(ResultCode.FAILURE);
        }
        partyBanner.setUpdateTime(new Date());
        partyBanner.setUpdateUser(userId);
        partyBanner.setImgUrl(partyBannerReq.getImgUrl());
        partyBanner.setTitle(partyBannerReq.getTitle());
        super.updateById(partyBanner);
    }

    @Override
    public List<PartyBanner> getList(String title) {
        QueryWrapper<PartyBanner> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("title", title);
        return super.list(queryWrapper);
    }

}
