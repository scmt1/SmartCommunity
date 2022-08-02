package me.zhengjie.mapper.task;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface TaskScoreMapper {


    IPage<Map<String,Object>> selectByQuery(Page<Map<String, Object>> page, JSONObject query);

    @Select("select score from task_score_details tsd where FIND_IN_SET(id,#{taskScoreIds})")
    List<Integer> loadScoreByIds(String taskScoreIds);
}
