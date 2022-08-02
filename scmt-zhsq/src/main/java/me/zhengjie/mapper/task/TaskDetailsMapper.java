package me.zhengjie.mapper.task;

import com.alibaba.fastjson.JSONObject;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.dto.TaskDetailsDto;
import me.zhengjie.entity.task.TaskDetails;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface TaskDetailsMapper extends BaseMapper<TaskDetails> {

    List<JSONObject> selectListByMaster(@Param("taskManagerId") Integer taskManagerId);

    Map<String, Object> loadOneDetails(@Param("taskManagerId") Integer taskManagerId);

    Map<String, Object> loadForApp(int id);

    TaskDetailsDto loadDateForApp(Integer id);

    IPage<Map<String, Object>> selectByQuery(Page<Map<String, Object>> page, @Param("query") JSONObject query);

    IPage<Map<String, Object>> loadAllForApp(Page<Map<String, Object>> page,@Param("query") JSONObject query);

    int loadCountForApp(@Param("query")JSONObject query);

    List<Map<String, Object>> taskStatistics(@Param("query")JSONObject query);

    List<Map<String, Object>> getBigData(@Param("query")JSONObject query);

    Map<String, Object> getAllStatistics(@Param("query")JSONObject query);
}
