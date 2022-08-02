package me.zhengjie.service.park;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.common.vo.Result;
import me.zhengjie.entity.park.*;
import me.zhengjie.system.service.dto.UserDto;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dengjie
 * @since 2020-10-26
 */
public interface IParkingService extends IService<Parking> {

    /**
     * 新增停车场
     * 
     * @param parkingRequest
     * @return
     */
    Result<Object> add(ParkingRequest parkingRequest);

    /**
     * 修改停车场
     * 
     * @param id
     * @param parkingRequest
     * @return
     */
    Result<Object>update(Long id, ParkingRequest parkingRequest);

    /**
     * 根据名称搜索停车场
     * 
     * @param parkingName
     * @return
     */
    Result<IPage<SearchParking>> searchParking(String parkingName, Double latitude, Double longitude, Integer current,
                                          Integer size);

    /**
    *  查询停车场
    * 
    * @return
    */
    Result<List<ParkingVo>> loadAll(Map<String, Object> map);

    /**
     * 搜索停车场
     * 
     * @param gridId
     * @param keywords
     * @param current
     * @param size
     * @return
     */
    Result<IPage<ParkingList>> getParkingList(String gridId, String streetId, String communityId, String keywords,
                                         Integer current, Integer size);

    /**
     * 获取停车场详情
     * 
     * @param parkingId
     * @return
     */
    ParkingDetail getParkingDetailByParkingId(Long parkingId);

    Result<Object>delete(Long id);

    List<CommunityParking> getParkingByGridId(String gridId);

    /**
     * 获取车辆进出记录统计
     * @param gridId
     * @return
     */
    Object getCarStatistics(String gridId);
}
