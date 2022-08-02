package me.zhengjie.controller.task;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;

import me.zhengjie.common.utils.ResultCode;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.Result;
import me.zhengjie.entity.task.TaskCategory;
import me.zhengjie.entity.task.TaskScoreDetails;
import me.zhengjie.entity.task.TaskTimelyScore;
import me.zhengjie.service.task.ITaskScoreService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 任务分数设置
 */
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/work/score")
public class TaskScoreController {
    
    private final ITaskScoreService taskScoreService;

    /**
     * 修改任务分数信息
     * @param taskCategory
     * @return
     */
    @PostMapping("/modify")
     public Result<Object> modifyTaskScore(@RequestBody TaskCategory taskCategory) {
        taskScoreService.modify(taskCategory);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 获取单个任务分数
     * @param taskCategoryId
     * @return
     */
    @GetMapping("/loadOne")
     public Result<Object> loadOne(Integer taskCategoryId) {
        TaskCategory taskCategory = taskScoreService.loadOne(taskCategoryId);
        return ResultUtil.data(taskCategory);
    }

    /**
     * 获取所有任务分数
     * @param query
     * @return
     */
    @PostMapping("/loadAllByQuery")
     public Result<Object> loadAllByQuery(@RequestBody JSONObject query) {
        IPage<Map<String, Object>> taskScores = taskScoreService.loadAllByQuery(query);
        return ResultUtil.data(taskScores);
    }

    /**
     * 修改及时分数信息
     * @param taskTimelyScore
     * @return
     */
    @PostMapping("/timely/saveOrUpdate")
     public Result<Object> saveOrUpdateTimely(@RequestBody TaskTimelyScore taskTimelyScore) {
        taskScoreService.saveOrUpdateTimely(taskTimelyScore);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 获取及时率分数
     * @param
     * @return
     */
    @GetMapping("/loadTimely")
     public Result<Object> loadTimely() {
        Map<String, Object> taskTimelyScore = taskScoreService.loadTimely();
        return ResultUtil.data(taskTimelyScore);
    }

    /**
     * 修改任务分数信息
     * @param taskScoreDetails
     * @return
     */
    @PostMapping("/details/saveOrUpdate")
     public Result<Object> modifyTaskScoreDetails(@RequestBody List<TaskScoreDetails> taskScoreDetails) {
        taskScoreService.saveOrUpdateDetails(taskScoreDetails);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 删除任务分数信息
     * @param taskScoreDetailsId
     * @return
     */
    @GetMapping("/details/delete")
     public Result<Object> deleteTaskScoreDetails(Integer taskScoreDetailsId) {
        taskScoreService.deleteDetails(taskScoreDetailsId);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 获取所有任务分数明细
     * @param masterId
     * @return
     */
    @GetMapping("/details/loadAllDetailsByMaster")
     public Result<Object> loadAllDetailsByMaster(Integer masterId) {
        List<TaskScoreDetails> taskScoreDetails = taskScoreService.loadAllDetailsByMaster(masterId);
        return ResultUtil.data(taskScoreDetails);
    }

    /**
     * 获取所有任务分数明细
     * @param param
     * @return
     */
    @PostMapping("/details/loadAllDetailsScore")
     public Result<Object> loadAllDetailsByCode(@RequestBody JSONObject param) {
        JSONObject taskScoreDetails = taskScoreService.loadAllDetailsByCode(param);
        return ResultUtil.data(taskScoreDetails);
    }
}
