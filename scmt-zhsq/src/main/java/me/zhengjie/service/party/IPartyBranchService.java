package me.zhengjie.service.party;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.entity.party.PartyBranch;
import me.zhengjie.req.PartyBranchReq;

/**
 * <p>
 * 党支部 服务类
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-12
 */
public interface IPartyBranchService extends IService<PartyBranch> {

    /**
     * 新增支部
     * 
     * @param userId
     * @param partyBranchReq
     */
    void add(Long userId, PartyBranchReq partyBranchReq);

    /**
     * 修改支部
     * 
     * @param id
     * @param userId
     * @param partyBranchReq
     */
    void edit(Long id, Long userId, PartyBranchReq partyBranchReq);

    /**
     * 党支部列表
     * 
     * @param name
     * @param jurisdictionId
     * @param partyCommitteeId
     * @param secretary
     * @param phoneNumber
     * @param current
     * @param size
     * @return
     */
    IPage<PartyBranch> getList(String name, Long jurisdictionId, Long partyCommitteeId, String secretary,
                               String phoneNumber, Integer current, Integer size);
}
