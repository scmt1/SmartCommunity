package me.zhengjie.service.attendance;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.dto.AttendanceRecordDto;
import me.zhengjie.entity.attendance.AttendanceRecord;

import java.text.ParseException;
import java.util.List;
import java.util.Map;


public interface IAttendanceRecordService extends IService<AttendanceRecord> {

    /**
     * 新增考勤记录
     * @param attendanceRecord
     */
    void add(AttendanceRecord attendanceRecord);

    /**
     * 修改考勤记录
     * @param attendanceRecord
     */
    void modify(AttendanceRecord attendanceRecord);

    /**
     * 查询考勤记录
     * @param attendanceRecordId
     * @return
     */
    AttendanceRecord loadOne(Integer attendanceRecordId);

    /**
     * 删除考勤记录
     * @param attendanceRecordId
     */
    void delete(Integer attendanceRecordId);

    /**
     * 根据条件查找考勤记录
     * @param query
     * @return
     */
    IPage<AttendanceRecordDto> loadAllByQuery(JSONObject query);

    /**
     * 获取本周的数据
     * @param query
     * @return
     */
    List<Map<String, Object>> getCurrentWeekData(JSONObject query) throws ParseException;

    /**
     * 批量添加
     * @param query
     * @return
     */
    void listAdd(JSONObject query) throws ParseException;

    /**
     * 获取网格下，所有网格员
     * @param query
     * @return
     */
    List<Map<String, Object>> getArchivesList(JSONObject query);

    /**
     * 获取网格下，员工ids
     * @param query
     * @return
     */
    String getUserIds(JSONObject query);
}
