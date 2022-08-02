package me.zhengjie.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import me.zhengjie.entity.BasicGridPersonPoint;
import me.zhengjie.entity.TZhsqCommunityCadres;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author dengjie
 * @since 2020-08-06
 */
public interface BasicGridPersonPointMapper extends BaseMapper<BasicGridPersonPoint> {

    /**
     * 查询当天网格员坐标数据
     * @param gridPersonId
     * @param gridPersonName
     * @param date
     * @return
     */
    List<BasicGridPersonPoint> getToDayGridPersonPoint(@Param("gridPersonId")String gridPersonId,@Param("gridPersonName") String gridPersonName, @Param("date")String date);

    /**
     * 查询网格人员最新的坐标数据
     * @param gridPersonId
     * @return
     */
    BasicGridPersonPoint getCurrentGridPersonPoint(@Param("gridPersonId")String gridPersonId);

    /**
     * 查询网格人员轨迹
     * @param gridPersonId
     * @param startTime
     * @param endTime
     * @return
     */
    List<BasicGridPersonPoint> gridPersonTrackQuery(@Param("gridPersonId")String gridPersonId, @Param("startTime")String startTime, @Param("endTime")String endTime);

}
