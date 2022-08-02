package me.zhengjie.service.task.impl;

import com.alibaba.fastjson.JSONObject;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.common.utils.SecurityUtil;
import me.zhengjie.dto.TaskDetailsDto;


import me.zhengjie.global.GridTree;

import me.zhengjie.mapper.DictionaryMapper;
import me.zhengjie.mapper.task.*;

import me.zhengjie.entity.task.*;

import me.zhengjie.service.task.ITaskDetailsService;
import me.zhengjie.service.task.ITaskScoreService;

import lombok.AllArgsConstructor;
import me.zhengjie.util.*;
import me.zhengjie.util.BusinessErrorException;
import me.zhengjie.util.ThreadLocalUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * @author ljj
 */
@Service
@AllArgsConstructor
public class TaskDetailsServiceImpl extends ServiceImpl<TaskDetailsMapper, TaskDetails> implements ITaskDetailsService {

    private final TaskDetailsMapper taskDetailsMapper;

    private final TaskTimelyScoreMapper taskTimelyScoreMapper;

    private final ITaskScoreService taskScoreService;

    private final TaskCategoryMapper taskCategoryMapper;


    private final GridTree gridTree;

    private final DictionaryMapper dictionaryMapper;

    private final SecurityUtil securityUtil;

    private final Lock refreshLock = new ReentrantLock();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(TaskDetails taskDetails) throws Exception {
        refreshLock.lock();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if (taskDetails.getStatus() == 8) { //此时的id 为设备的id，取消任务需要改变设备的状态
                TaskDetails taskDetails1 = taskDetailsMapper.selectById(taskDetails.getId());
                taskDetails1.setStatus(8);
                taskDetails1.setInvalidDate(new Date());
                taskDetails1.setExecuteUserId("");
                taskDetailsMapper.updateById(taskDetails1);
                return;
            }
            TaskDetails taskDetails1 = taskDetailsMapper.selectById(taskDetails.getId());
            if (taskDetails.getStatus() != null) {
                if (taskDetails.getStatus() == 3) {//驳回拼接进去
                    taskDetails.setStatus(5);
                    if (taskDetails1.getStatus() == 6) {
                        throw new BusinessErrorException("该工单已通过，驳回失败");
                    } else if (taskDetails1.getStatus() == 3) {
                        throw new BusinessErrorException("该工单已被驳回");
                    }

                    if (StringUtils.isEmpty(taskDetails1.getReviewUserId())) {
                        taskDetails.setReviewUserId(taskDetails.getOverruleUserIds());
                        taskDetails.setReviewDate(format.format(new Date()));
                        taskDetails.setOverruleRemark(taskDetails.getOverruleRemark());
                    } else {
                        String s = taskDetails1.getReviewUserId() + "," + taskDetails.getOverruleUserIds();
                        taskDetails.setReviewUserId(s);
                        taskDetails.setReviewDate(taskDetails1.getReviewDate() + "," + format.format(new Date()));
                        taskDetails.setOverruleRemark(taskDetails1.getOverruleRemark() + "#%&" + taskDetails.getOverruleRemark());
                    }
                }
                if (taskDetails.getStatus() == 4 && taskDetails.getFiles() != null) {
                    if (taskDetails.getFiles() != null) {
                        try {
                           // String s = batchUpload(taskDetails.getFiles());
                            String s = "";
                            taskDetails.setCompletePhotos(s);
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        }
                    }
                    if (StringUtils.isEmpty(taskDetails1.getReviewUserId())) {
                        if (StringUtils.isEmpty(taskDetails.getCompletePhotos())) {
                            taskDetails.setCompletePhotos("0");
                        }
                        taskDetails.setCompleteDate(format.format(new Date()));
                        TaskTimelyScore taskTimelyScore = taskTimelyScoreMapper.selectById(1);
                        Date date = new Date();
                        // 系统计算分值，判断状态，再根据时间差异判断是否合格
                        if (taskTimelyScore.getCompleteOrderStatus() == 1) {
                            // 完成时间减去接单时间
                            long diff = (date.getTime() - taskDetails1.getReceiveDate().getTime()) / 1000 / 60;
                            if (taskTimelyScore.getCompleteOrder() >= diff) {
                                String systemScoreNum = taskTimelyScore.getCompleteOrderNum().toString();
                                String systemScoreIds = taskDetails1.getSystemScoreIds();
                                if (org.apache.commons.lang3.StringUtils.isNotEmpty(systemScoreNum)) {
                                    if (org.apache.commons.lang3.StringUtils.isNotEmpty(systemScoreIds)) {
                                        taskDetails.setSystemScoreIds(systemScoreIds + "," + systemScoreNum);
                                    } else {
                                        taskDetails.setSystemScoreIds(systemScoreNum);
                                    }
                                }
                            }
                        }
                    } else {
                        taskDetails.setCompleteDate(taskDetails1.getCompleteDate() + "," + format.format(new Date()));
                        String s = org.apache.commons.lang3.StringUtils.isEmpty(taskDetails.getCompletePhotos()) ? "0" : taskDetails.getCompletePhotos();
                        taskDetails.setCompletePhotos(taskDetails1.getCompletePhotos() + "%" + s);
                        taskDetails.setCompleteRemark(taskDetails1.getCompleteRemark() + "#%&" + taskDetails.getCompleteRemark());
                    }

                }

                if (taskDetails.getStatus() == 6) {
                    if (taskDetails1.getStatus() == 3) {
                        throw new BusinessErrorException("该工单已被驳回，审核失败");
                    } else if (taskDetails1.getStatus() == 6) {
                        throw new BusinessErrorException("该工单已审核通过");
                    }
                    if (StringUtils.isEmpty(taskDetails1.getReviewUserId())) {
                        taskDetails.setReviewUserId(taskDetails.getReviewUserId());
                        taskDetails.setReviewDate(format.format(new Date()));
                    } else {
                        taskDetails.setReviewUserId(taskDetails1.getReviewUserId() + "," + taskDetails.getReviewUserId());
                        taskDetails.setReviewDate(taskDetails1.getReviewDate() + "," + format.format(new Date()));
                    }
                }
                if (taskDetails.getStatus() == 7) {
                    if (taskDetails1.getStatus() == 3) {
                        throw new BusinessErrorException("该工单已被驳回，评价失败");
                    } else if (taskDetails1.getStatus() == 7) {
                        throw new BusinessErrorException("该工单已评价");
                    }
                    if (taskDetails.getFiles() != null) {
                       // OSSUploadUtil.buildFilesAddress(taskDetails, "files", "evaluationPhotos");
                    }
                    taskDetails.setEvaluateDate(new Date());

                }

            }
            taskDetailsMapper.updateById(taskDetails);
            if (taskDetails.getStatus() == 6) {
                //计算工时
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("cood", "RWLB");
                jsonObject.put("detailsId", taskDetails.getId());
                BigDecimal workingHours = getWorkingHours(jsonObject);
                taskDetails.setWorkingHours(workingHours);
                taskDetailsMapper.updateById(taskDetails);
            }

   /*         Map<String, String> map = new HashMap<>();
            map.put("code", "RWLB");
            map.put("type", "1");
            map.put("detailsId", taskDetails.getId().toString());
            map.put("status", taskDetails.getStatus().toString());
            map.put("messageType", "2");
            Integer executeUserId = Integer.valueOf(taskDetails1.getExecuteUserId());
            String userIds = "";*/

            //todo
            //InternalUser internalUser = internalUserMapper.selectById(executeUserId);
          /*  String content = "";
            if (taskDetails.getStatus() == 2 || taskDetails.getStatus() == 7) {
                userIds = executeUserId.toString();
                content = "你的订单已评价";
                jpushService.sendCustomPush(2, "任务列表", content, map, (null == internalUser ? "员工已删除" : internalUser.getPhone()));
            } else if (taskDetails.getStatus() == 5) {
                userIds = taskDetails1.getExecuteUserId().toString();
                jpushService.sendCustomPush(2, "任务列表", "你的工单被驳回", map, (null == internalUser ? "员工已删除" : internalUser.getPhone()));
                content = "你的工单被驳回";
            } else if (taskDetails.getStatus() == 6) {
                userIds = taskDetails1.getExecuteUserId().toString();
                jpushService.sendCustomPush(2, "任务列表", "你的工单已通过", map, (null == internalUser ? "员工已删除" : internalUser.getPhone()));
                content = "你的工单已通过";
            } else {
                if (taskDetails.getStatus() == 3) {
                    content = "员工已接单";
                } else if (taskDetails.getStatus() == 4) {
                    content = "有个新的工单待审核";
                }
                JSONObject jsonObject = new JSONObject();
                List<JSONObject> deptList = departmentService.loadDeptByProperty(taskDetails1.getGridId());
                jsonObject.put("deptList", deptList);
                jsonObject.put("menuCode", "work_dailytask_dispatch");
                List<JSONObject> jsonObjects = internalUserMapper.selectAllAuthUser(jsonObject);
                if (!jsonObjects.isEmpty()) {
                    String[] air = new String[jsonObjects.size()];
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < jsonObjects.size(); i++) {
                        air[i] = (String) jsonObjects.get(i).get("phone");
                        stringBuilder.append(jsonObjects.get(i).get("id"));
                        stringBuilder.append(",");
                    }
                    userIds = stringBuilder.toString();
                    jpushService.sendCustomPush(2, "任务列表", content, map, air);
                }
            }*/
      /*      PushMsg pushMsg = new PushMsg();
            pushMsg.setCreateDate(new Date());
            pushMsg.setAppType(2);
            pushMsg.setDetailsId(taskDetails1.getId());
            pushMsg.setStatus(taskDetails1.getStatus());
            pushMsg.setUserIds(userIds);
            pushMsg.setType(1);
            //    pushMsg.setContent(content);
            pushMsg.setMessageType(2);        //任务列表
            pushMsg.setTitle("任务列表");
            pushMsg.setIsRead(1);
            pushMsgMapper.insert(pushMsg);*/
        } finally {
            refreshLock.unlock();
        }

    }


    @Override
    public TaskDetails loadOne(Integer taskDetailsId) {
        return taskDetailsMapper.selectById(taskDetailsId);
    }

    @Override
    public IPage<Map<String, Object>> loadAllByQuery(JSONObject query) {
        Page<Map<String, Object>> page = new Page<>(query.getLongValue("pageNum"), query.getLongValue("pageSize"));
        return taskDetailsMapper.selectByQuery(page, query);
    }

    @Override
    public void delete(Integer taskScoreDetailsId) {
        taskDetailsMapper.deleteById(taskScoreDetailsId);
    }

    @Override
    public void add(TaskDetails taskDetails) {
        taskDetailsMapper.insert(taskDetails);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dispatch(JSONObject query) {
        Integer id = query.getInteger("id");
        String userId = query.getString("userId");
        Integer urgentType = query.getInteger("urgentType");
        String repairTime = query.getString("repairTime");
        TaskDetails taskDetails = taskDetailsMapper.selectById(id);
        if (taskDetails.getStatus() != null && taskDetails.getStatus() > 2) {//派单
            throw new BusinessErrorException("该设备工单正在进行，派单失败");
        }
        taskDetails.setRepairTime(repairTime);
        if (urgentType != null) {
            taskDetails.setUrgentType(urgentType);
        }
        taskDetails.setExecuteUserId(userId);
        taskDetails.setStatus(2);
        taskDetails.setOperateUserId(ThreadLocalUtil.getUserId());
        taskDetails.setAssignDate(new Date());
        taskDetailsMapper.updateById(taskDetails);
        List<Long> ids = new ArrayList<>();
        String[] split = userId.split(",");
        for (int i = 0; i < split.length; i++) {
            Long temp = Long.valueOf(split[i]);
            ids.add(temp);
        }
 /*       //todo
        String[] phones = new String[3];
        //    phones = internalUserMapper.loadPhonesByUserIds(ids);
        Map<String, String> map = new HashMap<>();
        map.put("code", "RWLB");
        map.put("type", "1");
        map.put("detailsId", taskDetails.getId().toString());
        map.put("status", "2");
        map.put("messageType", "2");
        if (phones != null) {
            jpushService.sendCustomPush(2, "日常任务通知", "你有新的任务", map, phones);
            PushMsg pushMsg = new PushMsg();
            pushMsg.setCreateDate(new Date());
            pushMsg.setAppType(2);
            pushMsg.setDetailsId(taskDetails.getId());
            pushMsg.setUserIds(userId);
            pushMsg.setContent("你有新的任务");
            pushMsg.setMessageType(2);        //任务模块
            pushMsg.setTitle("日常任务通知");
            pushMsg.setIsRead(1);
            pushMsgMapper.insert(pushMsg);
        }*/


    }

    @Override
    public IPage<Map<String, Object>> loadTaskHistory(JSONObject query) {
        Page<Map<String, Object>> page = new Page<>(query.getLongValue("pageNum"), query.getLongValue("pageSize"));
        query.put("status", "6,7");
        IPage<Map<String, Object>> mapIPage = taskDetailsMapper.selectByQuery(page, query);
        for (Map<String, Object> record : mapIPage.getRecords()) {
            Long id = (Long) record.get("id");
            TaskDetails taskDetails = taskDetailsMapper.selectById(id);
            record.put("overrule", 0);
            //     record.put("userInFo",userInFo);
            record.put("totalScore", taskDetails.getTotalScore());
            record.put("evaluationStarLevel", taskDetails.getEvaluationStarLevel());
            if (StringUtils.isNotEmpty(taskDetails.getOverruleRemark())) {
                String[] split = taskDetails.getOverruleRemark().split("#%&");
                record.put("overrule", split.length);
            }
        }
        return mapIPage;
    }

    @Override
    public IPage<Map<String, Object>> loadAllForApp(JSONObject query) {
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        SimpleDateFormat min = new SimpleDateFormat("HH:mm");
        Date nowDate = new Date();
        Page<Map<String, Object>> page = new Page<>(query.getLongValue("pageNum"), query.getLongValue("pageSize"));
        Integer userId = query.getInteger("userId");
//        JSONObject data = userClient.currentUser().getJSONObject("data");
//        String gridId = query.getString("gridId");
        String gridId = "23";
        JSONObject object = new JSONObject();
        object.put("userId", 11);
        object.put("code", "latticecs_paidan");
//        boolean jurisdiction = menuRoleService.isJurisdiction(object);
        boolean jurisdiction = true;
        int characterType = 1;
       /* UserInfo userInfo = userInfoMapper.selectOne(new QueryWrapper<UserInfo>().lambda()
                .eq(UserInfo::getUserId,data.getInteger("id")));
        if (!data.getString("gridId").equals(gridId) ||userInfo.getFileType() ==4){
            characterType =0;
        }*/
        if (!jurisdiction) {
            characterType = 0;
        }
        query.put("gridId", gridId);
        query.put("characterType", characterType);//是否是管理者

        if (StringUtils.isNotEmpty(query.getString("status"))) {
            String statusList = query.getString("status");
            String[] splits = statusList.split(",");
            List<Long> statuss = new ArrayList<>();
            for (int y = 0; y < splits.length; y++) {
                Long temp = Long.valueOf(splits[y]);
                statuss.add(temp);
            }
            query.put("statusList", statuss);
        }
        IPage<Map<String, Object>> list = taskDetailsMapper.loadAllForApp(page, query);
        for (Map<String, Object> map : list.getRecords()) {
            String cood = (String) map.get("cood");//任务类型的编码
            int status = (int) map.get("status");

            Integer id = Integer.valueOf(map.get("id").toString());

            String photo = (String) map.get("photo");
            if (StringUtils.isNotEmpty(photo)) {
                String[] split = photo.split(",");
                map.replace("photo", split[0]);
            } else {
                map.put("photo", "");
            }

            //1.申报，2.接单，3.维修，4.待审核，5.驳回，6.完成，7.评价，8.无效


            if ("RWLB".equals(cood)) { //任务类别
                Map<String, Object> task = taskDetailsMapper.loadForApp(id);
                TaskDetails taskDetails = taskDetailsMapper.selectById(id);
                if (taskDetails.getUrgentType() != null) {
                    String urgentTypeName = dictionaryMapper.loadOneByFieldNameAndNumber(taskDetails.getUrgentType(), "urgentType");
                    map.put("urgentTypeName", urgentTypeName);
                }
                //订单驳回的次数
                String overruleUserIds = (String) task.get("overrule_remark");
                if (StringUtils.isNotEmpty(overruleUserIds)) {
                    String[] split = overruleUserIds.split("#%&");
                    map.put("overrule", split.length);
                } else {
                    map.put("overrule", 0);
                }
                if (status == 8) {
                    map.put("bottom", "已标记无效");
                    if (taskDetails.getInvalidDate() != null) {
                        String format = sdf.format(taskDetails.getInvalidDate());
                        map.put("bottom", format + " " + "已标记无效");
                    }
                }
                if (status == 9) {
                    map.put("bottom", "该工单已搁置");
                }
                try {
                    GridTree.Record gridInfomation = gridTree.getGridInfomation(gridId);
                    map.put("gridName", gridInfomation.getName());
                    map.put("communityName", gridInfomation.getCommunityName());
                    map.put("streetName", gridInfomation.getStreetName());
                } catch (Exception e) {
                    map.put("gridName", "");
                    map.put("communityName", "");
                    map.put("streetName", "");
                }

                map.put("operateUserId", taskDetails.getOperateUserId()); //派单人
                map.put("urgentType", taskDetails.getUrgentType());
                map.put("taskManagerId", taskDetails.getMasterId());
                map.put("executor", taskDetails.getExecuteUserId()); //执行人
                map.put("isSubmit", 1);   //今日是否提交，1是，2否
             /*   if (status > 2 && status < 6) {
                    List<OrderFollowUp> sbwx = orderFollowUpMapper.selectList(new QueryWrapper<OrderFollowUp>().lambda()
                            .eq(OrderFollowUp::getCood, "RWLB")
                            .eq(OrderFollowUp::getDetailsId, id)
                            .eq(OrderFollowUp::getPublisher, Integer.valueOf(taskDetails.getExecuteUserId()))
                            .apply("DATE_FORMAT(create_date,'%Y-%m-%d') = {0}", simple.format(nowDate)));
                    String completeDate = taskDetails.getCompleteDate();
                    if (StringUtils.isNotEmpty(completeDate)) {
                        String[] split = completeDate.split(",");
                        List<String> dates = new ArrayList<>();
                        for (String s : split) {
                            dates.add(s.substring(0, 10));
                        }
                        if (dates.contains(simple.format(nowDate)) || !sbwx.isEmpty()) {
                            map.put("isSubmit", 1);   //今日是否提交，1是，2否
                        }
                    } else {
                        if (!sbwx.isEmpty()) {
                            map.put("isSubmit", 1);   //今日是否提交，1是，2否
                        }
                    }
                }*/

                if (status > 2 && status < 8) {
//                    JSONObject jsonObject = userClient.baseUserInfoById(Long.valueOf(taskDetails.getExecuteUserId()));
//                    JSONObject internalUser = jsonObject.getJSONObject("data");
//                    String realName = (String) internalUser.get("realName");
                    String realName =  securityUtil.getCurrUser().getNickName().toString();
                    if (status == 3) {
                        Date operateDate = taskDetails.getReceiveDate();
                        String date = operateDate.toString().substring(11, 16);
                        String bottom = "";
                        if (userId != Integer.valueOf(taskDetails.getExecuteUserId())) {//管理者
                            bottom = date + "|" + realName + "  " + "已接单";
                        } else {
                            bottom = date + "|" + realName + "  " + "需即刻处理";
                        }
                        map.put("bottom", bottom);
                    }
                    if (status > 3 && status < 7) {
                        String[] split = taskDetails.getCompleteDate().split(",");
                        String completeDate = split[split.length - 1];
                        String date = completeDate.substring(11, 16);
                        String bottom = "";
                        if (userId != Integer.valueOf(taskDetails.getExecuteUserId())) {
                            bottom = date  + "|" + realName + "  " + "已完成";
                        } else {
                            bottom = date + "|" + "辛苦啦，您已完成" + map.get("sType");
                        }
                        map.put("bottom", bottom);
                    }
                    if (status == 5) {
                        String[] split = taskDetails.getCompleteDate().split(",");
                        String completeDate = split[split.length - 1];
                        String date = completeDate.substring(11, 16);
                        String bottom = "";
                        if (userId != Integer.valueOf(taskDetails.getExecuteUserId())) {
                            bottom = date + "|" + realName + "  " + "正在整改中";
                        } else {
                            bottom = date + " " + "工单被驳回，请及时整改";
                        }
                        map.put("bottom", bottom);
                    }
                    if (status == 7) {
                        Date evaluationDate = taskDetails.getEvaluateDate();
                        String date = sdf.format(evaluationDate);
                        String bottom = "";
                        Double evaluationStarLevel = taskDetails.getEvaluationStarLevel();
                        if (userId != Integer.valueOf(taskDetails.getExecuteUserId())) {
                            bottom = date  + "|" + realName;
                        } else {
                            bottom = date + " " + "管理者 已评价";
                        }
                        map.put("bottom", bottom);
                        map.put("evaluationStarLevel", evaluationStarLevel);
                    }
                }
            }


        }

        return list;
    }

    @Override
    public Map<String, Object> loadCountForApp(JSONObject query) {
        Map<String, Object> map = new HashMap<>();
        Integer userId = ThreadLocalUtil.getUserId();
        String gridId = query.getString("gridId");
        query.put("userId", userId);

//        JSONObject data = userClient.currentUser().getJSONObject("data");
        JSONObject object = new JSONObject();
        object.put("userId", userId);
        object.put("code", "latticecs_paidan");
//        boolean jurisdiction = menuRoleService.isJurisdiction(object);
        boolean jurisdiction = true;
//
        int characterType = 1;
       /* UserInfo userInfo = userInfoMapper.selectOne(new QueryWrapper<UserInfo>().lambda()
                .eq(UserInfo::getUserId,data.getInteger("id")));
        if (!data.getString("gridId").equals(gridId) ||userInfo.getFileType() ==4){
            characterType =0;
        }*/
        if (!jurisdiction) {
            characterType = 0;
        }


        query.put("gridId", gridId);
        query.put("characterType", characterType);//是否是管理者
        List<Integer> statuss = new ArrayList<>();
        statuss.add(1);
        statuss.add(2);
        query.put("statusList", statuss);
        int status1 = taskDetailsMapper.loadCountForApp(query);
        map.put("status1", status1);
        List<Integer> statuss2 = new ArrayList<>();
        statuss2.add(3);
        statuss2.add(5);
        query.put("statusList", statuss2);
        int status2 = taskDetailsMapper.loadCountForApp(query);
        map.put("status2", status2);

        List<Integer> statuss3 = new ArrayList<>();
        statuss3.add(4);
        query.put("statusList", statuss3);
        int statuss4 = taskDetailsMapper.loadCountForApp(query);
        map.put("statuss3", statuss4);
        List<Integer> statuss6 = new ArrayList<>();
        statuss6.add(5);
        query.put("statusList", statuss6);
        int statuss5 = taskDetailsMapper.loadCountForApp(query);
        map.put("statuss4", statuss5);
        return map;
    }

    @Override
    public Map<String, Object> loadDateForApp(JSONObject query) {
        //1.申报，2.接单，3.维修，4.待审核，5.驳回，6.完成，7.评价，8.无效
        Map<String, Object> map = new HashMap<>();
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        List<Object> result = new ArrayList<>();
        Integer id = query.getInteger("id");
        Integer noApp = 0;
        if (query.getInteger("noApp") != null) {
            noApp = query.getInteger("noApp");     //1是后台,为了开放查看权限
        }
        Integer status = 0;
//        JSONObject useIn = userClient.currentUser().getJSONObject("data");
        Integer userId = securityUtil.getCurrUser().getId().intValue();
        query.put("userId", userId);
        TaskDetails taskDetails = taskDetailsMapper.selectById(id);

        JSONObject object = new JSONObject();
        object.put("userId", userId);
        object.put("code", "latticecs_paidan");
//        boolean jurisdiction = menuRoleService.isJurisdiction(object);
        boolean jurisdiction = true;
        int characterType = 1;
       /* UserInfo userInfo = userInfoMapper.selectOne(new QueryWrapper<UserInfo>().lambda()
                .eq(UserInfo::getUserId,data.getInteger("id")));
        if (!data.getString("gridId").equals(gridId) ||userInfo.getFileType() ==4){
            characterType =0;
        }*/
        if (!jurisdiction) {
            characterType = 0;
        }
        status = taskDetails.getStatus();
        if (characterType != 1 && status > 2 && !taskDetails.getExecuteUserId().equals(userId.toString()) && noApp != 1) {
            throw new BusinessErrorException("该单已被抢");
        }
        if (status == 8) {
            status = status - 7;
            status += 1;
        }
        if (status == 5) {  //设置为驳回时，展示状态为维修中数据
            status = status - 2;
        }
        GridTree.Record gridInfomation = gridTree.getGridInfomation(taskDetails.getGridId());
        TaskDetailsDto taskDetailsDto = taskDetailsMapper.loadDateForApp(id);
        map.put("code", taskDetailsDto.getCode());
        map.put("status", taskDetailsDto.getStatus());
        map.put("categoryId", taskDetailsDto.getCategoryId());
        map.put("userType", 2);
        map.put("cycleFixed", taskDetails.getCycleFixed());  //是否是周期任务，1是周期，2是临时
        map.put("operateUserId", taskDetails.getOperateUserId()); //派单人
        map.put("executor", taskDetails.getExecuteUserId()); //执行人
        map.put("date",simple.format(taskDetailsDto.getCreateDate()));
        map.put("departmentId", taskDetailsDto.getDepartmentIds());
        map.put("isFollowUp", 1); //是否有跟进，1是，2否
/*        if (taskDetails.getStatus() > 2) {
            List<OrderFollowUp> sbwx = orderFollowUpMapper.selectList(new QueryWrapper<OrderFollowUp>().lambda()
                    .eq(OrderFollowUp::getCood, "RWLB")
                    .eq(OrderFollowUp::getDetailsId, id)
                    .eq(OrderFollowUp::getPublisher, userId));
            if (!sbwx.isEmpty()) {
                map.put("isFollowUp", 1);
            }
        }*/
        if (StringUtils.isNotEmpty(taskDetails.getOverruleRemark())) { //把有驳回的次数放进去
            String[] split = taskDetails.getOverruleRemark().split("#%&");
            map.put("overruleCount", split.length);
        } else {
            map.put("overruleCount", 0);
        }
        if (status > 0 || StringUtils.isNotEmpty(taskDetailsDto.getOverruleUserIds())) {
            List<Object> status1 = new ArrayList<>();
            Map<String, Object> map14 = new HashMap<>();
            map14.put("name", "任务单号");
            map14.put("count", "");
            map14.put("content", taskDetails.getCode());
            map14.put("photo", "");
            status1.add(map14);

            Map<String, Object> map0 = new HashMap<>();
            map0.put("name", "任务类型");
            map0.put("count", "");
            TaskCategory taskCategory = taskCategoryMapper.selectById(taskDetails.getCategoryId());
            map0.put("content", taskCategory.getName());
            map0.put("photo", "");
            status1.add(map0);

            Map<String, Object> map11 = new HashMap<>();
            map11.put("name", "任务属性");
            map11.put("count", "");
            map11.put("content", taskDetails.getCycleFixed() ==1?"周期":"临时");
            map11.put("photo", "");
            status1.add(map11);

            Map<String, Object> map13 = new HashMap<>();
            map13.put("name", "所属街道");
            map13.put("count", "");
            map13.put("content", org.apache.commons.lang3.StringUtils.isEmpty(gridInfomation.getStreetName())?"":gridInfomation.getStreetName());
            map13.put("photo", "");
            status1.add(map13);

            Map<String, Object> map12 = new HashMap<>();
            map12.put("name", "所属社区");
            map12.put("count", "");
            map12.put("content", org.apache.commons.lang3.StringUtils.isEmpty(gridInfomation.getCommunityName())?"":gridInfomation.getCommunityName());
            map12.put("photo", "");
            status1.add(map12);

            Map<String, Object> map15 = new HashMap<>();
            map15.put("name", "所属网格");
            map15.put("count", "");
            map15.put("content", org.apache.commons.lang3.StringUtils.isEmpty(gridInfomation.getName())?"":gridInfomation.getName());
            map15.put("photo", "");
            status1.add(map15);

            Map<String, Object> map1 = new HashMap<>();
            map1.put("name", "紧急程度");
            map1.put("count", "");
            if (StringUtils.isEmpty(taskDetailsDto.getUrgentTypeName())) {
                map1.put("content", "普通");
            } else {
                map1.put("content", taskDetailsDto.getUrgentTypeName());
            }
            map1.put("photo", "");
            status1.add(map1);

            Map<String, Object> map2 = new HashMap<>();
            map2.put("name", "发布人");
            map2.put("content", taskDetailsDto.getPublisher());
            map2.put("photo", "");
            map2.put("count", "");
            status1.add(map2);

            Map<String, Object> map3 = new HashMap<>();
            map3.put("name", "任务描述");
            map3.put("content", taskDetailsDto.getRemark());
            map3.put("photo", taskDetailsDto.getPhotos() == null ? "" : taskDetailsDto.getPhotos());
            map3.put("count", "");
            status1.add(map3);
            if (status > 2) {

                Map<String, Object> map9 = new HashMap<>();
                map9.put("name", "执行人");
                map9.put("count", "");
//                JSONObject jsonObject = userClient.baseUserInfoById(Long.valueOf(taskDetails.getExecuteUserId()));
//                JSONObject data = jsonObject.getJSONObject("data");
//                if (null != data) {
//                    map9.put("content", data.getString("realName") + " " + data.getString("phone"));
//                    map9.put("photo", "");
//                } else {
//                    map9.put("content", "员工已删除");
//                    map9.put("photo", "");
//                }
                status1.add(map9);
                Map<String, Object> map10 = new HashMap<>();
                map10.put("name", "接单时间");
                map10.put("content", simple.format(taskDetails.getReceiveDate()));
                map10.put("photo", "");
                map10.put("count", "");
                status1.add(map10);
            }

            result.add(status1);
        }
        if (StringUtils.isNotEmpty(taskDetails.getOverruleRemark())) {
            String[] split = taskDetails.getOverruleRemark().split("#%&");
            String[] completeDate = taskDetails.getCompleteDate().split(",");  //提交时间
            String[] completePhoto = taskDetails.getCompletePhotos().split("%"); //提交照片
            String[] completeRemark = taskDetails.getCompleteRemark().split("#%&"); //提交描述
            String[] reviewUserId = taskDetails.getReviewUserId().split(",");   //审核人
            String[] reviewDate = taskDetails.getReviewDate().split(",");
            for (int i = 0; i < split.length; i++) {
                List<Object> temp = new ArrayList<>();
                Map<String, Object> map4 = new HashMap<>();
                map4.put("name", "工作内容");
                map4.put("content", completeRemark[i]);
                map4.put("photo", "0".equals(completePhoto[i]) ? "" : completePhoto[i]);
                int y = i + 1;
                map4.put("count", "第" + y + "次驳回");
                temp.add(map4);

                Map<String, Object> map6 = new HashMap<>();
                map6.put("name", "提交时间");
                map6.put("content", completeDate[i].substring(0, 16));
                map6.put("photo", "");
                map6.put("count", "");
                temp.add(map6);

                Map<String, Object> map8 = new HashMap<>();
                map8.put("name", "审核时间");
                map8.put("content", reviewDate[i].substring(0, 16));
                map8.put("photo", "");
                map8.put("count", "");
                temp.add(map8);

                Map<String, Object> map9 = new HashMap<>();
                map9.put("name", "审核人");
                map9.put("count", "");
//                JSONObject jsonObject = userClient.baseUserInfoById(Long.valueOf(reviewUserId[i]));
//                JSONObject data = jsonObject.getJSONObject("data");
//
//                if (null != data) {
//                    map9.put("content",  data.getString("realName") + " " + data.getString("phone"));
//                    map9.put("photo", "");
//                } else {
//                    map9.put("content", "员工已删除");
//                    map9.put("photo", "");
//                }
                temp.add(map9);

                Map<String, Object> map10 = new HashMap<>();
                map10.put("name", "驳回理由");
                map10.put("content", split[i]);
                map10.put("photo", "");
                map10.put("count", "");
                temp.add(map10);
                result.add(temp);
            }
            List<Object> temp = new ArrayList<>();
            if (status == 4 || status == 6) {

                Map<String, Object> map4 = new HashMap<>();
                map4.put("name", "工作内容");
                map4.put("content", completeRemark[completeRemark.length - 1]);
                map4.put("photo", "0".equals(completePhoto[completePhoto.length - 1]) ? "" : completePhoto[completePhoto.length - 1]);
                map4.put("count", "");
                temp.add(map4);

                Map<String, Object> map6 = new HashMap<>();
                map6.put("name", "提交时间");
                map6.put("content", completeDate[completeDate.length - 1].substring(0, 16));
                map6.put("photo", "");
                map6.put("count", "");
                temp.add(map6);
            }
            if (status == 6) {
                Map<String, Object> map8 = new HashMap<>();
                map8.put("name", "审核时间");
                map8.put("content", reviewDate[reviewDate.length - 1].substring(0, 16));
                map8.put("photo", "");
                map8.put("count", "");
                temp.add(map8);

                Map<String, Object> map9 = new HashMap<>();
                map9.put("name", "审核人");
                map9.put("count", "");
//                JSONObject jsonObject = userClient.baseUserInfoById(Long.valueOf(reviewUserId[reviewUserId.length - 1]));
//                JSONObject data = jsonObject.getJSONObject("data");
//                if (null != data) {
//                    map9.put("content",  data.getString("realName") + " " + data.getString("phone"));
//                    map9.put("photo", "");
//                } else {
//                    map9.put("content", "员工已删除");
//                    map9.put("photo", "");
//                }
                temp.add(map9);
            }
            if (!temp.isEmpty()) {
                result.add(temp);
            }
        } else {
            List<Object> temp = new ArrayList<>();
            if (status == 4 || status == 6) {

                Map<String, Object> map4 = new HashMap<>();
                map4.put("name", "工作内容");
                map4.put("content", taskDetails.getCompleteRemark());
                map4.put("photo", "0".equals(taskDetails.getCompletePhotos()) ? "" : taskDetails.getCompletePhotos());
                map4.put("count", "");
                temp.add(map4);

                Map<String, Object> map6 = new HashMap<>();
                map6.put("name", "提交时间");
                map6.put("content", taskDetails.getCompleteDate().substring(0, 16));
                map6.put("photo", "");
                map6.put("count", "");
                temp.add(map6);
            }
            if (status == 6) {
                Map<String, Object> map8 = new HashMap<>();
                map8.put("name", "审核时间");
                map8.put("content", taskDetails.getReviewDate().substring(0, 16));
                map8.put("photo", "");
                map8.put("count", "");
                temp.add(map8);

               Map<String, Object> map9 = new HashMap<>();
                map9.put("name", "审核人");
                map9.put("count", "");
//                JSONObject jsonObject = userClient.baseUserInfoById(Long.valueOf(taskDetails.getReviewUserId()));
//                JSONObject data = jsonObject.getJSONObject("data");
//                if (null != data) {
//                    map9.put("content", data.getString("realName") + " " + data.getString("phone"));
//                    map9.put("photo", "");
//                } else {
//                    map9.put("content", "员工已删除");
//                    map9.put("photo", "");
//                }
                temp.add(map9);
            }
            if (!temp.isEmpty()) {
                result.add(temp);
            }
        }
        if (status > 5 && status < 8) {
            List<Object> temp = new ArrayList<>();
            Map<String, Object> map8 = new HashMap<>();
            map8.put("name", "实际工时");
            map8.put("content", taskDetails.getWorkingHours() + "小时");
            map8.put("photo", "");
            map8.put("count", "");
            temp.add(map8);
            result.add(temp);
        }
        if (status == 7) {
            List<Object> status3 = new ArrayList<>();

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("detailsId", taskDetails.getId());
            jsonObject.put("gridId", taskDetails.getGridId());
            List<Map<String, Object>> maps = loadSystemScore(jsonObject);
            Integer systemScoreDetail = 0;
            for (Map<String, Object> mp : maps) {
                Integer mstatus = Integer.valueOf(mp.get("status").toString());
                int score = (int) mp.get("score");
                if (mstatus == 1) {
                    systemScoreDetail += score;
                }
            }
            int i = systemScoreDetail; //个人应得总分
            Map<String, Object> map10 = new HashMap<>();
            map10.put("name", "系统打分");
            map10.put("content", i + "分值)");
            map10.put("count", "");
            map10.put("photo", "");
            map10.put("systemScore", maps);
            status3.add(map10);

            Map<String, Object> map6 = new HashMap<>();
            map6.put("name", "评价内容");
            map6.put("content", taskDetails.getEvaluationRemark());
            map6.put("photo", taskDetails.getEvaluationPhotos());
            map6.put("count", "");
            status3.add(map6);
            result.add(status3);
        }
        map.put("result", result);

        return map;
    }

    @Override
    public void changeUrgency(TaskDetails taskDetails) {
        taskDetailsMapper.updateById(taskDetails);
    }

    @Override
    public void reminder(JSONObject query) {
        String userIds = query.getString("userIds");
        Integer id = query.getInteger("id");
        TaskDetails taskDetails = taskDetailsMapper.selectById(id);
        if (taskDetails.getStatus() != 3) {
            throw new BusinessErrorException("该工单不在处理中，无法进行催单");
        }
     /*   Map<String, String> map = new HashMap<>();
        map.put("code", "RWLB");
        map.put("type", "1");
        map.put("detailsId", taskDetails.getId().toString());
        map.put("status", taskDetails.getStatus().toString());
        map.put("messageType", "2");
        List<Long> ids = new ArrayList<>();
        String[] split = userIds.split(",");
        for (int i = 0; i < split.length; i++) {
            Long temp = Long.valueOf(split[i]);
            ids.add(temp);
        }
        //todo 拉取phones
        String[] phones =new String[3];
       // String[] phones = internalUserMapper.loadPhonesByUserIds(ids);
        jpushService.sendCustomPush(2, "日常任务通知", "主管催单,请尽快完成", map, phones);
        PushMsg pushMsg = new PushMsg();
        pushMsg.setCreateDate(new Date());
        pushMsg.setPropertyId(taskDetails.getGridId());
        pushMsg.setAppType(2);
        pushMsg.setType(1);
        pushMsg.setDetailsId(taskDetails.getId());
        pushMsg.setStatus(taskDetails.getStatus());
        pushMsg.setUserType(2);
        pushMsg.setUserIds(userIds);
        pushMsg.setContent("主管催单,请尽快完成");
        pushMsg.setMessageType(2);        //任务模块
        pushMsg.setTitle("日常任务通知");
        pushMsg.setIsRead(1);
        pushMsgMapper.insert(pushMsg);*/
    }

    @Override
    public void orderEvaluate(JSONObject query) {
        Integer detailsId = query.getInteger("detailsId");
        Integer userId = ThreadLocalUtil.getUserId();
        String remark = query.getString("remark");
        String photos = query.getString("photos");   //照片
        TaskDetails taskDetails = taskDetailsMapper.selectById(detailsId);
        if (taskDetails.getStatus() == 6) {
            JSONObject jsonObject = new JSONObject();
            StringBuilder stringBuilder = new StringBuilder();
            int systemScore = 0;
            jsonObject.put("detailsId", detailsId);
            List<Map<String, Object>> maps = loadSystemScore(jsonObject);
            for (Map<String, Object> map : maps) {
                Object temp = map.get("status");
                Integer status = Integer.valueOf(temp.toString());
                int id = (int) map.get("id");
                int score = (int) map.get("score");
                if (status == 1) {
                    stringBuilder.append(id);
                    stringBuilder.append(",");
                    systemScore += score;
                }
            }
            taskDetails.setTotalScore(Double.valueOf(systemScore));
            taskDetails.setSystemScoreIds(stringBuilder.toString());
            taskDetails.setStatus(7);
            taskDetails.setEvaluateDate(new Date());
            taskDetails.setEvaluationPhotos(photos);
            taskDetails.setEvaluationRemark(remark);
            taskDetails.setEvaluationUserId(userId);
            taskDetailsMapper.updateById(taskDetails);
/*
            Map<String, String> push = new HashMap<>();
            push.put("code", "RWLB");
            push.put("type", "1");
            push.put("detailsId", taskDetails.getId().toString());
            push.put("status", taskDetails.getStatus().toString());
            push.put("messageType", "2");
            Long integer = Long.valueOf(taskDetails.getExecuteUserId());
            JSONObject data = userClient.baseUserInfoById(integer).getJSONObject("data");
            jpushService.sendCustomPush(2, "日常任务", "你的任务已评价", push, (null == data ? "员工已删除" : data.getString("phone")));
            PushMsg pushMsg = new PushMsg();
            pushMsg.setCreateDate(new Date());
            pushMsg.setPropertyId(taskDetails.getGridId());
            pushMsg.setAppType(2);
            pushMsg.setType(1);
            pushMsg.setDetailsId(taskDetails.getId());
            pushMsg.setStatus(7);
            pushMsg.setUserIds(taskDetails.getExecuteUserId());
            pushMsg.setContent("你的任务已评价");
            pushMsg.setMessageType(2);        //日常任务
            pushMsg.setTitle("日常任务");
            pushMsg.setUserType(2);
            pushMsg.setIsRead(1);
            pushMsgMapper.insert(pushMsg);*/
        }
    }

    @Override
    public List<Map<String, Object>> taskStatistics(JSONObject query) {
        List<Map<String, Object>> result = taskDetailsMapper.taskStatistics(query);
        return result;
    }

    @Override
    public Map<String, Object> getBigData(JSONObject query) {
        Map<String, Object> task =new HashMap<>();

        List<Map<String, Object>> bigData = taskDetailsMapper.getBigData(query);
        task.put("bigData",bigData);
        Map<String, Object> allStatistics = taskDetailsMapper.getAllStatistics(query);
        task.put("allStatistics",allStatistics);
        return task;
    }

    public BigDecimal getWorkingHours(JSONObject query) throws ParseException {
        String cood = query.getString("cood");
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Integer detailsId = query.getInteger("detailsId");
        Date operateDate = null;                //接单时间
        String completeDate = "";             //完成的提交时间
        String reviewDate = "";                 //审核时间
        if ("RWLB".equals(cood)) {
            TaskDetails taskDetails = taskDetailsMapper.selectById(detailsId);
            if (taskDetails.getStatus() < 6) {
                return new BigDecimal(0);
            }
            operateDate = taskDetails.getReceiveDate();
            completeDate = taskDetails.getCompleteDate();
            reviewDate = taskDetails.getReviewDate();
        }
        String[] completeDates = completeDate.split(",");
        String[] reviewDates = reviewDate.split(",");
        Long min = 0L;  //总时间
        for (int i = 0; i < completeDates.length; i++) {       //完成时间
            if (i == 0) {          //第一次
                Date fist = simple.parse(completeDates[0]);
                min = fist.getTime() - operateDate.getTime();
            } else {
                Date completeDat = simple.parse(completeDates[i]);
                Date reviewDat = simple.parse(reviewDates[i - 1]);
                min = min + (completeDat.getTime() - reviewDat.getTime());
            }
        }
        BigDecimal divide = new BigDecimal(min).divide(new BigDecimal(1000 * 60 * 60), 2, BigDecimal.ROUND_HALF_UP);
        return divide;
    }

    public List<Map<String, Object>> loadSystemScore(JSONObject param) {
        Integer detailsId = param.getInteger("detailsId");
        String systemScoreIds = null;

        TaskDetails taskDetails = taskDetailsMapper.selectById(detailsId);
        if (taskDetails != null && StringUtils.isNotEmpty(taskDetails.getSystemScoreIds())) {
            systemScoreIds = taskDetails.getSystemScoreIds();
        }

        Map<String, Object> timelyMap = taskScoreService.loadTimely();
        List<Map<String, Object>> timelyList = new ArrayList<>();
        Map<String, Object> taskTimely;
        for (String key : timelyMap.keySet()) {
            if ("id".equals(key) || "gridId".equals(key)) {
                continue;
            }
            Map<String, Object> temp = (Map<String, Object>) timelyMap.get(key);
            if ("1".equals(temp.get("status").toString())) {
                taskTimely = new HashMap<>();
                // 区分系统分来组装对应的说明
                if ("1".equals(temp.get("num").toString())) {
                    taskTimely.put("remark", "前" + temp.get("time") + "分钟内抢单或接单");
                } else if ("2".equals(temp.get("num").toString())) {
                    taskTimely.put("remark", "前" + temp.get("time") + "分钟内抢单或接单");
                } else {
                    taskTimely.put("remark", temp.get("time") + "分钟内完成");
                }
                taskTimely.put("id", temp.get("num"));
                taskTimely.put("score", temp.get("score"));
                // 当前单据包含对应系统分num
                if (StringUtils.isNotEmpty(systemScoreIds)) {
                    if (systemScoreIds.contains(temp.get("num").toString())) {
                        taskTimely.put("status", 1);
                    } else {
                        taskTimely.put("status", 0);
                    }
                } else {
                    taskTimely.put("status", "0");
                }
                timelyList.add(taskTimely);
            }
        }
        if (!timelyList.isEmpty()) {
            Collections.sort(timelyList, (o1, o2) -> {
                if (o1.get("id") == null && o2.get("id") == null) {
                    return 0;
                }
                if (o1.get("id") == null) {
                    return -1;
                }
                if (o2.get("id") == null) {
                    return 1;
                }
                try {
                    return Integer.parseInt(o1.get("id").toString()) > Integer.parseInt(o2.get("id").toString()) ? 1 : -1;
                } catch (Exception e) {
                    return 0;
                }
            });
        }
        return timelyList;
    }
}
