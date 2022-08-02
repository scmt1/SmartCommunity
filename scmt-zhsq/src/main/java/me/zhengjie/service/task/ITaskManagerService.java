package me.zhengjie.service.task;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.entity.task.TaskDetails;
import me.zhengjie.entity.task.TaskManager;


import java.text.ParseException;
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
public interface ITaskManagerService extends IService<TaskManager> {

    /**
     * 添加任务管理信息
     * @param taskManager
     */
    void add(TaskManager taskManager) throws Exception;

    /**
     * 删除任务管理信息
     * @param taskManagerId
     */
    void delete(Integer taskManagerId);

    /**
     * 修改任务管理信息
     * @param taskManager
     */
    void modify(TaskManager taskManager);

    /**
     * 根据任务管理id获取单个任务管理信息
     * @param taskManagerId
     * @return
     */
    Map<String,Object> loadOne(Integer taskManagerId);

    /**
     * 根据查询条件获取所有任务管理信息
     * @param query
     * @return
     */
    IPage<Map<String,Object>> loadAllByQuery(JSONObject query);

    /**
     *
     * 根据电话查询我的发布
     * */
    List<Map<String, Object>> loadByQuery(JSONObject query);

    /**
     * 根据条件查看我的绩效
     * */
    List<Map<String, Object>> loadByAchievements(JSONObject query);

    /**
     * 管理者查询所有情况
     * */
    Map<String, Object> loadViewingTotal(JSONObject query);

    /**
     * 员工查询个人的绩效情况
     * */
    Map<String, Object> loadViewingTotalByUser(JSONObject query);

    /**
     * 员工查询个人的任务完成情况
     * */
    List<Map<String, Object>> loadTaskByUser(JSONObject query);


    /**
     * 审核
     * type 1：通过；2：驳回
     * @param query
     */
    void review(JSONObject query) throws ParseException;

    /**
     * 获取评分信息
     * @param taskManagerId
     * @return
     */
    Map<String, Object> loadEvaluationInfo(Integer taskManagerId);

    /**
     * 添加完成信息
     * @param taskDetails
     * @throws Exception
     */
    void completeTask(TaskDetails taskDetails) throws Exception;

    /**
     * 接单
     * @param parameter
     */
    JSONObject orderTask(JSONObject parameter) throws Exception;

    /**
     * 添加评价
     * @param parameter
     */
    void addEvaluation(JSONObject parameter);

    /**
     * 取消
     * @param taskManagerId
     * @return
     */
    void cancel(Integer taskManagerId);

    /**
     * 根据主单id加载最近的一个单据
     * @param taskManagerId
     * @return
     */
    Map<String, Object> loadOneDetails(Integer taskManagerId);

    /**
     * 获取周期信息
     * @param taskManagerId
     * @return
     */
    Map<String, Object> loadCycleInfo(Integer taskManagerId);

    /**
     * 获取小区的所有临时任务
     * @param gridId
     * @return
     */
    List<Map<String, Object>> loadTaskForApp(String gridId);

    /**
     * 获取系统评价信息
     * @param param
     * @return
     */
    List<Map<String,Object>> loadSystemScore(JSONObject param);

    /**
     * 根据管理者的id，获取部门人员排行
     * @param query
     * @return
     */
    List<Map<String, Object>> loadByDeparentAchievements(JSONObject query);

    /**
     * 获取所有周期任务
     * @param query
     * @return
     */
    IPage<Map<String, Object>> loadAllPeriodicTask(JSONObject query);

    /**
     * 修改状态
     * @param taskManager
     */
    void updateStatus(TaskManager taskManager);

    /**
     * 查看接口
     * @param taskManagerId
     */
    TaskManager getTaskManager(Integer taskManagerId);

    /**
     * 获取周期任务最新情况
     * @param taskManagerId
     * @return
     */
    Long loadManagerNew(Integer taskManagerId);
}
