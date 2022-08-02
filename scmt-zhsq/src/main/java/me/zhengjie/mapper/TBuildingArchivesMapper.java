package me.zhengjie.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.entity.TBuildingArchives;
import me.zhengjie.entity.TZhsqMerchantProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dengjie
 * @since 2020-07-21
 */
@Mapper
public interface TBuildingArchivesMapper extends BaseMapper<TBuildingArchives> {


    /**
     * 最大的层数和楼层中最大的房间数
     * @param id
     * @return
     */
    List<Map<String,Object>> getMaxOfFloorAndDoorAndUnit(@Param("id")String id);

    /**
     * 通过I的查询建筑标绘的位置
     * @param id
     * @return
     */
    List<Map<String,Object>> getOtherPolygonData(@Param("id")String id);

    /**
     *
     * 使用MP提供的Wrapper条件构造器，
     *
     * @param userWrapper
     * @return
     */
    List<TBuildingArchives> selectByMyWrapper(@org.apache.ibatis.annotations.Param(Constants.WRAPPER) Wrapper<TBuildingArchives> userWrapper);

    /**
     * 分页查询
     * 使用MP提供的Wrapper条件构造器，
     *
     * @param userWrapper
     * @return
     */
    Page<TBuildingArchives> selectByPageWrapper(@org.apache.ibatis.annotations.Param(Constants.WRAPPER) Wrapper<TBuildingArchives> userWrapper, Page<TBuildingArchives> page);

}
