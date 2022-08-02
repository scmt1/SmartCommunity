package me.zhengjie.service.party.impl;

import java.util.Calendar;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.dto.LearningMaterialsDto;
import me.zhengjie.entity.party.PartyLearningMaterials;
import me.zhengjie.enums.PartyLearningMaterialsSourceEnum;
import me.zhengjie.mapper.party.PartyLearningMaterialsMapper;
import me.zhengjie.req.PartyLearningMaterialsSeq;
import me.zhengjie.service.party.IPartyLearningMaterialsService;

import lombok.AllArgsConstructor;

/**
 * <p>
 * 党建学习资料（模范宣塑,两学一做) 服务实现类
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-13
 */
@Service
@AllArgsConstructor
public class PartyLearningMaterialsServiceImpl extends ServiceImpl<PartyLearningMaterialsMapper, PartyLearningMaterials>
                                               implements IPartyLearningMaterialsService {

    private final PartyLearningMaterialsMapper partyLearningMaterialsMapper;

    @Override
    public void add(Long userId, PartyLearningMaterialsSeq partyLearningMaterialsSeq) {
        PartyLearningMaterials entity = getPartyLearningMaterialsByType(partyLearningMaterialsSeq.getSourceType());
        if (entity == null) {
            entity = new PartyLearningMaterials();
            entity.setCreateTime(Calendar.getInstance().getTime());
        }
        entity.setCreateUser(userId);
        entity.setSiteName(partyLearningMaterialsSeq.getSiteName());
        entity.setSiteUrl(partyLearningMaterialsSeq.getSiteUrl());
        entity.setSourceType(partyLearningMaterialsSeq.getSourceType());
        entity.setUpdateTime(entity.getCreateTime());
        entity.setUpdateUser(userId);
        saveOrUpdate(entity);
    }

    public PartyLearningMaterials getPartyLearningMaterialsByType(Integer sourceType) {
        QueryWrapper<PartyLearningMaterials> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("source_type", sourceType);
        return super.getOne(queryWrapper);

    }

    @Override
    public LearningMaterialsDto learningLxyzInfo() {
        return getPartyLearningMaterials(PartyLearningMaterialsSourceEnum.LXYZ);
    }

    @Override
    public LearningMaterialsDto learningMfxsInfo() {
        return getPartyLearningMaterials(PartyLearningMaterialsSourceEnum.MFXS);
    }

    public LearningMaterialsDto getPartyLearningMaterials(PartyLearningMaterialsSourceEnum source) {
        QueryWrapper<PartyLearningMaterials> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        queryWrapper.eq("source_type", source.getCode());
        queryWrapper.last("limit 1");
        PartyLearningMaterials plm = partyLearningMaterialsMapper.selectOne(queryWrapper);
        if (plm == null) {
            return null;
        }
        return LearningMaterialsDto.builder().siteName(plm.getSiteName()).siteUrl(plm.getSiteUrl()).build();

    }

}
