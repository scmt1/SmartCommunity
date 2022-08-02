package me.zhengjie.mapper.park;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import me.zhengjie.entity.park.CommunityParking;
import me.zhengjie.entity.park.Parking;
import me.zhengjie.entity.park.SearchParking;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dengjie
 * @since 2020-10-26
 */
public interface ParkingMapper extends BaseMapper<Parking> {

    IPage<SearchParking> getByParkingName(@Param("parkingName") String parkingName, @Param("latitude") Double latitude,
                                          @Param("longitude") Double longitude, IPage<SearchParking> page);

    List<CommunityParking> getCommunityParking(@Param("gridId") String gridId);

    String getPropertyUuids(@Param("gridId") String gridId);
}
