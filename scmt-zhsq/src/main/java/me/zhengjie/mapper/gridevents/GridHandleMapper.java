package me.zhengjie.mapper.gridevents;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.entity.gridevents.GridHandle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;


@Repository
@Mapper
public interface GridHandleMapper extends BaseMapper<GridHandle> {


    IPage<Map<String, Object>> loadAllByQuery(Page<Map<String, Object>> page, @Param("query") JSONObject query);
}
