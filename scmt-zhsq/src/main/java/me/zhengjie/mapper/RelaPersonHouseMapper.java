package me.zhengjie.mapper;

import me.zhengjie.entity.BasicHousing;
import me.zhengjie.entity.RelaPersonHouse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dengjie
 * @since 2020-07-22
 */
public interface RelaPersonHouseMapper extends BaseMapper<RelaPersonHouse> {

    /**
     * 通过房屋id查询人员
     * @param pid 房屋id
     * @return
     */
    List<BasicHousing> getRelaPersonHouseByPid(@Param("pid")String pid);

    /**
     *通过人员id查询房屋
     * @return
     */
    List<RelaPersonHouse> getRelaByHouseId();

    /**
     *通过房屋id 查询关联关系
     * @param id
     * @return
     */
    RelaPersonHouse getToponeHouseId(@Param("id")String id);
}
