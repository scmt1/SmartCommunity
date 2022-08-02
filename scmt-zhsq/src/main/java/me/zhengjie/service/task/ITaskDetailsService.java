package me.zhengjie.service.task;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.entity.task.TaskDetails;

import java.util.List;
import java.util.Map;

public interface ITaskDetailsService extends IService<TaskDetails> {

    /**
     * 修改任务详情信息
     * @param taskDetails
     */
    void modify(TaskDetails taskDetails) throws Exception;

    /**
     * 根据任务详情id获取单个任务详情信息
     * @param taskDetailsId
     * @return
     */
    TaskDetails loadOne(Integer taskDetailsId);

    /**
     * 根据查询条件获取所有任务详情信息
     * @param query
     * @return
     */
    IPage<Map<String,Object>> loadAllByQuery(JSONObject query);

    /**
     * 根据Id删除任务详情明细
     * @param taskScoreDetailsId
     */
    void delete(Integer taskScoreDetailsId);

    /**
     * 新增任务详情信息
     * @param taskDetails
     */
    void add(TaskDetails taskDetails);

    /**
     * 任务模块派单接口
     * @param query
     */
    void dispatch(JSONObject query);

    /**
     * 任务模块获取周期任务获取历史记录
     * @param query
     */
    IPage<Map<String, Object>> loadTaskHistory(JSONObject query);

    /**
     * 获取任务列表
     * @param query
     */
    IPage<Map<String, Object>> loadAllForApp(JSONObject query);

    /**
     * query
     * @param query
     * @return
     */
    Map<String, Object> loadCountForApp(JSONObject query);

    /**
     * 工单详情
     * @param query
     * @return
     */
    Map<String, Object> loadDateForApp(JSONObject query);

    /**
     * 修改接口
     * @param taskDetails
     */
    void changeUrgency(TaskDetails taskDetails);

    /**
     * 催单
     * @param query
     */
    void reminder(JSONObject query);

    /**
     * 评价
     * @param query
     */
    void orderEvaluate(JSONObject query);

    /**
     * 任务统计
     * @param query
     * @return
     */
    List<Map<String, Object>> taskStatistics(JSONObject query);

    /**
     * 数据大屏
     * @param query
     * @return
     */
    Map<String, Object> getBigData(JSONObject query);
}
