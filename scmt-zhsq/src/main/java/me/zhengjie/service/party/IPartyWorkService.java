package me.zhengjie.service.party;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.dto.PartyWorkListDto;
import me.zhengjie.entity.party.PartyWork;
import me.zhengjie.req.PartyWorkReq;

/**
 * <p>
 * 党务公开 服务类
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-12
 */

public interface IPartyWorkService extends IService<PartyWork> {

    /**
     * 党务列表
     *
     * @param title
     * @param partBranchId
     * @param partyCommitteeId
     * @param partyCategoryId
     * @param current
     * @param size
     * @return
     */
    IPage<PartyWork> getList(String title, Long partBranchId, Long partyCommitteeId, Integer partyCategoryId,
                             Integer current, Integer size);

    /**
     * 党务列表
     * @param current
     * @param size
     * @return
     */
    IPage<PartyWorkListDto> getList(Long partyCommitteeId, Long branchId, Integer partyCategoryId, Integer current,
                                    Integer size);

    /**
     * app端查询党务列表
     * @param current
     * @param size
     * @return
     */
    IPage<PartyWork> getPartyList(Integer current, Integer size);

    /**
     * 新增党务信息
     * @param userId
     * @param partyWorkReq
     */
    void add(Long userId, PartyWorkReq partyWorkReq);

    /**
     * 修改党务信息
     *
     * @param id
     * @param userId
     * @param partyWorkReq
     */
    void edit(Long id, Long userId, PartyWorkReq partyWorkReq);
}
