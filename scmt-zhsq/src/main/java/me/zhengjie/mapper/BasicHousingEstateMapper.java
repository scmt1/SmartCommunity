package me.zhengjie.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import me.zhengjie.entity.BasicHousingEstate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.zhengjie.entity.BasicPerson;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 小区管理信息 Mapper 接口
 * </p>
 *
 * @author dengjie
 * @since 2020-07-20
 */
public interface BasicHousingEstateMapper extends BaseMapper<BasicHousingEstate> {

    /**
     * 根据街道名查询街道id
     * @param street
     * @return
     */
    String getStreetId(@Param(value = "street") String street);

    /**
     * 根据社区名查询社区id
     * @param community
     * @return
     */
    String getCommunityId(@Param(value = "community")String community);

    /**
     * 根据网格名查询网格id
     * @param grid
     * @return
     */
    String getGridId(@Param(value = "grid")String grid);

    /**
     *
     * 使用MP提供的Wrapper条件构造器，
     *
     * @param userWrapper
     * @return
     */
    List<BasicHousingEstate> selectByMyWrapper(@org.apache.ibatis.annotations.Param(Constants.WRAPPER) Wrapper<BasicHousingEstate> userWrapper);
}
