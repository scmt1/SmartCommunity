package me.zhengjie.controller.iot;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.utils.SecurityUtil;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.utils.ResultCode;
import me.zhengjie.entity.iot.ParkingBarrier;
import me.zhengjie.service.iot.IParkingBarrierService;
import lombok.AllArgsConstructor;
import me.zhengjie.system.service.dto.UserDto;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 停车道闸管理
 * </p>
 *
 * @author ly
 * @since 2019-05-06
 */
@RestController
@RequestMapping("/api/parkingBarrier")
@AllArgsConstructor
public class ParkingBarrierController {

    private final IParkingBarrierService parkingBarrierService;

    private final SecurityUtil securityUtil;

    /**
     * 添加停车道闸信息
     * @param parkingBarrier
     * @return
     */
    @PostMapping("/add")
    public Result<Object>addParkingBarrier(@RequestBody ParkingBarrier parkingBarrier) {
        parkingBarrierService.add(parkingBarrier);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 删除停车道闸信息
     * @param parkingBarrierId
     * @return
     */
    @GetMapping("/delete")
    public Result<Object>deleteParkingBarrier(Integer parkingBarrierId) {
        parkingBarrierService.delete(parkingBarrierId);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 修改停车道闸信息
     * @param parkingBarrier
     * @return
     */
    @PostMapping("/modify")
    public Result<Object>modifyParkingBarrier(@RequestBody ParkingBarrier parkingBarrier) {
        parkingBarrierService.modify(parkingBarrier);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 获取单个停车道闸
     * @param parkingBarrierId
     * @return
     */
    @GetMapping("/loadOne")
    public Result<Object>loadOne(Integer parkingBarrierId) {
        ParkingBarrier parkingBarrier = parkingBarrierService.loadOne(parkingBarrierId);
        return ResultUtil.data(parkingBarrier);
    }

    /**
     * 获取所有停车道闸
     * @param query
     * @return
     */
    @PostMapping("/loadAllByQuery")
    public Result<Object>loadAllByQuery(@RequestBody JSONObject query) {
        //用户数据权限
        UserDto user = securityUtil.getCurrUser();
        if (user.getPower() != null){
            if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
                String deptId = user.getPower().getDeptId();
                Integer attribute = user.getPower().getAttribute();
                if (attribute == 1){//街道
                    query.put("streetId",deptId);
                }else if (attribute == 2){//社区
                    query.put("communityId",deptId);
                }else if (attribute == 3){//网格
                    query.put("gridId",deptId);
                }
            }
        }
        IPage<Map<String, Object>> parkingBarriers = parkingBarrierService.loadAllByQuery(query);
        return ResultUtil.data(parkingBarriers);
    }


}
