package me.zhengjie.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import me.zhengjie.entity.BasicSocialOrganization;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.zhengjie.entity.TBuildingArchives;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dengjie
 * @since 2020-07-22
 */
public interface BasicSocialOrganizationMapper extends BaseMapper<BasicSocialOrganization> {

    /**
     * 社会组织按社区统计
     * @return
     */
    List<Map<String, Object>> getCommunityCountData();

    List<Map<String, Object>> getCommunityCountData(@Param("basicSocialOrganization") BasicSocialOrganization basicSocialOrganization);

    /**
     * 社会组织按网格统计
     * @return
     */
    List<Map<String, Object>> getGridsCountData();

    List<Map<String, Object>> getGridsCountData(@Param("basicSocialOrganization") BasicSocialOrganization basicSocialOrganization);

    /**
     *
     * 使用MP提供的Wrapper条件构造器，
     *
     * @param userWrapper
     * @return
     */
    List<BasicSocialOrganization> selectByMyWrapper(@org.apache.ibatis.annotations.Param(Constants.WRAPPER) Wrapper<BasicSocialOrganization> userWrapper);
}
