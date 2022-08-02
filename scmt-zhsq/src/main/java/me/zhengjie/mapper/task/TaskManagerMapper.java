package me.zhengjie.mapper.task;

import com.alibaba.fastjson.JSONObject;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.dto.TaskDetailsDto;
import me.zhengjie.entity.task.TaskManager;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ljj
 * @since 2019-05-06
 */
@Repository
@Mapper
public interface TaskManagerMapper extends BaseMapper<TaskManager> {

    IPage<Map<String, Object>> selectByQuery(Page<Map<String, Object>> page, JSONObject query);

    List<Map<String, Object>> loadByQuery(Map map);

    List<Map<String, Object>> loadByAchievements(Map map);

    Map<String, Object> loadViewingTotal(JSONObject query);

    Map<String, Object> loadViewingTotalByUser(JSONObject query);

    List<Map<String, Object>> loadTaskByUser(Map map);

    Map<String, Object> selectOneById(@Param("taskManagerId") Integer taskManagerId);

    Map<String, Object> loadCycleInfo(@Param("taskManagerId") Integer taskManagerId);

    List<Map<String, Object>> loadTaskForApp(String gridId);

    int loadCount(int id);

    TaskDetailsDto loadDateForApp(Integer id);

    List<Map<String, Object>> loadByDeparentAchievements(JSONObject query);

    IPage<Map<String, Object>> loadAllPeriodicTask(Page<Map<String, Object>> page, @Param("query")JSONObject query);

    TaskManager getTaskManager(@Param("taskManagerId")Integer taskManagerId);
}
