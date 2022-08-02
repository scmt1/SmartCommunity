package me.zhengjie.mapper.party;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.zhengjie.dto.StatisticalAnalysisDto;
import me.zhengjie.entity.party.PartyMassessEnroll;

/**
 * <p>
 * 党群活动报名 Mapper 接口
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-13
 */
@Repository
@Mapper
public interface PartyMassessEnrollMapper extends BaseMapper<PartyMassessEnroll> {

    List<StatisticalAnalysisDto> pushcrowdAnalysis(@Param("partyCommitteeId") Long partyCommitteeId,
                                                   @Param("partyBranchId") Long partyBranchId);

}
