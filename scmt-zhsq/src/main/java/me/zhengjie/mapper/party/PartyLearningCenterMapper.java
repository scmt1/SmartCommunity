package me.zhengjie.mapper.party;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import me.zhengjie.dto.PartyLearningCenterDto;
import me.zhengjie.entity.party.PartyLearningCenter;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-13
 */
@Repository
@Mapper
public interface PartyLearningCenterMapper extends BaseMapper<PartyLearningCenter> {

    /**
     * 学习中心列表 
     * 
     * @param page
     * @param userId
     * @return
     */
    List<PartyLearningCenterDto> getLearnCenterList(IPage<PartyLearningCenterDto> page, @Param("userId") Long userId);

}
