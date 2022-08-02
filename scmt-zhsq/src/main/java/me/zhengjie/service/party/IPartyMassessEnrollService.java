package me.zhengjie.service.party;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.dto.StatisticalAnalysisDto;
import me.zhengjie.dto.UserInfomation;
import me.zhengjie.entity.party.PartyMassessEnroll;

/**
 * <p>
 * 党群活动报名 服务类
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-13
 */
public interface IPartyMassessEnrollService extends IService<PartyMassessEnroll> {

    /**
     * 党群活动报名
     * 
     * @param id
     * @param userInfomation
     */
    void enroll(Long id, UserInfomation userInfomation);

    /**
     * 党群报名列表
     * 
     * @param partymassesId
     * @param current
     * @param size
     * @return
     */
    IPage<PartyMassessEnroll> getByPartymassesId(Long partymassesId, Integer current, Integer size);

    /**
     * 
     * 活动推送人群
     * @param partyCommitteeId
     * @param partyBranchId
     * @return
     */
    List<StatisticalAnalysisDto> pushcrowdAnalysis(Long partyCommitteeId, Long partyBranchId);

}
