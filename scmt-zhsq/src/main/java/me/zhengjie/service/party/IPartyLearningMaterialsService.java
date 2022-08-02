package me.zhengjie.service.party;

import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.dto.LearningMaterialsDto;
import me.zhengjie.entity.party.PartyLearningMaterials;
import me.zhengjie.enums.PartyLearningMaterialsSourceEnum;
import me.zhengjie.req.PartyLearningMaterialsSeq;

/**
 * <p>
 * 党建学习资料（模范宣塑,两学一做) 服务类
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-13
 */
public interface IPartyLearningMaterialsService extends IService<PartyLearningMaterials> {

    /**
     * 新增加 党建学习资料（模范宣塑,两学一做) 
     * 
     * @param userId
     * @param partyLearningMaterialsSeq
     */
    void add(Long userId, PartyLearningMaterialsSeq partyLearningMaterialsSeq);

    /**
     * 获取最新的两学一做数据
     * 
     * @return
     */
    LearningMaterialsDto learningLxyzInfo();

    /**
     * 获取最新的模范宣塑数据
     * 
     * @return
     */
    LearningMaterialsDto learningMfxsInfo();

    LearningMaterialsDto getPartyLearningMaterials(PartyLearningMaterialsSourceEnum source);

}
