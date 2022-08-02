package me.zhengjie.service.task;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.entity.task.TaskCategory;
import me.zhengjie.entity.task.TaskManager;
import me.zhengjie.entity.task.TaskScoreDetails;
import me.zhengjie.entity.task.TaskTimelyScore;

import java.util.List;
import java.util.Map;

public interface ITaskScoreService  {

    /**
     * 修改任务分数信息
     * @param taskCategory
     */
    void modify(TaskCategory taskCategory);

    /**
     * 根据任务分数id获取单个任务分数信息
     * @param taskCategoryId
     * @return
     */
    TaskCategory loadOne(Integer taskCategoryId);

    /**
     * 根据查询条件获取所有任务分数信息
     * @param query
     * @return
     */
    IPage<Map<String,Object>> loadAllByQuery(JSONObject query);

    /**
     * 根据Id删除任务分数明细
     * @param taskScoreDetailsId
     */
    void deleteDetails(Integer taskScoreDetailsId);

    /**
     * 查询当前小区的及时率分数
     * @param
     * @return
     */
    Map<String,Object> loadTimely();

    /**
     * 新增或删除任务及时率分数
     * @param taskTimelyScore
     */
    void saveOrUpdateTimely(TaskTimelyScore taskTimelyScore);

    /**
     * 新增或删除任务分数明细
     * @param taskScoreDetails
     */
    void saveOrUpdateDetails(List<TaskScoreDetails> taskScoreDetails);

    /**
     * 根据主单ID查询所有任务分数明细
     * @param masterId
     * @return
     */
    List<TaskScoreDetails> loadAllDetailsByMaster(Integer masterId);

    /**
     * 根据主单Code查询所有任务分数明细
     * @param param
     * @return
     */
    JSONObject loadAllDetailsByCode(JSONObject param);

}
