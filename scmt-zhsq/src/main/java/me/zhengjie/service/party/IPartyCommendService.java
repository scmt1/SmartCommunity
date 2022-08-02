package me.zhengjie.service.party;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.entity.party.PartyCommend;
import me.zhengjie.req.PartyCommendReq;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-13
 */
public interface IPartyCommendService extends IService<PartyCommend> {

    /**
     * 党员表彰列表
     * @param partyMemberId
     * @param current
     * @param size
     * @return
     */
    IPage<PartyCommend> getList(Long partyMemberId, String startDate, String endDate, Integer current, Integer size);

    /**
     * 新增党员表彰列表
     * 
     * @param userId
     * @param partyCommendReq
     */
    void add(Long userId, PartyCommendReq partyCommendReq);

    /**
     * 修改党员表彰列表
     * 
     * @param id
     * @param userId
     * @param partyCommendReq
     */
    void edit(Long id, Long userId, PartyCommendReq partyCommendReq);
}
