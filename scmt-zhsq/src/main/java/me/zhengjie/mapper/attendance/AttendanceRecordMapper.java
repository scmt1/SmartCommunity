package me.zhengjie.mapper.attendance;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.dto.AttendanceRecordDto;
import me.zhengjie.entity.attendance.AttendanceRecord;
import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface AttendanceRecordMapper extends BaseMapper<AttendanceRecord> {


    IPage<AttendanceRecordDto> loadAllByQuery(Page<Map<String, Object>> page,@Param("query")JSONObject query);

    List<Map<String,Object>> getUsers(@Param("query")JSONObject query);

    List<Map<String, Object>> getArchivesList(@Param("query")JSONObject query);

    String getUserIds(@Param("query")JSONObject query);

    String getPostName(@Param("personId")String personId);
}
