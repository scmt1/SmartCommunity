package me.zhengjie.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.entity.BasicHousing;
import me.zhengjie.entity.BasicPerson;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author dengjie
 * @since 2020-07-21
 */
public interface BasicHousingMapper extends BaseMapper<BasicHousing> {

    /**
     * 根据楼栋id获取单元
     * @param buildArchiveId
     * @return
     */
    List<Map<String, Object>> getUnitByBuildArchiveId(String buildArchiveId);


    /**
     * 获取层户数
     * @param buildArchiveId
     * @param unit
     * @return
     */
    List<Map<String, Object>> getDoor(String buildArchiveId, String unit);

    /**
     * 最大的层数和楼层中最大的房间数
     *
     * @param buildArchiveId
     * @param unit
     * @return
     */
    List<Map<String, Object>> getMaxOfFloorAndDoor(String buildArchiveId, String unit);

    /**
     * 获取房屋编号
     * @param buildArchiveId
     * @param unit
     * @return
     */
    List<Map<String, Object>> getHouseNum(@Param("buildArchiveId")String buildArchiveId, @Param("unit")String unit);
    /**
     * 获取真实的门牌号（即房屋）
     * @param buildArchiveId
     * @param unit
     * @return
     */
    List<Map<String,Object>> getRealDoor(@Param("buildArchiveId")String buildArchiveId,@Param("unit")String unit);
    /**
     * 获取真实的门牌号（即房屋）
     * @param buildArchiveId
     * @param unit
     * @return
     */
    List<Map<String,Object>> getRealDoorInfo(@Param("buildArchiveId")String buildArchiveId,@Param("unit")String unit);
    /**
     * 获取房屋
     * @param buildArchiveId
     * @param unit
     * @return
     */
    List<Map<String,Object>> getRealHouse(@Param("buildArchiveId")String buildArchiveId,@Param("unit")String unit);
    /**
     * 获取房屋信息
     * @param buildArchiveId
     * @param unit
     * @return
     */
    List<Map<String,Object>> getRealHouseInfo(@Param("buildArchiveId")String buildArchiveId,@Param("unit")String unit);
    /**
     * 获取房屋绑定的人
     * @param buildArchiveId
     * @param unit
     * @return
     */
    List<Map<String,Object>> getRealPersonInfo(@Param("buildArchiveId")String buildArchiveId,@Param("unit")String unit);

    /**
     * 根据中间表查询所有租客(分页)
     * @param basicPerson
     * @param page
     * @return
     */
    IPage<Map<String, Object>> getAllPersonByRela(@Param("basicPerson") BasicPerson basicPerson, Page page);

    /**
     * 根据中间表查询所有租客(不分页)
     * @param basicPerson
     * @return
     */
    List<Map<String, Object>> getAllPersonByRelaRent(@Param("basicPerson") BasicPerson basicPerson);
    /**
     * 根据楼栋id 查找房屋
     *
     * @param buildArchiveId
     * @param personId
     * @return
     */
    List<BasicHousing> getRoomByBuildArchiveId(@Param("buildArchiveId") String buildArchiveId, @Param("personId") String personId, @Param("type") String type);

    /**
     * 统计各房屋类型数据
     *
     * @return
     */
    List<Map<String, Object>> statisticsHousingType(@Param("buildingArchiveId") String buildingArchiveId);

    /**
     * 根据楼栋查询单元
     *
     * @param buildArchiveId
     * @return
     */
    List<String> getUnits(@Param("buildArchiveId") String buildArchiveId);


    /**
     * 根据楼栋单元查询楼层
     *
     * @param buildArchiveId
     * @param unit
     * @return
     */
    List<String> getFloors(@Param(value = "buildArchiveId") String buildArchiveId,@Param(value = "unit") String unit);

    /**
     * 根据楼栋单元查询楼层
     *
     * @param buildArchiveId
     * @param unit
     * @param floor
     * @return
     */
    List<Map<String, Object>> getDoorNumbers(@Param(value = "buildArchiveId")String buildArchiveId,@Param(value = "unit") String unit, @Param(value = "floor")String floor);

    /**
     * 根据personId查询房屋信息
     *
     * @param personId
     * @return
     */
    List<BasicHousing> getHouseByPersonId(@Param(value = "personId")String personId);

    /**
     *
     * 使用MP提供的Wrapper条件构造器，
     *
     * @param userWrapper
     * @return
     */
    List<BasicHousing> selectByMyWrapper(@org.apache.ibatis.annotations.Param(Constants.WRAPPER) Wrapper<BasicHousing> userWrapper);

    /**
     * 查询房屋结构图左侧树菜单
     * @return
     */
    List<Map<String, Object>> selectHouseTree();

    /**
     * 查询房屋结构图左侧树菜单
     * @param powerMap 用户数据权限
     * @return
     */
    List<Map<String, Object>> selectHouseTree(@Param(value = "powerMap")Map<String,Object> powerMap);
}

