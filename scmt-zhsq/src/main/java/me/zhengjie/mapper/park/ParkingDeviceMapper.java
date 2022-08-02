package me.zhengjie.mapper.park;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import me.zhengjie.entity.park.ParkDeviceList;
import me.zhengjie.entity.park.ParkingDevice;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dengjie
 * @since 2020-10-27
 */
public interface ParkingDeviceMapper extends BaseMapper<ParkingDevice> {

    IPage<ParkDeviceList> getgetParkingDeviceList(IPage<ParkDeviceList> page,
                                                  @Param("gridId") String gridId,
                                                  @Param("keywords") String keywords);

    IPage<ParkDeviceList> getgetParkingDeviceList(IPage<ParkDeviceList> page,
                                                  @Param("streetId") String streetId,
                                                  @Param("communityId") String communityId,
                                                  @Param("gridId") String gridId,
                                                  @Param("keywords") String keywords);

}
