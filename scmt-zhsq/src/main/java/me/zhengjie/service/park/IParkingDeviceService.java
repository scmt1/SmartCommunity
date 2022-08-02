package me.zhengjie.service.park;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.common.vo.Result;
import me.zhengjie.entity.park.ParkDeviceList;
import me.zhengjie.entity.park.ParkDeviceRequest;
import me.zhengjie.entity.park.ParkingDevice;
import me.zhengjie.entity.park.UpdateParkDeviceRequest;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dengjie
 * @since 2020-10-27
 */
public interface IParkingDeviceService extends IService<ParkingDevice> {

    Result<Object>addDevice(ParkDeviceRequest parkDevice);

    Result<Object>updateParkDevice(Long i, UpdateParkDeviceRequest parkDevice);

    //    ParkingDevice getParkingDevice(Long id);

    IPage<ParkDeviceList> getParkingDeviceList(String streetId, String communityId, String gridId, String keywords, Integer current, Integer size);

    Result<Object>add(UpdateParkDeviceRequest parkDevice);

    List<ParkingDevice> getParkingDeviceByParkingId(Long id);

    Result<Object>deleteById(Long id);

}
