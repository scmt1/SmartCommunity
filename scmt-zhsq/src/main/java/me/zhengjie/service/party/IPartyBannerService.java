package me.zhengjie.service.party;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.entity.party.PartyBanner;
import me.zhengjie.req.PartyBannerReq;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-13
 */
public interface IPartyBannerService extends IService<PartyBanner> {
    /**
     * 新增banner
     * @param userId
     * @param partyBannerReq
     */
    void add(Long userId, PartyBannerReq partyBannerReq);

    /**
     * 修改banner
     * @param id
     * @param userId
     * @param partyBannerReq
     */
    void edit(Long id, Long userId, PartyBannerReq partyBannerReq);

    List<PartyBanner> getList(String title);
}
