package me.zhengjie.controller.task;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import me.zhengjie.common.utils.ResultCode;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.utils.SecurityUtil;
import me.zhengjie.common.vo.Result;
import me.zhengjie.entity.task.TaskDetails;
import me.zhengjie.service.task.ITaskDetailsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.system.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 任务详情接口
 */
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/work/details")
public class TaskDetailsController {

    private final ITaskDetailsService taskDetailsService;

    private final SecurityUtil securityUtil;

    /**
     * 新增任务详情信息
     *
     * @param taskDetails
     * @return
     */
    @PostMapping("/add")
    public Result<Object> add(@RequestBody TaskDetails taskDetails) {
        taskDetailsService.add(taskDetails);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 修改任务详情信息
     *
     * @param taskDetails
     * @return
     * @throws Exception
     */
    @PostMapping("/modify")
    public Result<Object> modify(TaskDetails taskDetails) throws Exception {
        taskDetailsService.modify(taskDetails);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    @PostMapping("/changeUrgency")
    public Result<Object> changeUrgency(@RequestBody TaskDetails taskDetails)  {
        taskDetailsService.changeUrgency(taskDetails);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 任务模块派单接口
     *
     * @param query
     * @return
     */
    @PostMapping("/dispatch")
    public Result<Object> dispatch(@RequestBody JSONObject query) {
        taskDetailsService.dispatch(query);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 获取单个任务详情
     *
     * @param taskDetailsId
     * @return
     */
    @GetMapping("/loadOne")
    public Result<Object> loadOne(Integer taskDetailsId) {
        TaskDetails taskDetails = taskDetailsService.loadOne(taskDetailsId);
        return ResultUtil.data(taskDetails);
    }

    /**
     * 获取所有任务详情
     *
     * @param query
     * @return
     */
    @PostMapping("/loadAllByQuery")
    public Result<Object> loadAllByQuery(@RequestBody JSONObject query) {
        IPage<Map<String, Object>> TaskDetailss = taskDetailsService.loadAllByQuery(query);
        return ResultUtil.data(TaskDetailss);
    }

    /**
     * 获取历史记录
     *
     * @param query
     * @return
     */
    @PostMapping("/loadTaskHistory")
    public Result<Object> loadTaskHistory(@RequestBody JSONObject query) {
        IPage<Map<String, Object>> TaskDetailss = taskDetailsService.loadTaskHistory(query);
        return ResultUtil.data(TaskDetailss);
    }

    /**
     * 删除任务详情信息
     *
     * @param TaskDetailsDetailsId
     * @return
     */
    @GetMapping("/delete")
    public Result<Object> delete(Integer TaskDetailsDetailsId) {
        taskDetailsService.delete(TaskDetailsDetailsId);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 获取任务列表
     * @param query
     * @return
     * @throws ParseException
     */
    @PostMapping("/loadAllForApp")
    public  Result<Object> loadAllForApp(@RequestBody JSONObject query) throws ParseException {
        IPage<Map<String, Object>> mapIPage = taskDetailsService.loadAllForApp(query);
        return ResultUtil.data(mapIPage);
    }

    /**
     * 任务数量接口
     * @param query
     * @return
     */
    @PostMapping("/loadCountForApp")
    public Result<Object> loadCountForApp(@RequestBody JSONObject query) {
        Map<String, Object> map = taskDetailsService.loadCountForApp(query);
        return ResultUtil.data(map);
    }

    /**
     * 工单详情
     * @param query
     * @return
     */
    @PostMapping("/loadDateForApp")
    public Result<Object> loadDateForApp(@RequestBody JSONObject query) {
        Map<String, Object> date = taskDetailsService.loadDateForApp(query);
        return ResultUtil.data(date);
    }

    /**
     * 催单
     * @param query
     * @return
     */
    @PostMapping("/reminder")
    public Result<Object> reminder(@RequestBody JSONObject query) {
        taskDetailsService.reminder(query);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 评价接口
     * @param query
     * @return
     * @throws Exception
     */
    @PostMapping("/orderEvaluate")
    public Result<Object> evaluate(@RequestBody JSONObject query) throws Exception {
        taskDetailsService.orderEvaluate(query);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 任务统计
     * @param query
     * @return
     * @throws Exception
     */
    @PostMapping("/taskStatistics")
    public Result<Object> taskStatistics(@RequestBody JSONObject query) throws Exception {
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
        List<Map<String,Object>> result= taskDetailsService.taskStatistics(query);
        return ResultUtil.data(result);
    }

    /**
     * 任务大数据统计
     * @param query
     * @return
     * @throws Exception
     */
    @PostMapping("/getBigData")
    public Result<Object> getBigData(@RequestBody JSONObject query) throws Exception {
     Map<String,Object> result= taskDetailsService.getBigData(query);
        return ResultUtil.data(result);
    }

}
