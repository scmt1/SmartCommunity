package me.zhengjie.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.zhengjie.entity.DeptType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface DeptTypeMapper extends BaseMapper<DeptType> {

    @Select("select id,name from grid_dept_type")
    List<JSONObject> loadAll4Select();
}
