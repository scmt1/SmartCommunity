package me.zhengjie.controller.task;


import com.alibaba.fastjson.JSONObject;

import me.zhengjie.common.utils.ResultCode;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.Result;
import me.zhengjie.entity.task.TaskCategory;
import me.zhengjie.service.task.ITaskCategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 任务管理
 * </p>
 *
 * @author ljj
 * @since 2019-05-06
 */
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/work/task-category")
public class TaskCategoryController {

    private final ITaskCategoryService taskCategoryService;

    /**
     * 添加或修改任务类别信息
     *
     * @param taskCategories
     * @return
     */
    @PostMapping("/saveAndUpdate")
    public Result<Object> saveAndUpdate(@RequestBody JSONObject taskCategories) {
        taskCategoryService.saveAndUpdate(taskCategories);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 删除任务类别信息
     *
     * @param taskCategoryId
     * @return
     */
    @GetMapping("/delete")
    public Result<Object> deleteTaskCategory(Integer taskCategoryId) {
        taskCategoryService.delete(taskCategoryId);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 根据任务类别id获取
     *
     * @param taskCategoryId
     * @return
     */
    @GetMapping("/loadOne")
    public Result<Object> loadOne(Integer taskCategoryId) {
        TaskCategory taskCategory = taskCategoryService.loadOne(taskCategoryId);
        return ResultUtil.data(taskCategory);
    }

    /**
     * 获取所有任务类别
     *
     * @param query
     * @return
     */
    @PostMapping("/loadAll")
    public Result<Object> loadAllByQuery(@RequestBody JSONObject query) {
        List<TaskCategory> taskCategoryList = taskCategoryService.loadAllByProperty(query);
        return ResultUtil.data(taskCategoryList);
    }

    /**
     * 获取所有任务类别
     *
     * @param gridId
     * @return
     */
    @GetMapping("/loadAllBySelect")
    public Result<Object> loadAllBySelect(String gridId) {
        List<Map<String, Object>> taskCategoryList = taskCategoryService.loadAllBySelect(gridId);
        return ResultUtil.data(taskCategoryList);
    }

    /**
     * 获取所有任务类别
     *
     * @param query
     * @return
     */
    @PostMapping("/loadAllAndDepartment")
    public Result<Object> loadAllAndDepartment(@RequestBody JSONObject query) {
        List<TaskCategory> taskCategoryList = taskCategoryService.loadAllAndDepartment(query);
        return ResultUtil.data(taskCategoryList);
    }
}
