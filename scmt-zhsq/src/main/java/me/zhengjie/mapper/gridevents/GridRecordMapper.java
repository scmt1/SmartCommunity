package me.zhengjie.mapper.gridevents;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.entity.gridevents.GridEvents;
import me.zhengjie.entity.gridevents.GridRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
@Mapper
public interface GridRecordMapper extends BaseMapper<GridRecord> {


    IPage<Map<String, Object>> loadAllByQuery(Page<Map<String, Object>> page, @Param("query") JSONObject query);

    List<GridRecord> getRecordListData(@Param("gridEventsId")Long gridEventsId);
}
