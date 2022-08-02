package me.zhengjie.mapper.gridevents;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.entity.TComponentmanagement;
import me.zhengjie.entity.gridevents.EventsType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface EventsTypeMapper extends BaseMapper<EventsType> {


    IPage<EventsType> loadAllByQuery(Page<Map<String, Object>> page, @Param("query") JSONObject query);

    Page<EventsType> loadByPage(@Param("page") Page page, @Param("query") JSONObject query);

    List<EventsType> loadNotPage(@Param("query") JSONObject query);

    List<EventsType> selectTreeByParentId(@Param("parentId") String parentId);
}
