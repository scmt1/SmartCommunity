/**
 * yammii.com Inc.
 * Copyright (c) 2018-2020 All Rights Reserved.
 */
package me.zhengjie.controller.park;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.api.R;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.utils.SecurityUtil;
import me.zhengjie.common.vo.Result;
import me.zhengjie.entity.park.ParkingList;
import me.zhengjie.entity.park.ParkingRequest;
import me.zhengjie.entity.park.ParkingVo;
import me.zhengjie.service.park.IParkingService;
import lombok.AllArgsConstructor;
import me.zhengjie.system.service.dto.UserDto;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author yongyan.pu
 * @version $Id: ParkingController.java, v 1.0 2020年10月26日 下午11:15:02 yongyan.pu Exp $
 */

@RestController
@RequestMapping("/api/parking")
@AllArgsConstructor
public class WebParkingController {

    private final IParkingService parkingService;

    private final SecurityUtil securityUtil;

    /**
     * 新增停车场
     * 
     * @param ParkingRequest
     * @return
     */
    @PostMapping("/lot")
    public Result<Object>add(@RequestBody ParkingRequest ParkingRequest) {
        return parkingService.add(ParkingRequest);
    }

    /**
     * 获取停车场列表
     * 
     * @param gridId
     * @param keywords
     * @param current
     * @param size
     * @return
     */
    @GetMapping("/lot")
    public Result<IPage<ParkingList>> parkingList(@RequestParam(value = "gridId", defaultValue = "") String gridId,
                                             @RequestParam(value = "keywords", defaultValue = "") String keywords,
                                             @RequestParam(value = "streetId", defaultValue = "") String streetId,
                                             @RequestParam(value = "communityId", defaultValue = "") String communityId,
                                             @RequestParam(value = "current", defaultValue = "1") Integer current,
                                             @RequestParam(value = "size", defaultValue = "10") Integer size) {
        //用户数据权限
        UserDto user = securityUtil.getCurrUser();
        if (user.getPower() != null){
            if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
                String deptId = user.getPower().getDeptId();
                Integer attribute = user.getPower().getAttribute();
                if (attribute == 1){//街道
                    streetId = deptId;
                }else if (attribute == 2){//社区
                    communityId = deptId;
                }else if (attribute == 3){//网格
                    gridId = deptId;
                }
            }
        }
        return parkingService.getParkingList(gridId, streetId, communityId, keywords, current, size);
    }

    /**
     * 修改停车场
     * 
     * @param id
     * @param ParkingRequest
     * @return
     */
    @PutMapping("/lot/{id}")
    public Result<Object>update(@PathVariable(value = "id") Long id, @RequestBody ParkingRequest ParkingRequest) {
        return parkingService.update(id, ParkingRequest);
    }

    /**
     * 删除停车场
     * 
     * @param id
     * @return
     */
    @DeleteMapping("/lot/{id}")
    public Result<Object>delete(@PathVariable(value = "id") Long id) {
        return parkingService.delete(id);
    }

    /**
     * 查询全部停车场
     * 
     * @return
     */
    @GetMapping("/lot/loadAll")
    public Result<List<ParkingVo>> loadAll() {
        //用户数据权限
        UserDto user = securityUtil.getCurrUser();
        Map<String,Object> map = new HashMap<>();
        if (user.getPower() != null){
            if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
                String deptId = user.getPower().getDeptId();
                Integer attribute = user.getPower().getAttribute();
                if (attribute == 1){//街道
                    map.put("street_id",deptId);
                }else if (attribute == 2){//社区
                    map.put("community_id",deptId);
                }else if (attribute == 3){//网格
                    map.put("grid_id",deptId);
                }
            }
        }
        return parkingService.loadAll(map);
    }

    /**
     * 获取车辆进出记录统计
     *
     * @return
     */
    @GetMapping("/lot/getCarStatistics")
    public Result<Object>getCarStatistics(String gridId) {
        return ResultUtil.data(parkingService.getCarStatistics(gridId)) ;
    }
}
