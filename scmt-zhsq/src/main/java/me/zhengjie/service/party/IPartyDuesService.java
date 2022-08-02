package me.zhengjie.service.party;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.dto.MyDuesDto;
import me.zhengjie.dto.StatisticalAnalysisDto;
import me.zhengjie.entity.party.PartyDues;
import me.zhengjie.req.PartyDuesReq;

/**
 * <p>
 * 党费 服务类
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-13
 */
public interface IPartyDuesService extends IService<PartyDues> {
    /**
     * 党费列表
     * 
     * @param startDate
     * @param enDate
     * @param current
     * @param size
     * @return
     */
    IPage<PartyDues> getList(Long partyMemberId, Date startDate, Date enDate, Integer current, Integer size);

    /**
    * 新增党费
    * 
    * @param userId
    * @param partyDuesReq
    */
    void add(Long userId, PartyDuesReq partyDuesReq);

    /**
     * 修改党费
     * 
     * @param id
     * @param userId
     * @param partyDuesReq
     */
    void edit(Long id, Long userId, PartyDuesReq partyDuesReq);

    /**
     * 根据身份证号码获取党费
     * 
     * @param idCardNo
     * @param current
     * @param size
     * @return
     */
    IPage<MyDuesDto> getDuesDtoByIdCardNo(String idCardNo, Integer current, Integer size);

    /**
     * 党员缴费分析
     * @param partyCommitteeId
     * @param partyBranchId
     * @param time
     * @return
     */
    List<StatisticalAnalysisDto> partyPaymentAnalysis(Long partyCommitteeId, Long partyBranchId, String time);

}
