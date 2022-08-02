package me.zhengjie.mapper.iot;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.entity.iot.BuildingIntercom;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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
public interface BuildingIntercomMapper extends BaseMapper<BuildingIntercom> {

    IPage<Map<String, Object>> selectByQuery(Page<Map<String, Object>> page, @Param("query") JSONObject query);
}
