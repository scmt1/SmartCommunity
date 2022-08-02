package me.zhengjie.service.party.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.dto.StatisticalAnalysisDto;
import me.zhengjie.entity.party.PartyLearnCenterEnroll;
import me.zhengjie.mapper.party.PartyLearnCenterEnrollMapper;
import me.zhengjie.service.party.IPartyLearnCenterEnrollService;

import lombok.AllArgsConstructor;

/**
 * <p>
 * 用户学习中心学习记录 服务实现类
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-13
 */
@Service
@AllArgsConstructor
public class PartyLearnCenterEnrollServiceImpl extends
                                               ServiceImpl<PartyLearnCenterEnrollMapper, PartyLearnCenterEnroll>
                                               implements IPartyLearnCenterEnrollService {

    private final PartyLearnCenterEnrollMapper partyLearnCenterEnrollMapper;

    @Override
    public List<StatisticalAnalysisDto> learnAnalysis(Long partyCommitteeId) {
        return partyLearnCenterEnrollMapper.learnAnalysis(partyCommitteeId);
    }

}
