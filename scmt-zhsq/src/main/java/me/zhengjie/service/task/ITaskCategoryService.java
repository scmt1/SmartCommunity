package me.zhengjie.service.task;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.entity.task.TaskCategory;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ljj
 * @since 2019-05-06
 */
public interface ITaskCategoryService extends IService<TaskCategory> {

    /**
     * 删除任务类别信息
     * @param taskCategoryId
     */
    void delete(Integer taskCategoryId);

    /**
     * 根据任务类别id获取单个任务类别信息
     * @param taskCategoryId
     * @return
     */
    TaskCategory loadOne(Integer taskCategoryId);

    /**
     * 根据查询条件获取所有任务类别信息
     * @return
     */
    List<TaskCategory> loadAllByProperty(JSONObject query);

    /**
     * 查询分类并带部门
     * @param query
     * @return
     */
    List<TaskCategory> loadAllAndDepartment(JSONObject query);

    /**
     * 新增或修改
     * @param taskCategories
     */
    void saveAndUpdate(JSONObject taskCategories);

    /**
     * 根据小区获取下拉查询
     * @param gridId
     * @return
     */
    List<Map<String, Object>> loadAllBySelect(String gridId);
}
