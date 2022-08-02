package me.zhengjie.service.party;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.dto.ReportPartyOrganization;
import me.zhengjie.entity.party.PartyCommittee;
import me.zhengjie.req.PartyCommitteeReq;

/**
 * <p>
 * 党委 服务类
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-12
 */
public interface IPartyCommitteeService extends IService<PartyCommittee> {

    /**
     * 新增党委 
     * 
     * @param userId
     * @param partyCommitteeReq
     */
    void add(Long userId, PartyCommitteeReq partyCommitteeReq);

    /**
     * 修改党委
     * 
     * @param id
     * @param userId
     * @param partyCommitteeReq
     */
    void edit(Long id, Long userId, PartyCommitteeReq partyCommitteeReq);

    /**
     * 根据id获取党委信息
     * 
     * @param id
     * @return
     */
    PartyCommittee getPartyCommitteeById(Long id);

    /**
     * 删除党委
     * 
     * @param id
     */
    void deleteById(Long id);

    /**
    * 获取党委列表
    * 
    * @param name
    * @param communityId
    * @param streetId
    * @param secretary
    * @param phoneNumber
    * @param current
    * @param size
    * @return
    */
    IPage<PartyCommittee> getList(String name, Long communityId, Long streetId, String secretary, String phoneNumber,
                                  Integer current, Integer size);

    List<ReportPartyOrganization> reportPartyOrganization();

    List<Long> getCommitteeIdsByCommunityId(Long communityId);

}
