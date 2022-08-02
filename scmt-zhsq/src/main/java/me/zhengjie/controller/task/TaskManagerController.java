package me.zhengjie.controller.task;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import me.zhengjie.common.utils.ResultCode;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.utils.SecurityUtil;
import me.zhengjie.common.vo.Result;
import me.zhengjie.entity.task.TaskDetails;
import me.zhengjie.entity.task.TaskManager;
import me.zhengjie.service.task.ITaskManagerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.system.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Security;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  任务管理
 * </p>
 *
 * @author ljj
 * @since 2019-05-06
 */
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/api/work/task-manager")
public class TaskManagerController {
    
    private final ITaskManagerService taskManagerService;

    private final SecurityUtil securityUtil;

    /**
     * 添加任务管理信息
     * @param taskManager
     * @return
     * @throws Exception
     */
    @PostMapping("/add")
    public Result<Object> addTaskManager(TaskManager taskManager) throws Exception{
        taskManagerService.add(taskManager);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 删除任务管理信息
     * @param taskManagerId
     * @return
     */
    @GetMapping("/delete")
    public Result<Object> deleteTaskManager(Integer taskManagerId){
        taskManagerService.delete(taskManagerId);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 获取周期任务最新情况
     * @param taskManagerId
     * @return
     */
    @GetMapping("/loadManagerNew")
    public  Result<Object> loadManagerNew(Integer taskManagerId){
        Long id = taskManagerService.loadManagerNew(taskManagerId);
        return ResultUtil.data(id);
    }

    /**
     * 修改任务管理信息
     * @param taskManager
     * @return
     */
    @PostMapping("/modify")
    public Result<Object> modifyTaskManager(TaskManager taskManager){
        taskManagerService.modify(taskManager);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 修改状态
     * @param taskManager
     * @return
     */
    @PostMapping("/updateStatus")
    public Result<Object> updateStatus(@RequestBody TaskManager taskManager){
        taskManagerService.updateStatus(taskManager);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 获取单个任务管理
     * @param taskManagerId
     * @return
     */
    @GetMapping("/loadOne")
    public Result<Object> loadOne(Integer taskManagerId){
        Map<String,Object> taskManager = taskManagerService.loadOne(taskManagerId);
        return ResultUtil.data(taskManager);
    }

    /**
     * 获取单个任务管理
     * @param taskManagerId
     * @return
     */
    @GetMapping("/getTaskManager")
    public Result<Object> getTaskManager(Integer taskManagerId){
        TaskManager taskManager = taskManagerService.getTaskManager(taskManagerId);
        return ResultUtil.data(taskManager);
    }

    /**
     * 获取周期信息
     * @param taskManagerId
     * @return
     */
    @GetMapping("/loadCycleInfo")
    public Result<Object> loadCycleInfo(Integer taskManagerId){
        Map<String,Object> taskManager = taskManagerService.loadCycleInfo(taskManagerId);
        return ResultUtil.data(taskManager);
    }

    /**
     * 获取所有任务管理
     * @param query
     * @return
     */
    @PostMapping("/loadAllByQuery")
    public Result<Object> loadAllByQuery(@RequestBody JSONObject query){
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
        IPage<Map<String,Object>> taskManagers = taskManagerService.loadAllByQuery(query);
        return ResultUtil.data(taskManagers);
    }
    /**
     * 获取所有周期任务
     * @param query
     * @return
     */
    @PostMapping("/loadAllPeriodicTask")
    public Result<Object> loadAllPeriodicTask(@RequestBody JSONObject query){
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
        IPage<Map<String,Object>> taskManagers = taskManagerService.loadAllPeriodicTask(query);
        return ResultUtil.data(taskManagers);
    }

    /**
     * loadTaskForApp
     * @param gridId
     * @return
     */
    @GetMapping("/loadTaskForApp")
    public Result<Object> loadTaskForApp(String gridId){
       List<Map<String,Object>> taskManagers = taskManagerService.loadTaskForApp(gridId);
        return ResultUtil.data(taskManagers);
    }

    /**
     * 查看我的发布
     * @param query
     * @return
     */
    @PostMapping("/loadByQuery")
    public Result<Object> loadByQuery(@RequestBody JSONObject query){
        List<Map<String, Object>> taskManagers = taskManagerService.loadByQuery(query);
        return ResultUtil.data(taskManagers);
    }

    /**
     * 排行榜
     * @param query
     * @return
     */
    @PostMapping("/loadByAchievements")
    public Result<Object> loadByAchievements(@RequestBody JSONObject query){
        List<Map<String, Object>> taskManagers = taskManagerService.loadByAchievements(query);
        return ResultUtil.data(taskManagers);
    }

    /**
     * 管理者查看我的绩效
     * @param query
     * @return
     */
    @PostMapping("/loadByDeparentAchievements")
    public Result<Object> loadByDeparentAchievements(@RequestBody JSONObject query){
        List<Map<String, Object>> taskManagers = taskManagerService.loadByDeparentAchievements(query);
        return ResultUtil.data(taskManagers);
    }

    /**
     * 管理者查看绩效总情况
     * @param query
     * @return
     */
    @PostMapping("/loadViewingTotal")
    public  Result<Object> loadViewingTotal(@RequestBody JSONObject query){
       Map<String, Object> taskManager = taskManagerService.loadViewingTotal(query);
        return ResultUtil.data(taskManager);
    }

    /**
     * 查看个人绩效总情况
     * @param query
     * @return
     */
    @PostMapping("/loadViewingTotalByUser")
    public  Result<Object> loadViewingTotalByUser(@RequestBody JSONObject query){
       Map<String, Object> taskManager = taskManagerService.loadViewingTotalByUser(query);
        return ResultUtil.data(taskManager);
    }

    /**
     * 查看个人绩效总情况
     * @param query
     * @return
     */
    @PostMapping("/loadTaskByUser")
    public Result<Object> loadTaskByUser(@RequestBody JSONObject query){
       List<Map<String, Object>> taskManagers = taskManagerService.loadTaskByUser(query);
        return ResultUtil.data(taskManagers);
    }

    /**
     * 添加任务评价
     * @param parameter
     * @return
     * @throws Exception
     */
    @PostMapping("/addEvaluation")
    public Result<Object> addResult(@RequestBody JSONObject parameter) throws Exception{
        taskManagerService.addEvaluation(parameter);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 接单
     * @param parameter
     * @return
     * @throws Exception
     */
    @PostMapping("/orderTask")
    public Result<Object> orderTask(@RequestBody JSONObject parameter) throws Exception{
      JSONObject jsonObject = taskManagerService.orderTask(parameter);
        return ResultUtil.data(jsonObject);
    }

    /**
     * 完成任务
     * @param taskDetails
     * @return
     * @throws Exception
     */
    @PostMapping("/completeTask")
    public Result<Object> completeTask(TaskDetails taskDetails) throws Exception{
        taskManagerService.completeTask(taskDetails);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 审核
     * @param query
     * @return
     * @throws ParseException
     */
    @PostMapping("/review")
    public Result<Object> review(@RequestBody JSONObject query) throws ParseException {
        taskManagerService.review(query);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 获取评价信息
     * @param taskManagerId
     * @return
     */
    @GetMapping("/loadEvaluationInfo")
    public Result<Object> loadEvaluationInfo(Integer taskManagerId) {
        Map<String, Object> result = taskManagerService.loadEvaluationInfo(taskManagerId);
        return ResultUtil.data(result);
    }

    /**
     * 获取系统评价信息
     * @param param
     * @return
     */
    @PostMapping("/loadSystemScore")
    public Result<Object> loadSystemScore(@RequestBody JSONObject param) {
        List<Map<String,Object>> result = taskManagerService.loadSystemScore(param);
        return ResultUtil.data(result);
    }

    /**
     * 取消
     * @param taskManagerId
     * @return
     */
    @GetMapping("/cancel")
    public Result<Object> cancel(Integer taskManagerId) {
        taskManagerService.cancel(taskManagerId);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 获取单个任务管理
     * @param taskManagerId
     * @return
     */
    @GetMapping("/loadOneDetails")
    public Result<Object> loadOneDetails(Integer taskManagerId){
        Map<String,Object> taskManager = taskManagerService.loadOneDetails(taskManagerId);
        return ResultUtil.data(taskManager);
    }




}
