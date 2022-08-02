package me.zhengjie.mapper.party;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.zhengjie.entity.party.PartyMessage;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-14
 */
@Repository
@Mapper
public interface PartyMessageMapper extends BaseMapper<PartyMessage> {

    //    /**
    //     * 查询我的消息
    //     * 
    //     * @param page
    //     * @param partyCommitteeId 党委Id
    //     * @param partyBranchId 支部Id
    //     * @return
    //     */
    //    List<MyMessageDto> getMyMessageList(IPage<MyMessageDto> page, @Param("partyCommitteeId") Long partyCommitteeId,
    //                                        @Param("partyBranchId") Long partyBranchId);

}
