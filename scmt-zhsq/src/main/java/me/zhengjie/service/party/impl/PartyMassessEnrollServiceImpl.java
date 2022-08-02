package me.zhengjie.service.party.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.dto.StatisticalAnalysisDto;
import me.zhengjie.dto.UserInfomation;
import me.zhengjie.entity.party.PartyMasses;
import me.zhengjie.entity.party.PartyMassessEnroll;
import me.zhengjie.mapper.party.PartyMassessEnrollMapper;
import me.zhengjie.service.party.IPartyMassesService;
import me.zhengjie.service.party.IPartyMassessEnrollService;

import lombok.AllArgsConstructor;

/**
 * <p>
 * 党群活动报名 服务实现类
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-13
 */
@Service
@AllArgsConstructor
public class PartyMassessEnrollServiceImpl extends ServiceImpl<PartyMassessEnrollMapper, PartyMassessEnroll>
                                           implements IPartyMassessEnrollService {

    private final IPartyMassesService      partyMassesService;

    private final PartyMassessEnrollMapper partyMassessEnrollMapper;

    @Override
    public void enroll(Long id, UserInfomation userInfomation) {
        PartyMasses partyMassess = partyMassesService.getById(id);

        PartyMassessEnroll partyMassessEnroll = getByUserId(id, userInfomation.getId());
        if (partyMassessEnroll != null) {
            return;
        }

        partyMassessEnroll = new PartyMassessEnroll();
        partyMassessEnroll.setCreateTime(new Date());
        partyMassessEnroll.setPartyMassesId(id);
        partyMassessEnroll.setUserName(userInfomation.getRealName());
        partyMassessEnroll.setPhoneNumber(userInfomation.getPhone());
        partyMassessEnroll.setUserId(userInfomation.getId());
        partyMassessEnroll.setPartyBranchId(partyMassess.getPartyBranchId());
        partyMassessEnroll.setPartyBranchName(partyMassess.getPartyBranchName());
        partyMassessEnroll.setPartyCategoryId(partyMassess.getPartyCategoryId());
        partyMassessEnroll.setPartyCategoryName(partyMassess.getPartyCategoryName());
        partyMassessEnroll.setPartyCommitteeId(partyMassess.getPartyCommitteeId());
        partyMassessEnroll.setPartyCommitteeName(partyMassess.getPartyCommitteeName());
        super.save(partyMassessEnroll);
    }

    private PartyMassessEnroll getByUserId(Long partymassesId, Long userId) {
        QueryWrapper<PartyMassessEnroll> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("party_masses_id", partymassesId);
        queryWrapper.eq("user_id", userId);
        List<PartyMassessEnroll> partyMassessEnrolls = partyMassessEnrollMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(partyMassessEnrolls)) {
            return partyMassessEnrolls.get(0);
        }
        return null;
    }

    @Override
    public IPage<PartyMassessEnroll> getByPartymassesId(Long partymassesId, Integer current, Integer size) {
        IPage<PartyMassessEnroll> page = new Page<>();
        QueryWrapper<PartyMassessEnroll> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("party_masses_id", partymassesId);
        return partyMassessEnrollMapper.selectPage(page, queryWrapper);
    }

    @Override
    public List<StatisticalAnalysisDto> pushcrowdAnalysis(Long partyCommitteeId, Long partyBranchId) {

        return partyMassessEnrollMapper.pushcrowdAnalysis(partyCommitteeId, partyBranchId);
    }
}
