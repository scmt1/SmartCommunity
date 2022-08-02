package me.zhengjie.mapper.party;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.zhengjie.dto.StatisticalAnalysisDto;
import me.zhengjie.entity.party.PartyLearnCenterEnroll;

/**
 * <p>
 * 用户学习中心学习记录 Mapper 接口
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-13
 */
@Repository
@Mapper
public interface PartyLearnCenterEnrollMapper extends BaseMapper<PartyLearnCenterEnroll> {

    /**
     * 学习情况分析
     * @param partyCommitteeId
     * @return
     */
    List<StatisticalAnalysisDto> learnAnalysis(@Param("partyCommitteeId") Long partyCommitteeId);

}
