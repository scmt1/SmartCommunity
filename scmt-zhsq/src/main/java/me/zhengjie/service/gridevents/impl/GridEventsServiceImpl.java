package me.zhengjie.service.gridevents.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import me.zhengjie.aop.annotation.InitBaseInfo;
import me.zhengjie.aop.type.InitBaseType;
import me.zhengjie.common.utils.SecurityUtil;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.dto.GridEventsDto;


import me.zhengjie.entity.GridStatistics;
import me.zhengjie.entity.gridevents.EventsType;
import me.zhengjie.global.GridTree;
import me.zhengjie.mapper.DictionaryMapper;
import me.zhengjie.mapper.gridevents.EventsTypeMapper;
import me.zhengjie.mapper.gridevents.GridEventsMapper;
import me.zhengjie.mapper.gridevents.GridHandleMapper;
import me.zhengjie.mapper.gridevents.GridRecordMapper;
import me.zhengjie.mapper.jpush.PushMsgMapper;
import me.zhengjie.entity.gridevents.GridEvents;
import me.zhengjie.entity.gridevents.GridHandle;
import me.zhengjie.entity.gridevents.GridRecord;
import me.zhengjie.entity.jpush.PushMsg;
import me.zhengjie.service.attendance.IAttendanceRecordService;
import me.zhengjie.service.gridevents.IGridEventsService;
import me.zhengjie.service.jpush.IPushMsgService;
import me.zhengjie.service.jpush.impl.PushMsgServiceImpl;
//import me.zhengjie.ws.MsgRoute;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.util.BusinessErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class GridEventsServiceImpl extends ServiceImpl<GridEventsMapper, GridEvents> implements IGridEventsService {

    private final GridEventsMapper gridEventsMapper;

    private final GridRecordMapper gridRecordMapper;

    private final GridHandleMapper gridHandleMapper;

    private final DictionaryMapper dictionaryMapper;

    private final  IAttendanceRecordService attendanceRecordService;
//    private final IAttendanceRecordService userClient;

    private final GridTree gridTree;

//    private final MsgRoute msgRoute;

    private final IPushMsgService pushMsgService;

    private final PushMsgMapper pushMsgMapper;


    private final SecurityUtil securityUtil;

    private final EventsTypeMapper eventsTypeMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
//    @InitBaseInfo(type = InitBaseType.ADD)
    public void add(GridEvents gridEvents) throws Exception {

//        JSONObject jsonObject = userClient.currentUser();
//        Integer userType = jsonObject.getJSONObject("data").getInteger("type");
        Integer userType = 1;
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        if (userType == 1) {
            gridEvents.setDataFrom(1);
            gridEvents.setStatus(1);
        } else {
            gridEvents.setDataFrom(2);
            gridEvents.setStatus(2);
        }
        gridEvents.setCode(sdf.format(date));

        gridEventsMapper.insert(gridEvents);

        GridRecord gridRecord = new GridRecord();
        gridRecord.setGridEventsId(gridEvents.getId());
        gridRecord.setStatus(1);
//        String userName = userClient.baseUserInfoById(gridEvents.getCreateUser()).getJSONObject("data").getString("realName");
//        gridRecord.setContent(gridEvents.getDataFrom() == 1 ? "??????"+ userName + "??????"+ gridEvents.getTitle() + "??????" : "?????????"  + userName + "??????"+ gridEvents.getTitle() + "??????");
        gridRecord.setKeyWords(gridEvents.getTitle());
        gridRecord.setCreateUser(gridEvents.getCreateUser());
        gridRecord.setCreateTime(date);
        gridRecordMapper.insert(gridRecord);

        //????????????
        PushMsg pushMsg = new PushMsg();
        pushMsg.setGridId(gridEvents.getGridId());
        pushMsg.setAppType(3);
        pushMsg.setDetailsId(gridEvents.getId());
        JSONObject temp = new JSONObject();
        temp.put("grid",gridEvents.getGridId());
        temp.put("fileType","2,3");
        String userIds = attendanceRecordService.getUserIds(temp);
        pushMsg.setUserIds(userIds);
        Long messageId = pushMsgService.add(pushMsg);
        try {
            if (StringUtils.isNotEmpty(userIds)){
                JSONObject params = new JSONObject();
                params.put("userIds",userIds);
                JSONObject param = new JSONObject();
                String[] split = userIds.split(",");
                param.put("userId", split[0]);
                param.put("messageId", messageId);
                Map<String, Object> map = pushMsgMapper.loadOneMsgInWeb(param);
                params.put("msg",map);
//                msgRoute.routeMsg(params);
            }
        }catch (Exception e){
            log.info("????????????:"+e.getMessage());
        }





    }

    @Override
    @InitBaseInfo(type = InitBaseType.UPDATE)
    public void modify(GridEvents gridEvents) {
        gridEventsMapper.updateById(gridEvents);
    }

    @Override
    public GridEvents loadOne(Integer gridEventsId) {
        return gridEventsMapper.selectById(gridEventsId);
    }

    @Override
    public GridEvents getOne(Integer gridEventsId) {
        return gridEventsMapper.getOne(gridEventsId.longValue());
    }

    @Override
    public void delete(Integer gridEventsId) {
        gridEventsMapper.deleteById(gridEventsId);
        gridRecordMapper.delete(new QueryWrapper<GridRecord>().lambda()
                .eq(GridRecord::getGridEventsId, gridEventsId));
        gridHandleMapper.delete(new QueryWrapper<GridHandle>().lambda()
                .eq(GridHandle::getGridEventsId, gridEventsId));
    }

    @Override
    public IPage<GridEvents> loadAllByQuery(JSONObject query) {
        Page<GridEvents> page = new Page<>(query.getLongValue("pageNum"), query.getLongValue("pageSize"));
        String status = query.getString("status");
        Integer userId = 1;
        Integer type = 0;
        if (type == 1) {
            query.put("createUser", userId);
        }

        if (StringUtils.isNotEmpty(status)) {
            String[] split = status.split(",");
            List<Long> ids = new ArrayList<>();
            for (int i = 0; i < split.length; i++) {
                Long temp = Long.valueOf(split[i]);
                ids.add(temp);
            }
            query.put("statusList", ids);
        }

        IPage<GridEvents> gridEventsIPage = gridEventsMapper.loadAllByQuery(query,page);
        if(gridEventsIPage != null && gridEventsIPage.getRecords() != null && gridEventsIPage.getRecords().size() > 0){
            for (int i = 0; i < gridEventsIPage.getRecords().size(); i++) {
                GridEvents gridEvents = gridEventsIPage.getRecords().get(i);
                if(gridEvents != null){
                    if(gridEvents.getEventsTypes() != null && StringUtils.isNotEmpty(gridEvents.getEventsTypes().getName())){
                        gridEvents.setEventsTypeName(gridEvents.getEventsTypes().getName());
                    }
                    if(gridEvents.getBasicGrid() != null && StringUtils.isNotEmpty(gridEvents.getBasicGrid().getName())){
                        gridEvents.setGridName(gridEvents.getBasicGrid().getName());
                    }
                }
            }
        }

        return gridEventsIPage;
    }

    @Override
    public List<Map<String, Object>> getAllStatus(JSONObject query) {
//        JSONObject jsonObject = userClient.currentUser();
//        Integer integer = jsonObject.getJSONObject("data").getInteger("type");
//        Integer userId = jsonObject.getJSONObject("data").getInteger("id");

        Integer integer = 1;
        Integer userId = 8;
        if (integer == 1) {    //??????
            query.put("createUser", userId);
        }
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> map = gridEventsMapper.getAllStatus(query);
        Map<String, Object> temp1 = new HashMap<>();
        temp1.put("name", "??????");
        temp1.put("count", map.get("totalCount"));
        temp1.put("status", "");
        result.add(temp1);

        Map<String, Object> temp2 = new HashMap<>();
        temp2.put("name", "?????????");
        temp2.put("count", map.get("beReceived"));
        temp2.put("status", "1");
        result.add(temp2);

        Map<String, Object> temp3 = new HashMap<>();
        temp3.put("name", "?????????");
        temp3.put("count", map.get("todo"));
        temp3.put("status", "3");
        result.add(temp3);

        Map<String, Object> temp8 = new HashMap<>();
        temp8.put("name", "?????????");
        temp8.put("count", map.get("toDispatch"));
        temp8.put("status", "2");
        result.add(temp8);

        if (integer != 1) {    //????????????
            Map<String, Object> temp4 = new HashMap<>();
            temp4.put("name", "?????????");
            temp4.put("count", map.get("toExamine"));
            temp4.put("status", "4,5,9");
            result.add(temp4);
        }

        Map<String, Object> temp9 = new HashMap<>();
        temp9.put("name", "?????????");
        temp9.put("count", map.get("toUserEnd"));
        temp9.put("status", "6,0");      //?????????
        result.add(temp9);

        Map<String, Object> temp5 = new HashMap<>();
        temp5.put("name", "?????????");
        temp5.put("count", map.get("toEnd"));
        temp5.put("status", "6,-1");     //?????????
        result.add(temp5);

        Map<String, Object> temp6 = new HashMap<>();
        temp6.put("name", "?????????");
        temp6.put("count", map.get("twoExamine"));
        temp6.put("status", "7");
        result.add(temp6);

        Map<String, Object> temp7 = new HashMap<>();
        temp7.put("name", "?????????");
        temp7.put("count", map.get("isEnd"));
        temp7.put("status", "8");
        result.add(temp7);

        return result;
    }

    @Override
    public GridEvents getOrderDetails(Long gridEventsId) {

        GridEvents gridEvents = gridEventsMapper.getOne(gridEventsId);
        try {
            GridTree.Record gridInfomation = gridTree.getGridInfomation(gridEvents.getGridId());
            gridEvents.setGridName(gridInfomation.getName());
            gridEvents.setCommunityName(gridInfomation.getCommunityName());
            gridEvents.setStreetName(gridInfomation.getStreetName());
        } catch (Exception e) {
            gridEvents.setGridName("");
            gridEvents.setCommunityName("");
            gridEvents.setStreetName("");
        }

        if (gridEvents.getExecutor() != null) {
        }
        return gridEvents;
    }

    @Override
    public List<Map<String, Object>> getStatistics(JSONObject query) {
        return gridEventsMapper.getStatistics(query);
    }
    @Override
    public List<Map<String, Object>> getStatisticsPage() {
        return gridEventsMapper.getStatisticsPage();
    }

    @Override
    public GridEvents statisticsEvent(SearchVo searchVo,JSONObject query) {
        GridEvents gridEvents = new GridEvents();

        //??????
        Integer allCount = gridEventsMapper.statisticsEvent(0,searchVo,query);
        gridEvents.setAllCount(allCount);
        //?????????
        Integer waitCount = gridEventsMapper.statisticsEvent(1,searchVo,query);
        gridEvents.setWaitCount(waitCount);
        //?????????
        Integer finishCount = gridEventsMapper.statisticsEvent(2,searchVo,query);
        gridEvents.setFinishCount(finishCount);
        //?????????
        Integer overtimeCount = gridEventsMapper.statisticsEvent(3,searchVo,query);
        gridEvents.setOvertimeCount(overtimeCount);

        return gridEvents;
    }

    @Override
    public List<GridEvents> echartEvent(SearchVo searchVo,JSONObject query) {
        List<EventsType> eventsTypes = eventsTypeMapper.selectList(new QueryWrapper<EventsType>().lambda()
                .notIn(EventsType::getIsDeleted, 1));
        //allCount
        List<GridEvents> echartEvents = new ArrayList<>();
        for(EventsType eventsType : eventsTypes) {
            //??????????????? ?????????????????????(??????????????????????????????????????????)
            query.put("eventsTypeId",eventsType.getId().intValue());
            GridEvents gridEvents = statisticsEvent(searchVo,query);
            //?????????????????????
            gridEvents.setEventsTypeName(eventsType.getName());
            /*//?????????????????? ????????????
            Integer in = gridEventsMapper.numbertEvent(eventsType.getId().intValue(),query);
            gridEvents.setAllCount(in);*/
            //???????????????
            echartEvents.add(gridEvents);
        }
        return echartEvents;
    }
    @Override
    public GridStatistics gridStatistics(SearchVo searchVo,JSONObject query) {
        return gridEventsMapper.gridStatistics(searchVo,query);
    }

    @Override
    public List<GridEvents> loadByQuery(JSONObject query) {
        return gridEventsMapper.loadByQuery(query);
    }

    @Override
    public Map<String, Object> getBigData(JSONObject query) {
        Map<String, Object> redaData =new HashMap<>();

        Map<String, Object> allStatus = gridEventsMapper.getAllStatus(query);
        redaData.put("allStatus",allStatus);     //?????????

        query.put("dataFrom",1);              //?????????
        List<Map<String, Object>> resident = gridEventsMapper.getBigData(query);
        redaData.put("resident",resident);

        query.put("dataFrom",2);                    //?????????
        List<Map<String, Object>> streetSide = gridEventsMapper.getBigData(query);
        redaData.put("streetSide",streetSide);


        return redaData;
    }

    public List<Map<String, Object>> getEventProcessing(Long gridEventsId) {
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        GridEvents gridEvents = gridEventsMapper.selectById(gridEventsId);
        List<Map<String, Object>> eventProcessingList = new ArrayList<>();
        List<GridHandle> gridHandles = gridHandleMapper.selectList(new QueryWrapper<GridHandle>().lambda()
                .eq(GridHandle::getGridEventsId, gridEventsId)
                .orderByDesc(GridHandle::getCreateTime)
                .orderByDesc(GridHandle::getId));
        for (GridHandle gridHandle : gridHandles) {
            //?????????1??????????????????2??????????????????3????????????????????????4????????????????????????5????????????????????????6???????????????,7???????????????
            Map<String, Object> map = new HashMap<>();
            if (gridHandle.getStatus() == 1) {
                map.put("name", "????????????");
                map.put("date", "");
                List<Map<String, Object>> datas = new ArrayList<>();
                Map<String, Object> map1 = new HashMap<>();
                map1.put("name", "?????????");
//                map1.put("content", userClient.baseUserInfoById(gridHandle.getCreateUser()).getJSONObject("data").getString("realName"));
                datas.add(map1);
                Map<String, Object> map2 = new HashMap<>();
                map2.put("name", "?????????");
                String remark = "";
                if (gridHandle.getBeUserId() == 0) {
                    remark = "?????????";
                } else if (gridHandle.getBeUserId() == -1) {
                    remark = "???????????????";
                } else {
//                    remark = userClient.baseUserInfoById(gridHandle.getBeUserId()).getJSONObject("data").getString("realName");
                }
                map2.put("content", remark);
                datas.add(map2);
                Map<String, Object> map3 = new HashMap<>();
                map3.put("name", "????????????");
                map3.put("content", simple.format(gridHandle.getCreateTime()));
                datas.add(map3);
                Map<String, Object> map4 = new HashMap<>();
                map4.put("name", "??????");
                map4.put("content", gridHandle.getRemark());
                datas.add(map4);
                map.put("result", datas);
            } else if (gridHandle.getStatus() == 2) {
                map.put("name", "????????????");
                map.put("date", simple.format(gridHandle.getCreateTime()));
                List<Map<String, Object>> datas = new ArrayList<>();
                Map<String, Object> map1 = new HashMap<>();
                map1.put("name", "?????????????????????");
//                map1.put("content", userClient.baseUserInfoById(gridHandle.getCreateUser()).getJSONObject("data").getString("realName"));
//                map1.put("phone", userClient.baseUserInfoById(gridHandle.getCreateUser()).getJSONObject("data").getString("phone"));
                datas.add(map1);

                if (gridHandle.getOrderStatus() ==null || gridHandle.getOrderStatus() !=9){
                    Map<String, Object> map3 = new HashMap<>();
                    map3.put("name", "??????????????????");
                    map3.put("content", "");
                    map3.put("audioMediaAddress", StringUtils.isEmpty(gridEvents.getAudioMediaAddress()) ? "" : gridEvents.getAudioMediaAddress());
                    map3.put("mediaAddress", StringUtils.isEmpty(gridEvents.getMediaAddress()) ? "" : gridEvents.getMediaAddress());
                    datas.add(map3);
                    Map<String, Object> map4 = new HashMap<>();
                    map4.put("name", "??????????????????");
                    map4.put("content", "");
                    map4.put("audioMediaAddress", StringUtils.isEmpty(gridHandle.getAudioMediaAddress()) ? "" : gridHandle.getAudioMediaAddress());
                    map4.put("mediaAddress", StringUtils.isEmpty(gridHandle.getMediaAddress()) ? "" : gridHandle.getMediaAddress());
                    datas.add(map4);
                }
                Map<String, Object> map5 = new HashMap<>();
                map5.put("name", "????????????");
                map5.put("content", gridHandle.getRemark());
                datas.add(map5);
                map.put("result", datas);
            } else if (gridHandle.getStatus() == 3) {
                map.put("name", "??????????????????");
                map.put("date", simple.format(gridHandle.getCreateTime()));
                List<Map<String, Object>> datas = new ArrayList<>();
                Map<String, Object> map1 = new HashMap<>();
                map1.put("name", "?????????????????????");
//                map1.put("content", userClient.baseUserInfoById(gridHandle.getCreateUser()).getJSONObject("data").getString("realName"));
//                map1.put("phone", userClient.baseUserInfoById(gridHandle.getCreateUser()).getJSONObject("data").getString("phone"));
                datas.add(map1);
                Map<String, Object> map3 = new HashMap<>();
                map3.put("name", "??????????????????");
                map3.put("content", "");
                map3.put("audioMediaAddress", StringUtils.isEmpty(gridHandle.getAudioMediaAddress()) ? "" : gridHandle.getAudioMediaAddress());
                map3.put("mediaAddress", StringUtils.isEmpty(gridHandle.getMediaAddress()) ? "" : gridHandle.getMediaAddress());
                datas.add(map3);

                Map<String, Object> map4 = new HashMap<>();
                map4.put("name", "??????????????????");
                if (gridHandle.getReviewStatus() == 1) {
                    map4.put("content", gridHandle.getRemark());
                }
                datas.add(map4);
                Map<String, Object> map5 = new HashMap<>();
                map5.put("name", "??????????????????");
                if (gridHandle.getReviewStatus() == 1) {
                    map5.put("content", "????????????");
                } else {
                    map5.put("content", "????????????");
                }
                datas.add(map5);
                map.put("result", datas);
            } else if (gridHandle.getStatus() == 4) {
                map.put("name", "??????????????????");
                map.put("date", simple.format(gridHandle.getCreateTime()));
                List<Map<String, Object>> datas = new ArrayList<>();
                Map<String, Object> map1 = new HashMap<>();
                map1.put("name", "?????????");
//                map1.put("content", "??????" + userClient.baseUserInfoById(gridHandle.getCreateUser()).getJSONObject("data").getString("realName"));
                datas.add(map1);
                Map<String, Object> map2 = new HashMap<>();
                map2.put("name", "??????");
                map2.put("content", gridHandle.getRemark());
                datas.add(map2);
                map.put("result", datas);
            } else if (gridHandle.getStatus() == 5) {
                map.put("name", "????????????");
                map.put("date", simple.format(gridHandle.getCreateTime()));
                List<Map<String, Object>> datas = new ArrayList<>();
                Map<String, Object> map1 = new HashMap<>();
                map1.put("name", "?????????????????????");
//                map1.put("content", userClient.baseUserInfoById(gridHandle.getCreateUser()).getJSONObject("data").getString("realName"));
//                map1.put("phone", userClient.baseUserInfoById(gridHandle.getCreateUser()).getJSONObject("data").getString("phone"));
                datas.add(map1);
                Map<String, Object> map3 = new HashMap<>();
                map3.put("name", "????????????");
                map3.put("content", "????????????????????????");
                datas.add(map3);
                map.put("result", datas);
            } else if (gridHandle.getStatus() == 6) {
                map.put("name", "?????????????????????");
                map.put("date", simple.format(gridHandle.getCreateTime()));
                List<Map<String, Object>> datas = new ArrayList<>();
                Map<String, Object> map1 = new HashMap<>();
                map1.put("name", "???????????????");
                map1.put("content", gridHandle.getStarRating().toString());
                datas.add(map1);
                Map<String, Object> map2 = new HashMap<>();
                map2.put("name", "????????????");
                map2.put("content", gridHandle.getRemark());
                datas.add(map2);
                map.put("result", datas);
            } else if (gridHandle.getStatus() == 7) {
                map.put("name", "??????????????????");
                map.put("date", simple.format(gridHandle.getCreateTime()));
                List<Map<String, Object>> datas = new ArrayList<>();
                Map<String, Object> map1 = new HashMap<>();
                map1.put("name", "??????????????????");
                map1.put("content", gridHandle.getReviewStatus() == 1 ? "??????" : "??????");
                datas.add(map1);
                Map<String, Object> map2 = new HashMap<>();
                map2.put("name", "??????????????????");
                map2.put("content", gridHandle.getRemark());
                datas.add(map2);
                map.put("result", datas);
            }else {
                map.put("name", "??????????????????");
                map.put("date", "");
                List<Map<String, Object>> datas = new ArrayList<>();
                Map<String, Object> map1 = new HashMap<>();
                map1.put("name", "?????????");
//                map1.put("content", userClient.baseUserInfoById(gridHandle.getCreateUser()).getJSONObject("data").getString("realName"));
                datas.add(map1);
                Map<String, Object> map2 = new HashMap<>();
                map2.put("name", "?????????");
                String remark = "";
                if (gridHandle.getBeUserId() == 0) {
                    remark = "?????????";
                } else if (gridHandle.getBeUserId() == -1) {
                    remark = "???????????????";
                } else {
//                    remark = userClient.baseUserInfoById(gridHandle.getBeUserId()).getJSONObject("data").getString("realName");
                }
                map2.put("content", remark);
                datas.add(map2);
                Map<String, Object> map3 = new HashMap<>();
                map3.put("name", "????????????");
                map3.put("content", simple.format(gridHandle.getCreateTime()));
                datas.add(map3);
                Map<String, Object> map4 = new HashMap<>();
                map4.put("name", "??????");
                map4.put("content", gridHandle.getRemark());
                datas.add(map4);
                map.put("result", datas);
            }
            eventProcessingList.add(map);
        }
        return eventProcessingList;
    }

}
