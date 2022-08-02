package me.zhengjie.service.party;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.dto.StatisticalAnalysisDto;
import me.zhengjie.entity.party.PartyLearnCenterEnroll;

/**
 * <p>
 * 用户学习中心学习记录 服务类
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-13
 */
public interface IPartyLearnCenterEnrollService extends IService<PartyLearnCenterEnroll> {

    /**
     * 学习分析
     * @param partyCommitteeId
     * @return
     */
    List<StatisticalAnalysisDto> learnAnalysis(Long partyCommitteeId);

}
