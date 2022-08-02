package me.zhengjie.mapper.party;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import me.zhengjie.dto.AppPartyMassessListDto;
import me.zhengjie.dto.ReportPartyMasses;
import me.zhengjie.entity.party.PartyMasses;

/**
 * <p>
 * 党群  Mapper 接口
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-13
 */
@Repository
@Mapper
public interface PartyMassesMapper extends BaseMapper<PartyMasses> {

    /**
     * 党群列表
     * 
     * @param page
     * @param userId
     * @return
     */
    List<AppPartyMassessListDto> getPartyMassessList(IPage<AppPartyMassessListDto> page, @Param("userId") Long userId,
                                                     @Param("partyCommitteeId") Long partyCommitteeId,
                                                     @Param("partyBranchId") Long partyBranchId,
                                                     @Param("partyCategoryId") Integer partyCategoryId);

    //    List<ReportPartyMasses> getpartyMassesStatistics(@Param("partyCommitteeIds") List<Long> committeeIds);

    List<ReportPartyMasses> getpartyMassesStatistics();
}
