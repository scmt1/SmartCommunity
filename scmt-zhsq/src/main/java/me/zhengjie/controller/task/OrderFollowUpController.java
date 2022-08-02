package me.zhengjie.controller.task;


import com.alibaba.fastjson.JSONObject;

import me.zhengjie.common.utils.ResultCode;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.Result;
import me.zhengjie.entity.task.OrderFollowUp;
import me.zhengjie.service.task.IOrderFollowUpService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  工单跟进明细
 * </p>
 *
 * @author ljj
 * @since 2019-05-06
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/work/order-follow-up")
public class OrderFollowUpController {

    @Autowired
    private IOrderFollowUpService orderFollowUpService;

    /**
     * 添加工单跟进明细信息
     * @param orderFollowUp
     * @return
     */
    @PostMapping("/add")
    public Result<Object> addOrderFollowUp(@RequestBody OrderFollowUp orderFollowUp){
        orderFollowUpService.add(orderFollowUp);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 删除工单跟进明细信息
     * @param orderFollowUpId
     * @return
     */
    @GetMapping("/delete")
    public Result<Object> deleteOrderFollowUp(Integer orderFollowUpId){
        orderFollowUpService.delete(orderFollowUpId);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 根据工单跟进明细信息修改
     * @param orderFollowUp
     * @return
     */
    @PostMapping("/modify")
    public Result<Object> modifyOrderFollowUp(@RequestBody OrderFollowUp orderFollowUp){
        orderFollowUpService.modify(orderFollowUp);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 获取单个工单跟进明细
     * @param orderFollowUpId
     * @return
     */
    @GetMapping("/loadOne")
    public Result<Object> loadOne(Integer orderFollowUpId){
        OrderFollowUp orderFollowUp = orderFollowUpService.loadOne(orderFollowUpId);
        return ResultUtil.data(orderFollowUp);
    }

    /**
     * 获取所有工单跟进明细
     * @param query
     * @return
     */
    @PostMapping("/loadByQuery")
    public  Result<Object> loadByQuery(@RequestBody JSONObject query){
        List<OrderFollowUp> orderFollowUpList = orderFollowUpService.loadByQuery(query);
        return ResultUtil.data(orderFollowUpList);
    }

}
