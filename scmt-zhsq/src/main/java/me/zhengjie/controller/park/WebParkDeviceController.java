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
import me.zhengjie.entity.park.ParkDeviceList;
import me.zhengjie.entity.park.UpdateParkDeviceRequest;
import me.zhengjie.service.park.IParkingDeviceService;
import lombok.AllArgsConstructor;
import me.zhengjie.system.service.dto.UserDto;
import org.springframework.web.bind.annotation.*;

/**
 * 
 * @author yongyan.pu
 * @version $Id: ParkDeviceController.java, v 1.0 2020年10月26日 下午3:07:24 yongyan.pu Exp $
 */
@RestController
@RequestMapping("/api/parking")
@AllArgsConstructor
public class WebParkDeviceController {

    private final IParkingDeviceService parkingDeviceService;

    private final SecurityUtil securityUtil;

    /**
     *  新增
     * 
     * @param parkDevice
     * @return
     */
    @PostMapping("/device")
    public Result<Object>add(@RequestBody UpdateParkDeviceRequest parkDevice) {
        return parkingDeviceService.add(parkDevice);
    }

    /**
     * 修改
     * 
     * @param id
     * @param parkDevice
     * @return
     */

    @PutMapping("/device/{id}")
    public Result<Object>update(@PathVariable(value = "id") Long id, @RequestBody UpdateParkDeviceRequest parkDevice) {
        return parkingDeviceService.updateParkDevice(id, parkDevice);
    }

    /**
     * 删除
     * 
     * @param id
     * @return
     */
    @DeleteMapping("/device/{id}")
    public Result<Object>delete(@PathVariable(value = "id") Long id) {
        return parkingDeviceService.deleteById(id);
    }

    /**
     * 查询 
     * 
     * @param gridId
     * @param keywords
     * @param current
     * @param size
     * @return
     */

    @GetMapping("/device")
    public Result<IPage<ParkDeviceList>> getParkingDeviceList(@RequestParam(value = "gridId", defaultValue = "") String gridId,
                                                         @RequestParam(value = "keywords", defaultValue = "") String keywords,
                                                         @RequestParam(value = "current", defaultValue = "1") Integer current,
                                                         @RequestParam(value = "size", defaultValue = "10") Integer size) {
        //用户数据权限
        UserDto user = securityUtil.getCurrUser();
        String streetId = "";
        String communityId = "";
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
        IPage<ParkDeviceList> list = parkingDeviceService.getParkingDeviceList(streetId, communityId, gridId, keywords, current, size);
        return ResultUtil.data(list);
    }
}
