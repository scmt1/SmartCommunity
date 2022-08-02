package me.zhengjie.mapper.gridevents;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.models.auth.In;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.GridStatistics;
import me.zhengjie.entity.gridevents.GridEvents;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface GridEventsMapper extends BaseMapper<GridEvents> {


    IPage<GridEvents> loadAllByQuery( @Param("query")JSONObject query,@Param(value = "page")Page<GridEvents> page);

    Integer statisticsEvent(@Param("decide")Integer decide,@Param("searchVo")SearchVo searchVo,@Param("query")JSONObject query);

    GridStatistics gridStatistics(@Param("searchVo")SearchVo searchVo, @Param("query")JSONObject query);

    //根据事件类型id 统计对应事件的数量
    Integer numbertEvent(@Param("id")Integer id,@Param("query")JSONObject query);

    Map<String, Object> getAllStatus(@Param("query")JSONObject query);

    GridEvents getOne(@Param("gridEventsId")Long gridEventsId);

    List<Map<String, Object>> getStatistics(@Param("query")JSONObject query);

    List<Map<String, Object>> getStatisticsPage();

    List<GridEvents> loadByQuery(@Param("query")JSONObject query);

    List<Map<String, Object>> getBigData(@Param("query")JSONObject query);
}
