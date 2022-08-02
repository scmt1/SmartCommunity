package me.zhengjie.mapper.party;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import me.zhengjie.dto.PartyWorkListDto;
import me.zhengjie.entity.party.PartyWork;

/**
 * <p>
 * 党务公开 Mapper 接口
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-12
 */
@Repository
@Mapper
public interface PartyWorkMapper extends BaseMapper<PartyWork> {

    /**
     *  查询党务公开列表
     * @param page
     * @return
     */
    List<PartyWorkListDto> getPartyWorkList(@Param("partyCommitteeId") Long partyCommitteeId,
                                            @Param("branchId") Long branchId,
                                            @Param("partyCategoryId") Integer partyCategoryId,
                                            IPage<PartyWorkListDto> page);

    /**
     * app端查询党务列表
     * @param page
     * @return
     */
    List<PartyWork> getPartyList(IPage<PartyWork> page);
}
