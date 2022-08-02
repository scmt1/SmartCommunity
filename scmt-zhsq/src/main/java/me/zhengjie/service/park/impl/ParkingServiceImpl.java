package me.zhengjie.service.park.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.Result;
import me.zhengjie.mapper.park.ParkingMapper;
import me.zhengjie.entity.park.*;
import me.zhengjie.service.park.IParkingDeviceService;
import me.zhengjie.service.park.IParkingService;
import lombok.AllArgsConstructor;
import me.zhengjie.system.service.dto.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dengjie
 * @since 2020-10-26
 */
@AllArgsConstructor
@Service
public class ParkingServiceImpl extends ServiceImpl<ParkingMapper, Parking> implements IParkingService {

    private final ParkingMapper parkingMapper;

    private final IParkingDeviceService parkingDeviceService;

    @Override
    public Result<Object>add(ParkingRequest parkingRequest) {
        Parking parking = new Parking();
        parking.setAddress(parkingRequest.getAddress());
        parking.setGridId(parkingRequest.getGridId());
        parking.setGridName(parkingRequest.getGridName());
        List<Double> regions = parkingRequest.getRegion();

        if (!CollectionUtils.isEmpty(regions)) {
            parking.setLatitude(regions.get(1));
            parking.setLongitude(regions.get(0));
        }
        parking.setRegion(JSON.toJSONString(regions));
        parking.setParkingName(parkingRequest.getParkingName());
        parking.setCreateTime(LocalDateTime.now());
        parking.setUpdateTime(LocalDateTime.now());
        parking.setStreetId(parkingRequest.getStreetId());
        parking.setStreetName(parkingRequest.getStreetName());
        parking.setCommunityId(parkingRequest.getCommunityId());
        parking.setCommunityName(parkingRequest.getCommunityName());
        parking.setRemark(parkingRequest.getRemark());
        save(parking);
        return ResultUtil.success("成功");
    }

    @Override
    public Result<Object>update(Long id, ParkingRequest parkingRequest) {
        Parking parking = getParking(id);
        parking.setAddress(parkingRequest.getAddress());
        parking.setGridId(parkingRequest.getGridId());
        parking.setGridName(parkingRequest.getGridName());
        List<Double> regions = parkingRequest.getRegion();
        if (!CollectionUtils.isEmpty(regions)) {
            parking.setLatitude(regions.get(1));
            parking.setLongitude(regions.get(0));
        }
        parking.setRegion(JSON.toJSONString(regions));
        parking.setParkingName(parkingRequest.getParkingName());
        parking.setUpdateTime(LocalDateTime.now());
        parking.setStreetId(parkingRequest.getStreetId());
        parking.setStreetName(parkingRequest.getStreetName());
        parking.setCommunityId(parkingRequest.getCommunityId());
        parking.setCommunityName(parkingRequest.getCommunityName());
        parking.setRemark(parkingRequest.getRemark());
        updateById(parking);
        return ResultUtil.success("成功");
    }

    public Parking getParking(Long id) {
        return getById(id);
    }

    @Override
    public Result<IPage<SearchParking>> searchParking(String parkingName, Double latitude, Double longitude, Integer current,
                                                 Integer size) {
        //        QueryWrapper<Parking> queryWrapper = new QueryWrapper<>();
        //        queryWrapper.eq("is_deleted", 0);
        //        queryWrapper.like("parking_name", parkingName).or().like("address", parkingName);
        IPage<SearchParking> page = new Page<>(current, size);

        IPage<SearchParking> pageResult = parkingMapper.getByParkingName(parkingName, latitude, longitude, page);
        List<SearchParking> searchParkings = new ArrayList<>();
        List<SearchParking> parkings = pageResult.getRecords();
        if (!CollectionUtils.isEmpty(parkings)) {
            for (SearchParking searchParking : parkings) {
                String longitudeAndLatitude = searchParking.getLongitudeAndLatitude();

                if (!StringUtils.isEmpty(longitudeAndLatitude)) {
                    List<Double> region = JSONArray.parseArray(longitudeAndLatitude, Double.class);
                    searchParking.setRegion(region);
                }

                searchParking.setNumberOfParkingSpace(0);
                searchParking.setNumberOfFreeParkingSpace(0);
                List<ParkingDevice> parkingDevices = parkingDeviceService
                        .getParkingDeviceByParkingId(searchParking.getParkingId());
                if (!CollectionUtils.isEmpty(parkingDevices)) {
                    searchParking.setNumberOfParkingSpace(parkingDevices.size());
                    List<ParkingDevice> freeList = parkingDevices.stream()
                            .filter(s -> s.getParkingStatus() != null && s.getParkingStatus().equals("2"))
                            .collect(Collectors.toList());
                    searchParking.setNumberOfFreeParkingSpace(CollectionUtils.isEmpty(freeList) ? 0 : freeList.size());
                }
                searchParkings.add(searchParking);
            }
        }
        page.setRecords(searchParkings);
        return ResultUtil.data(page);
    }

    @Override
    public Result<List<ParkingVo>> loadAll(Map<String,Object> map) {
        QueryWrapper<Parking> queryWrapper = new QueryWrapper<>();
        if (map != null){
            if (map.containsKey("street_id")){
                if (!StringUtils.isEmpty(map.get("street_id"))){
                    queryWrapper.eq("street_id", map.get("street_id"));
                }
            }
            if (map.containsKey("community_id")){
                if (!StringUtils.isEmpty(map.get("community_id"))){
                    queryWrapper.eq("community_id", map.get("community_id"));
                }
            }
            if (map.containsKey("grid_id")){
                if (!StringUtils.isEmpty(map.get("grid_id"))){
                    queryWrapper.eq("grid_id", map.get("grid_id"));
                }
            }
        }
        queryWrapper.eq("is_deleted", 0);
        List<Parking> parkings = parkingMapper.selectList(queryWrapper);
        List<ParkingVo> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(parkings)) {
            for (Parking parking : parkings) {
                ParkingVo parkingVo = new ParkingVo();
                parkingVo.setParkingId(parking.getId());
                parkingVo.setParkingName(parking.getParkingName());
                list.add(parkingVo);
            }
        }
        return ResultUtil.data(list);
    }

    @Override
    public Result<IPage<ParkingList>> getParkingList(String gridId, String streetId, String communityId, String keywords,
                                                Integer current, Integer size) {
        IPage<Parking> page = new Page<>(current, size);
        QueryWrapper<Parking> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(gridId)) {
            queryWrapper.eq("grid_id", gridId);
        }
        if (!StringUtils.isEmpty(streetId)) {
            queryWrapper.eq("street_id", streetId);
        }
        if (!StringUtils.isEmpty(communityId)) {
            queryWrapper.eq("community_id", communityId);
        }
        if (!StringUtils.isEmpty(keywords)) {
            queryWrapper.like("parking_name", keywords).or().like("address", keywords);
        }
        queryWrapper.eq("is_deleted", 0);
        queryWrapper.orderByDesc("create_time");
        IPage<Parking> pageParking = parkingMapper.selectPage(page, queryWrapper);
        IPage<ParkingList> pageResult = new Page<>();
        BeanUtils.copyProperties(pageParking, pageResult);
        List<Parking> parkings = pageParking.getRecords();

        List<ParkingList> records = new ArrayList<>();
        if (!CollectionUtils.isEmpty(parkings)) {
            for (Parking parking : parkings) {
                String region = parking.getRegion();
                ParkingList parkingList = new ParkingList();
                BeanUtils.copyProperties(parking, parkingList);
                if (!StringUtils.isEmpty(region)) {
                    List<Double> regions = JSONArray.parseArray(region, Double.class);
                    parkingList.setRegion(regions);
                }
                records.add(parkingList);
            }
        }
        pageResult.setRecords(records);
        return ResultUtil.data(pageResult);
    }

    @Override
    public ParkingDetail getParkingDetailByParkingId(Long parkingId) {
        Parking parking = getParking(parkingId);
        if (parking == null) {
            return null;
        }
        ParkingDetail parkingDetail = new ParkingDetail();
        parkingDetail.setAddress(parking.getAddress());
        List<ParkingDevice> parkingDevices = parkingDeviceService.getParkingDeviceByParkingId(parking.getId());
        parkingDetail.setNumberOfFreeParkingSpace(0);
        parkingDetail.setNumberOfParkingSpace(0);
        parkingDetail.setDeviceList(null);
        String region = parking.getRegion();
        if (!StringUtils.isEmpty(region)) {
            List<Double> regions = JSONArray.parseArray(region, Double.class);
            parkingDetail.setRegion(regions);

        }
        if (!CollectionUtils.isEmpty(parkingDevices)) {
            parkingDetail.setNumberOfParkingSpace(parkingDevices.size());
            List<ParkingDevice> freeList = parkingDevices.stream()
                    .filter(s -> s.getParkingStatus() != null && s.getParkingStatus().equals(2))
                    .collect(Collectors.toList());
            parkingDetail.setNumberOfFreeParkingSpace(CollectionUtils.isEmpty(freeList) ? 0 : freeList.size());
            parkingDetail.setDeviceList(getDeviceList(parkingDevices));
        }
        parkingDetail.setLatitude(parking.getLatitude());
        parkingDetail.setLongitude(parking.getLongitude());
        parkingDetail.setParkingId(parkingId);
        parkingDetail.setParkingName(parking.getParkingName());
        return parkingDetail;
    }

    private List<DeviceDetailVo> getDeviceList(List<ParkingDevice> parkingDevices) {
        List<DeviceDetailVo> list = new ArrayList<>();
        if (CollectionUtils.isEmpty(parkingDevices)) {
            return list;
        }
        for (ParkingDevice parkingDevice : parkingDevices) {
            DeviceDetailVo deviceDetailVo = new DeviceDetailVo();
            deviceDetailVo.setDeviceId(parkingDevice.getDeviceId());
            deviceDetailVo.setDeviceName(parkingDevice.getDeviceName());
            deviceDetailVo.setParkingStatus(parkingDevice.getParkingStatus());
            deviceDetailVo.setLatitude(parkingDevice.getLatitude());
            deviceDetailVo.setLongitude(parkingDevice.getLongitude());
            deviceDetailVo.setParkingStatus(parkingDevice.getParkingStatus());
            deviceDetailVo.setRemark(parkingDevice.getRemark());
            deviceDetailVo.setIsEnabled(parkingDevice.getIsEnabled());
            list.add(deviceDetailVo);
        }
        return list;
    }

    @Override
    public Result<Object>delete(Long id) {
        Parking parking = getById(id);
        if (parking != null) {
            parking.setIsDeleted(true);
            updateById(parking);
        }
        return ResultUtil.success("成功");
    }

    @Override
    public List<CommunityParking> getParkingByGridId(String gridId) {
        List<CommunityParking> communityParkings = parkingMapper.getCommunityParking(gridId);

        return communityParkings;
    }

    @Override
    public Object getCarStatistics(String gridId) {
        String propertyUuids = "";
        if (gridId !=null && !StringUtils.isEmpty(gridId)) {
            propertyUuids = parkingMapper.getPropertyUuids(gridId);
        }
//        JSONObject carStatistics = communityClient.getCarStatistics(propertyUuids);
        JSONObject carStatistics = null;
        return  carStatistics;
//        return carStatistics.getJSONObject("object");
    }
}
