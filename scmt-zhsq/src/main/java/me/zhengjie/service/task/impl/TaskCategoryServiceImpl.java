package me.zhengjie.service.task.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.mapper.task.TaskCategoryMapper;
import me.zhengjie.mapper.task.TaskScoreDetailsMapper;
import me.zhengjie.entity.task.TaskCategory;
import me.zhengjie.entity.task.TaskScoreDetails;
import me.zhengjie.service.task.ITaskCategoryService;
import lombok.AllArgsConstructor;
import me.zhengjie.util.BusinessErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ljj
 * @since 2019-05-06
 */
@Service
@AllArgsConstructor
public class TaskCategoryServiceImpl extends ServiceImpl<TaskCategoryMapper, TaskCategory> implements ITaskCategoryService {

    private final TaskCategoryMapper taskCategoryMapper;

    private final TaskScoreDetailsMapper taskScoreDetailsMapper;

    @Override
    public void delete(Integer taskCategoryId) {
        TaskCategory taskCategory = taskCategoryMapper.selectById(taskCategoryId);
        taskCategory.setStatus(1);
        taskCategoryMapper.updateById(taskCategory);
    }

    @Override
    public TaskCategory loadOne(Integer taskCategoryId) {
        return taskCategoryMapper.selectById(taskCategoryId);
    }

    @Override
    public List<TaskCategory> loadAllByProperty(JSONObject query) {
        QueryWrapper<TaskCategory> queryWrapper = new QueryWrapper<TaskCategory>();
        queryWrapper.lambda().eq(TaskCategory::getStatus, 0);
        return taskCategoryMapper.selectList(queryWrapper);
    }

    @Override
    public List<TaskCategory> loadAllAndDepartment(JSONObject query) {
        List<TaskCategory> taskCategories = loadAllByProperty(query);
        return taskCategories;
    }

    @Override
    public void saveAndUpdate(JSONObject taskCategories) {
        JSONArray jsonArray = taskCategories.getJSONArray("taskCategories");


        List<TaskCategory> taskCategoryList = jsonArray.toJavaList(TaskCategory.class);
        for (TaskCategory taskCategory : taskCategoryList) {
            if (taskCategory.getId() !=null){
                Integer count = taskCategoryMapper.selectCount(new QueryWrapper<TaskCategory>().lambda()
                        .eq(TaskCategory::getName, taskCategory.getName())
                        .notIn(TaskCategory::getId,taskCategory.getId())
                        .eq(TaskCategory::getStatus,0));
                if (count>0){
                    throw new BusinessErrorException("分类名为:" + taskCategory.getName() + "在该网格已经存在，请重新输入");
                }
            }else {
                Integer count = taskCategoryMapper.selectCount(new QueryWrapper<TaskCategory>().lambda()
                        .eq(TaskCategory::getName, taskCategory.getName())
                        .eq(TaskCategory::getStatus,0));
                taskCategory.setStatus(0);
                if (count>0){
                    throw new BusinessErrorException("分类名为:" + taskCategory.getName() + "在该网格已经存在，请重新输入");
                }
            }

        }

        saveOrUpdateBatch(taskCategoryList);
    }

    @Override
    public List<Map<String, Object>> loadAllBySelect(String gridId) {
        QueryWrapper<TaskCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TaskCategory::getStatus, 0);
        queryWrapper.lambda().select(TaskCategory::getId, TaskCategory::getName);
        return taskCategoryMapper.selectMaps(queryWrapper);
    }
}
