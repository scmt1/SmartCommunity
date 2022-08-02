package me.zhengjie.service.park.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.Result;
import me.zhengjie.mapper.park.ParkingDeviceMapper;
import me.zhengjie.mapper.park.ParkingMapper;
import me.zhengjie.entity.park.*;
import me.zhengjie.service.park.IParkingDeviceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dengjie
 * @since 2020-10-27
 */
@AllArgsConstructor
@Service
public class ParkingDeviceServiceImpl extends ServiceImpl<ParkingDeviceMapper, ParkingDevice>
                                      implements IParkingDeviceService {
    //
    private final ParkingMapper       parkingMapper;

    private final ParkingDeviceMapper parkingDeviceMapper;



    @Override
    public Result<Object>addDevice(ParkDeviceRequest parkDevice) {
        String cmd = parkDevice.getCmd();
        ParkDeviceBody deviceBody = parkDevice.getBody();

        if (cmd.equalsIgnoreCase("sendRegister")) {
            return this.sendRegister(deviceBody);
        } else if (cmd.equalsIgnoreCase("sendDeviceHeartbeat")) {
            return this.sendDeviceHeartbeat(deviceBody);
        } else if (cmd.equalsIgnoreCase("sendParkStatu")) {
            return this.sendParkStatu(deviceBody);
        }
        return ResultUtil.error("指令编码错误");

    }

    /**
     * 
     * 
     * @param deviceBody
     * @return
     */
    private Result<Object>sendParkStatu(ParkDeviceBody deviceBody) {
        String parkId = deviceBody.getParkID();
        String deviceId = deviceBody.getDeviceID();
        ParkingDevice parkDevice = getParkDevice(parkId, deviceId);
        if (parkDevice == null) {
            return ResultUtil.error("失败,设备不存在");
        }
        parkDevice.setRssi(deviceBody.getRssi());
        parkDevice.setPassTime(deviceBody.getPassTime());
        parkDevice.setSequence(deviceBody.getSequence());
        parkDevice.setBattary(deviceBody.getBattary());
        parkDevice.setParkingStatus(deviceBody.getParkingStatu());
        updateById(parkDevice);
        return ResultUtil.success("成功!");
    }

    /**
     * @param deviceBody
     */
    private Result<Object>sendDeviceHeartbeat(ParkDeviceBody deviceBody) {
        String parkId = deviceBody.getParkID();
        String deviceId = deviceBody.getDeviceID();
        ParkingDevice parkDevice = getParkDevice(parkId, deviceId);
        if (parkDevice == null) {
            return ResultUtil.error("失败,设备不存在");
        }
        parkDevice.setTime(deviceBody.getTime());
        parkDevice.setBattary(deviceBody.getBattary());
        parkDevice.setErrcode(deviceBody.getErrcode());
        parkDevice.setParkingStatus(deviceBody.getParkingStatu());
        updateById(parkDevice);
        return ResultUtil.success("成功!");
    }

    /**
     * @param deviceBody
     */
    private Result<Object>sendRegister(ParkDeviceBody deviceBody) {
        String parkId = deviceBody.getParkID();
        String deviceId = deviceBody.getDeviceID();
        Parking parking = parkingMapper.selectById(parkId);
        if (parking == null) {
            return ResultUtil.error("parkId error");
        }

        ParkingDevice parkDevice = getParkDevice(parkId, deviceId);
        if (parkDevice != null) {
            return ResultUtil.success("成功!");
        }
        parkDevice = new ParkingDevice();
        parkDevice.setParkingId(parkId);
        parkDevice.setTime(deviceBody.getTime());
        parkDevice.setDeviceId(deviceId);
        parkDevice.setVersion(deviceBody.getVersion());
        save(parkDevice);
        return ResultUtil.success("成功!");
    }

    private ParkingDevice getParkDevice(String parkId, String deviceId) {
        QueryWrapper<ParkingDevice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parking_id", parkId);
        queryWrapper.eq("device_id", deviceId);
        return super.getOne(queryWrapper);
    }

    @Override
    public Result<Object>updateParkDevice(Long id, UpdateParkDeviceRequest parkDevice) {
        ParkingDevice device = getById(id);
        if (device == null) {
            return ResultUtil.error("失败,设备不存在");
        }
        device.setDeviceId(parkDevice.getDeviceId());
        device.setDeviceName(parkDevice.getDeviceName());
        device.setIsEnabled(parkDevice.getIsEnabled());
        device.setParkingId(parkDevice.getParkingId());
        device.setLatitude(parkDevice.getLatitude());
        device.setLongitude(parkDevice.getLongitude());
        device.setRemark(parkDevice.getRemark());
        device.setParkingStatus("2");
        updateById(device);
        return ResultUtil.success("成功!");
    }

    /**
     * 
     * @see me.zhengjie.service.park.IParkingDeviceService(Long)
     */
    //    @Override
    //    public ParkingDevice getParkingDevice(Long id) {
    //        QueryWrapper<ParkingDevice> queryWrapper = new QueryWrapper<>();
    //        queryWrapper.eq("id", id);
    //        queryWrapper.eq("is_enabled", 0);
    //        return parkingDeviceMapper.selectOne(queryWrapper);
    //    }

    @Override
    public IPage<ParkDeviceList> getParkingDeviceList(String streetId, String communityId, String gridId, String keywords, Integer current, Integer size) {
        IPage<ParkDeviceList> page = new Page<>(current, size);
        IPage<ParkDeviceList> pageResult = parkingDeviceMapper.getgetParkingDeviceList(page,streetId,communityId,gridId, keywords);
        return pageResult;
    }

    @Override
    public Result<Object>add(UpdateParkDeviceRequest parkDevice) {
        String parkId = parkDevice.getParkingId();
        String deviceId = parkDevice.getDeviceId();
        ParkingDevice parkingDevice = getParkDevice(parkId, deviceId);
        if (parkingDevice != null) {
            return ResultUtil.error("device id 已经存在");
        }
        parkingDevice = new ParkingDevice();
        parkingDevice.setDeviceId(deviceId);
        parkingDevice.setDeviceName(parkDevice.getDeviceName());
        parkingDevice.setIsEnabled(parkDevice.getIsEnabled());
        parkingDevice.setParkingId(parkDevice.getParkingId());
        parkingDevice.setLatitude(parkDevice.getLatitude());
        parkingDevice.setLongitude(parkDevice.getLongitude());
        parkingDevice.setRemark(parkDevice.getRemark());
        parkingDevice.setParkingStatus("2");
        save(parkingDevice);
        return ResultUtil.success("成功");
    }

    @Override
    public List<ParkingDevice> getParkingDeviceByParkingId(Long parkingId) {
        QueryWrapper<ParkingDevice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parking_id", parkingId);
        queryWrapper.eq("is_enabled", 1);
        queryWrapper.eq("is_deleted", 0);
        return parkingDeviceMapper.selectList(queryWrapper);
    }

    @Override
    public Result<Object>deleteById(Long id) {
        ParkingDevice parkingDevice = getById(id);
        parkingDevice.setIsDeleted(true);
        parkingDeviceMapper.updateById(parkingDevice);
        return ResultUtil.success("成功");
    }

}
