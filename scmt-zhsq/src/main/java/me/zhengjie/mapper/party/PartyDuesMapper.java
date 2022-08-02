package me.zhengjie.mapper.party;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import me.zhengjie.dto.MyDuesDto;
import me.zhengjie.dto.StatisticalAnalysisDto;
import me.zhengjie.entity.party.PartyDues;

/**
 * <p>
 * 党费 Mapper 接口
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-13
 */
@Repository
@Mapper
public interface PartyDuesMapper extends BaseMapper<PartyDues> {

    /**
     * 党员缴费分析
     * 
     * @param partyCommitteeId
     * @param partyBranchId
     * @param time
     * @return
     */
    List<StatisticalAnalysisDto> partyPaymentAnalysis(@Param("partyCommitteeId") Long partyCommitteeId,
                                                      @Param("partyBranchId") Long partyBranchId,
                                                      @Param("time") String time);

    @Select("select pay_date as payDate,amount as amount from party_dues where party_member_id=#{partyMemberId} and is_deleted=0 order by payDate Desc")
    List<MyDuesDto> getMyMyDues(IPage<MyDuesDto> page, Long partyMemberId);

}
