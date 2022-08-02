package me.zhengjie.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.entity.BasicPerson;
import me.zhengjie.entity.TBuildingArchives;
import org.apache.ibatis.annotations.*;
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
@Mapper
public interface BasicPersonMapper extends BaseMapper<BasicPerson> {
    /**
     * 人口分布统计
     * @return
     */

    List<Map<String,Object>> getBasicPersonCount();

    /**
     * 人口分布统计
     * @param basicPerson 用户数据权限
     * @return
     */
    List<Map<String,Object>> getBasicPersonCount(@Param("basicPerson") BasicPerson basicPerson);

    /**
     * 特殊人群统计
     */
    List<Map<String,Object>> getSpecialPersonCount();

    /**
     * 特殊人群统计
     * @param basicPerson 用户数据权限
     * @return
     */
    List<Map<String,Object>> getSpecialPersonCount(@Param("basicPerson") BasicPerson basicPerson);


    /**
     * 根据房屋id查找人口
     * @param houseId
     * @return
     */
    List<BasicPerson> getPersonByHouseId(@Param("houseId") String houseId);

    /**
     * 根据小区id，获取楼栋
     * @param estateId
     * @return
     */
    List<TBuildingArchives> getBuildArchiveByEstateId(String estateId);

    /**
     * 统计各网格人口数据
     * @return
     */
    List<Map> statisticsGridPerson();

    /**
     * 统计人员坐标点
     * @return
     */
    List<Map> statisticsPersonLocation();

    /**
     * 统计各人口类型数据
     * @return
     */
    List<Map<String,Object>> statisticsPersonType(@Param("buildingArchiveId") String buildingArchiveId);

    /**
     *
     * @param cardId
     * @return
     */
    List<Map<String,Object>> getPersonByCardId(@Param("cardId") String cardId);


    List<Map<String,Object>> getCardId();

    /**
     * 统计党员
     * @return
     */
    List<Map> statisticsPersonPoliticaloutlook(@Param("community") String community);

    /**
     * 统计网格员
     * @return
     */
    List<Map> statisticsPersongridmember(@Param("community") String community);

    /**
     * 统计干部
     * @return
     */
    List<Map> statisticsPersoncommunity(@Param("community") String community);

    /**
     * 统计志愿者
     * @return
     */
    List<Map> statisticsPersonvolunteer(@Param("community") String community);

    /**
     * 统计人口类型
     * @return
     */
    List<Map> statisticspersontype(@Param("community") String community);

    /**
     * 统计事件
     * @return
     */
    List<Map> statisticspersonteventlist(@Param("community") String community);


    /**
     * 统计该社区下男女人口比例
     * @param communityId
     * @return
     */
    Map<String, Object> getPersonDataMaleToFemaleratio(@Param("communityId") String communityId);

    /**
     * 根据街道名查询街道id
     * @param streetName
     * @return
     */
    String getStreetId(@Param("streetName") String streetName);

    /**
     *  根据社区名查询社区id
     * @param communityName
     * @return
     */
    String getCommunityId(@Param("communityName") String communityName);

    /**
     *   根据网格名查询网格id
     * @param ownedGrid
     * @return
     */
    String getOwnedGridId(@Param("ownedGrid") String ownedGrid);


    /**
     * 性别统计
     * @param communityId
     * @param gridId
     * @return
     */
    Map<String, Object> getPersonDataMaleToFemaleratio(@Param("communityId") String communityId, @Param("gridId") String gridId);


    /**
     * 统计人口类型数量
     * @param communityId
     * @param gridId
     * @return
     */
    List<Map<String, Object>> getBasicPersonCountByPersonType(@Param("communityId") String communityId, @Param("gridId") String gridId);

    /**
     * 统计人员类型（老人、精神病人....）
     * @param communityId
     * @param gridId
     * @return
     */
    List<Map<String, Object>> getBasicPersonCountByPopulation(@Param("communityId") String communityId, @Param("gridId") String gridId);

    /**
     * 人群标签统计
     * @param communityId
     * @param gridId
     * @return
     */
    List<Map<String, Object>> getTableTypeCount(@Param("communityId") String communityId, @Param("gridId") String gridId);

    /**
     *
     * 使用MP提供的Wrapper条件构造器，
     *
     * @param userWrapper
     * @return
     */
    List<BasicPerson> selectByMyWrapper(@org.apache.ibatis.annotations.Param(Constants.WRAPPER) Wrapper<BasicPerson> userWrapper);

    /**
     *
     * 使用MP提供的Wrapper条件构造器，
     *
     * @param userWrapper
     * @return
     */
    List<Map<String, Object>> selectMapByMyWrapper(@org.apache.ibatis.annotations.Param(Constants.WRAPPER) Wrapper<BasicPerson> userWrapper);


    /**
     *
     * 使用MP提供的Wrapper条件构造器，
     *
     * @param basicPerson
     * @return
     */
    IPage<Map<String, Object>> selectByMyWrapperByPage(@Param("basicPerson")BasicPerson basicPerson,Page page);

    /**
     *
     * 使用MP提供的Wrapper条件构造器，
     * 通过wapper 联表查询人和房信息
     *
     * @param basicPerson
     * @return
     */
    List<Map<String, Object>> selectInfoByMyWrapper(@Param("basicPerson")BasicPerson basicPerson);

    /**
     *
     * 使用MP提供的Wrapper条件构造器，
     * 通过wapper 联表分页查询人和房信息
     *
     * @param userWrapper
     * @param page
     * @return
     */
    IPage<Map<String, Object>> selectInfoByMyWrapperWithPage(@Param("basicPerson")BasicPerson basicPerson, Page page);


    /**
     * 通过人口id查询绑定的第一套房屋信息-
     * @param id
     * @return
     */
    Map<String, Object> getOneHouseByPersonId(@Param("id") String id);

    /**
     * 查询所有人口的位置
     * @return
     */
    List<BasicPerson> getAllPersonPosition(@Param("basicPerson")BasicPerson basicPerson);

}
