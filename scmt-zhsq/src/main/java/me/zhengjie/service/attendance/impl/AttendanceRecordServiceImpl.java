package me.zhengjie.service.attendance.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.dto.AttendanceRecordDto;

import me.zhengjie.global.GridTree;
import me.zhengjie.mapper.attendance.AttendanceRecordMapper;
import me.zhengjie.entity.attendance.AttendanceRecord;

import me.zhengjie.service.attendance.IAttendanceRecordService;

import lombok.AllArgsConstructor;
import me.zhengjie.util.BusinessErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@AllArgsConstructor
public class AttendanceRecordServiceImpl extends ServiceImpl<AttendanceRecordMapper, AttendanceRecord> implements IAttendanceRecordService {

    private final AttendanceRecordMapper attendanceRecordMapper;


    private final GridTree gridTree;


    @Override
    public void add(AttendanceRecord attendanceRecord) {

        attendanceRecordMapper.insert(attendanceRecord);
    }

    @Override
    public void modify(AttendanceRecord attendanceRecord) {
        attendanceRecord.setUpdateTime(new Date());
        attendanceRecordMapper.updateById(attendanceRecord);
    }

    @Override
    public AttendanceRecord loadOne(Integer attendanceRecordId) {
        return attendanceRecordMapper.selectById(attendanceRecordId);
    }

    @Override
    public void delete(Integer attendanceRecordId) {
        attendanceRecordMapper.deleteById(attendanceRecordId);
    }

    @Override
    public IPage<AttendanceRecordDto> loadAllByQuery(JSONObject query) {
        if (StringUtils.isNotEmpty(query.getString("gridId"))) {
            String gridId = query.getString("gridId");
            List<String> strings = Arrays.asList(gridId.split(","));
            query.put("gridList", strings);
        }
        Page<Map<String, Object>> page = new Page<>(query.getLongValue("pageNum"), query.getLongValue("pageSize"));
        IPage<AttendanceRecordDto> attendanceRecordDtoIPage = attendanceRecordMapper.loadAllByQuery(page, query);
        List<AttendanceRecordDto> records = attendanceRecordDtoIPage.getRecords();
        for (AttendanceRecordDto record : records) {
            String postName = attendanceRecordMapper.getPostName(record.getPersonId());
            record.setJobName(postName);

            StringBuilder communityName = new StringBuilder();
            StringBuilder gridName = new StringBuilder();
            StringBuilder streetName = new StringBuilder();
            String[] split = record.getGridId().split(",");
            for (String s : split) {
                GridTree.Record gridInformation = gridTree.getGridInfomation(s);
                if (StringUtils.isNotEmpty(gridInformation.getCommunityName())) {
                    communityName.append(gridInformation.getCommunityName()).append(",");
                }
                if (StringUtils.isNotEmpty(gridInformation.getName())) {
                    gridName.append(gridInformation.getName()).append(",");
                }
                if (StringUtils.isNotEmpty(gridInformation.getStreetName())) {
                    streetName.append(gridInformation.getStreetName()).append(",");
                }
            }
            if (communityName.length() > 0) {
                communityName.setLength(communityName.length() - 1);
            }
            if (gridName.length() > 0) {
                gridName.setLength(gridName.length() - 1);
            }
            if (streetName.length() > 0) {
                streetName.setLength(streetName.length() - 1);
            }
            record.setCommunityName(communityName.toString());
            record.setGridName(gridName.toString());
            record.setStreetName(streetName.toString());
        }
        return attendanceRecordDtoIPage;
    }

    @Override
    public List<Map<String, Object>> getCurrentWeekData(JSONObject query) throws ParseException {
        String gridId = query.getString("gridId");
        String dateFrom = query.getString("dateFrom");   //?????????
        String dateTo = query.getString("dateTo");       //?????????
        List<Map<String, Object>> result = new ArrayList<>();
        if (gridId == null) {
            throw new BusinessErrorException("?????????????????????");
        }
        if (StringUtils.isNotEmpty(dateFrom) || StringUtils.isNotEmpty(dateTo)) {
            if (StringUtils.isEmpty(dateFrom) || StringUtils.isEmpty(dateTo)) {
                throw new BusinessErrorException("?????????????????????????????????");
            }
        }
        if (StringUtils.isEmpty(dateFrom)) {
            String timeInterval = getTimeInterval(new Date());
            String[] split = timeInterval.split(",");
            dateFrom = split[0];
            dateTo = split[1];
        }
        List<String> dayBetweenDates = getDayBetweenDates(dateFrom, dateTo);
        for (String dayBetweenDate : dayBetweenDates) {
            Map<String, Object> map = new HashMap<>();
            map.put("date", dayBetweenDate);
            map.put("week", getWeekOfDate(dayBetweenDate));
            query.put("date", dayBetweenDate);
            // todo ??????????????????
            List<Map<String, Object>> user = attendanceRecordMapper.getUsers(query);
            map.put("userList", user);
            result.add(map);
        }
        return result;
    }

    @Override
    public void listAdd(JSONObject query) throws ParseException {
        Date curr = new Date();
        JSONArray dates = query.getJSONArray("dates");
        Long createUserId = query.getLongValue("createUserId");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<JSONObject> jsonObjects = dates.toJavaList(JSONObject.class);
        for (JSONObject jsonObject : jsonObjects) {
            String date = jsonObject.getString("date");
            String userIds = jsonObject.getString("userIds");
            Date dateTime = format.parse(date);
            if (StringUtils.isEmpty(userIds)) {
                continue;
            }
            if (StringUtils.isEmpty(date)) {
                throw new BusinessErrorException("????????????");
            }
            String[] split = userIds.split(",");
            ExecutorService pool = Executors.newCachedThreadPool();
            attendanceRecordMapper.delete(new QueryWrapper<AttendanceRecord>().lambda()
                    .eq(AttendanceRecord::getDate, date));
            for (String s : split) {
                Long userId = Long.valueOf(s);
                Runnable run = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            AttendanceRecord attendanceRecord = new AttendanceRecord();
                            attendanceRecord.setDate(dateTime);
                            attendanceRecord.setStatus(1);
                            attendanceRecord.setCreateUser(createUserId);
                            attendanceRecord.setUserId(userId);
                            attendanceRecord.setCreateTime(curr);
                            attendanceRecord.setIsDeleted(0);
                            attendanceRecordMapper.insert(attendanceRecord);
                        } catch (Exception e) {
                            e.getMessage();
                        }
                    }
                };
                pool.execute(run);
            }
            pool.shutdown();
        }


    }

    @Override
    public List<Map<String, Object>> getArchivesList(JSONObject query) {
        if (StringUtils.isNotEmpty(query.getString("gridId"))) {
            String gridId = query.getString("gridId");
            List<String> strings = Arrays.asList(gridId.split(","));
            query.put("gridList", strings);
        }
        List<Map<String, Object>> maps = attendanceRecordMapper.getArchivesList(query);
        return maps;
    }

    @Override
    public String getUserIds(JSONObject query) {
        if (StringUtils.isNotEmpty(query.getString("gridId"))) {
            String gridId = query.getString("gridId");
            List<String> strings = Arrays.asList(gridId.split(","));
            query.put("gridList", strings);
        }
        String userIds = attendanceRecordMapper.getUserIds(query);
        return userIds;
    }

    /**
     * ????????????????????????????????????
     *
     * @param begin
     * @param end
     * @return
     */
    public static List<String> getDayBetweenDates(String begin, String end) throws ParseException {
        List<String> lDate = new ArrayList<String>();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        lDate.add(begin);
        Calendar calBegin = Calendar.getInstance();
        // ??????????????? Date ????????? Calendar ?????????
        calBegin.setTime(sd.parse(begin));
        Calendar calEnd = Calendar.getInstance();
        // ??????????????? Date ????????? Calendar ?????????
        calEnd.setTime(sd.parse(end));
        // ??????????????????????????????????????????
        while (sd.parse(end).after(calBegin.getTime())) {
            // ?????????????????????????????????????????????????????????????????????????????????
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(sd.format(calBegin.getTime()));
        }
        return lDate;
    }

    /**
     * ??????????????????????????????<br>
     *
     * @param date
     * @return ????????????????????????
     */
    public static String getWeekOfDate(String date) throws ParseException {
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = {"?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(simple.parse(date));
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public static String getTimeInterval(Date date) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        cal.setTime(date);
        // ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// ?????????????????????????????????????????????
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // System.out.println("??????????????????:" + sdf.format(cal.getTime())); // ?????????????????????
        // ???????????????????????????????????????????????????????????????????????????????????????
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // ?????????????????????????????????????????????
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // ???????????????????????????????????????????????????????????????????????????????????????
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        String imptimeBegin = sdf.format(cal.getTime());
        // System.out.println("??????????????????????????????" + imptimeBegin);
        cal.add(Calendar.DATE, 6);
        String imptimeEnd = sdf.format(cal.getTime());
        // System.out.println("??????????????????????????????" + imptimeEnd);
        return imptimeBegin + "," + imptimeEnd;
    }


}
