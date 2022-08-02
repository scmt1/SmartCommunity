package me.zhengjie.service.task.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import me.zhengjie.common.utils.SecurityUtil;
import me.zhengjie.global.GridTree;
import me.zhengjie.mapper.task.*;
import me.zhengjie.entity.task.*;
import me.zhengjie.service.task.ITaskManagerService;
import me.zhengjie.service.task.ITaskScoreService;

import lombok.AllArgsConstructor;
import me.zhengjie.util.BusinessErrorException;
import me.zhengjie.util.ThreadLocalUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static me.zhengjie.util.OBSUploadUtil.batchUpload;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ljj
 * @since 2019-05-06
 */
@Service
@AllArgsConstructor
public class TaskManagerServiceImpl extends ServiceImpl<TaskManagerMapper, TaskManager> implements ITaskManagerService {

    private final TaskManagerMapper taskManagerMapper;

    private final TaskDetailsMapper taskDetailsMapper;

    private final TaskDetailsServiceImpl taskDetailsService;

    private final OrderFollowUpMapper orderFollowUpMapper;

    private final ITaskScoreService taskScoreService;

    private final TaskScoreMapper taskScoreMapper;

    private final TaskTimelyScoreMapper taskTimelyScoreMapper;

    private final GridTree gridTree;

//    private final UserClient userClient;

    private final Lock refreshLock = new ReentrantLock();

    private final SecurityUtil securityUtil;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(TaskManager taskManager) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Integer userId = ThreadLocalUtil.getUserId();
        taskManager.setStatus(0);
        Date date = new Date();
        taskManager.setCreateDate(date);
        taskManager.setCode("RW" + sdf.format(date));
        if (taskManager.getCycleFixed() == 2) { //临时
            taskManager.setNextExecDate(new Date());
        } else {
            taskManager.setNextExecDate(buildNextExecDate(taskManager));
        }
        taskManager.setStatus(0);
        if (taskManager.getFiles() != null) {
            try {
                String s = batchUpload(taskManager.getFiles());
                taskManager.setPhotos(s);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        taskManagerMapper.insert(taskManager);
        if (taskManager.getCycleFixed() == 2) {
            TaskDetails taskDetails = new TaskDetails();
            taskDetails.setCycleFixed(taskManager.getCycleFixed());
            if (StringUtils.isEmpty(taskManager.getExecutorIds())) {
                taskDetails.setStatus(1);
            } else {
                taskDetails.setStatus(2);
                taskDetails.setOperateUserId(userId);
                taskDetails.setAssignDate(date);
                taskDetails.setExecuteUserId(taskManager.getExecutorIds());
            }
            taskDetails.setUrgentType(taskManager.getUrgentType());
            taskDetails.setCreateDate(date);
            taskDetails.setGridId(taskManager.getGridId());
            taskDetails.setCode("RW" + sdf.format(date));
            taskDetails.setSubmitUserId(taskManager.getSubmitUserId());
            taskDetails.setMasterId(taskManager.getId());
            taskDetails.setRepairTime(taskManager.getRepairTime());
            taskDetails.setCategoryId(taskManager.getCategoryId());
            taskDetails.setRemark(taskManager.getRemark());
            taskDetails.setRepairPhotos(taskManager.getPhotos());
            taskDetailsMapper.insert(taskDetails);
       /*     if (StringUtils.isEmpty(taskManager.getExecutorIds())) {  //派给主管
                Map<String, String> map = new HashMap<>();
                map.put("code", "RWLB");
                map.put("type", "1");
                map.put("detailsId", taskDetails.getId().toString());
                map.put("status", "1");
                map.put("messageType", "2");
                JSONObject jsonObject = new JSONObject();
                // todo
      *//*          List<JSONObject> deptList = departmentService.loadDeptByProperty(taskDetails.getGridId());
                jsonObject.put("deptList", deptList);
                jsonObject.put("menuCode", "work_dailytask_dispatch");
                List<JSONObject> jsonObjects = internalUserMapper.selectAllAuthUser(jsonObject);
                String userIds = "";
                if (!jsonObjects.isEmpty()) {
                    String[] air = new String[jsonObjects.size()];
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < jsonObjects.size(); i++) {
                        air[i] = (String) jsonObjects.get(i).get("phone");
                        stringBuilder.append(jsonObjects.get(i).get("id"));
                        stringBuilder.append(",");
                    }
                    userIds = stringBuilder.toString();
                    jpushService.sendCustomPush(2, "任务模块", "你有新的任务,请及时处理", map, air);
                }*//*
                PushMsg pushMsg = new PushMsg();
                pushMsg.setCreateDate(new Date());
             //   pushMsg.setGridId(taskDetails.getGridId());
                pushMsg.setAppType(2);
                pushMsg.setUserType(2);
                pushMsg.setType(1);
                pushMsg.setDetailsId(taskDetails.getId());
                pushMsg.setStatus(1);
                //todo
        //        pushMsg.setUserIds(userIds);
                pushMsg.setContent("你有新的任务,请及时处理");
                pushMsg.setMessageType(2);
                pushMsg.setTitle("任务模块");
                pushMsg.setIsRead(1);
                pushMsgMapper.insert(pushMsg);
            } else {
                String userIds = taskManager.getExecutorIds();
                List<Long> ids = new ArrayList<>();
                String[] split = userIds.split(",");
                for (int i = 0; i < split.length; i++) {
                    Long temp = Long.valueOf(split[i]);
                    ids.add(temp);
                }
                // todo
                String[] phones =new String[3];
                //   phones = internalUserMapper.loadPhonesByUserIds(ids);
                Map<String, String> map = new HashMap<>();
                map.put("code", "RWLB");
                map.put("type", "1");
                map.put("detailsId", taskDetails.getId().toString());
                map.put("status", "2");
                map.put("messageType", "2");
                if (phones != null) {
                    jpushService.sendCustomPush(2, "任务模块", "你有新的任务,请及时处理", map, phones);
                    PushMsg pushMsg = new PushMsg();
                    pushMsg.setCreateDate(date);
                 //   pushMsg.setGridId(taskDetails.getGridId());
                    pushMsg.setAppType(2);
                    pushMsg.setUserType(2);
                    pushMsg.setType(1);
                    pushMsg.setDetailsId(taskDetails.getId());
                    pushMsg.setStatus(2);
                    pushMsg.setUserIds(userIds);
                    pushMsg.setContent("你有新的任务,请及时处理");
                    pushMsg.setMessageType(2);
                    pushMsg.setTitle("任务模块");
                    pushMsg.setIsRead(1);
                   // pushMsgMapper.insert(pushMsg);
                }
            }*/
        }


    }


    /**
     * 构建下个要执行的周期时间
     *
     * @param taskManager
     */
    private Date buildNextExecDate(TaskManager taskManager) {
        Date beginDate = taskManager.getBeginDate();
        Date nextDate = null;
        if (null != taskManager.getCycleFixed() && taskManager.getCycleFixed() == 1) {
            Calendar calendar = Calendar.getInstance();
            // 设置时间为开始时间
            calendar.setTime(beginDate);
            Integer cycleType = taskManager.getCycleType();
            Integer cycleNum = taskManager.getCycleNum();
            // 1: 日；2：周；3：月；4：年
            if (1 == cycleType) {
                calendar.add(Calendar.DAY_OF_MONTH, cycleNum);
            } else if (2 == cycleType) {
                calendar.add(Calendar.WEEK_OF_MONTH, cycleNum);
            } else if (3 == cycleType) {
                calendar.add(Calendar.MONTH, cycleNum);
            } else {
                calendar.add(Calendar.YEAR, cycleNum);
            }
            nextDate = calendar.getTime();
        }
        return nextDate;
    }

    @Override
    public void delete(Integer taskManagerId) {
        taskManagerMapper.deleteById(taskManagerId);
    }

    @Override
    public void modify(TaskManager taskManager) {
        buildNextExecDate(taskManager);
        taskManagerMapper.updateById(taskManager);
    }

    @Override
    public Map<String, Object> loadOne(Integer taskManagerId) {
        return taskManagerMapper.selectOneById(taskManagerId);
    }

    @Override
    public IPage<Map<String, Object>> loadAllByQuery(JSONObject query) {
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate = new Date();
        Page<Map<String, Object>> page = new Page<>(query.getLongValue("pageNum"), query.getLongValue("pageSize"));

        IPage<Map<String, Object>> mapIPage = taskManagerMapper.selectByQuery(page, query);
        for (Map<String, Object> record : mapIPage.getRecords()) {

            GridTree.Record greeTree = gridTree.getGridInfomation(record.get("gridId").toString());
            record.put("gridName", greeTree.getName());
            record.put("communityName", greeTree.getCommunityName());
            record.put("streetName", greeTree.getStreetName());
            Long id = (Long) record.get("id");
            TaskDetails taskDetails = taskDetailsMapper.selectById(id);
            Integer status = taskDetails.getStatus();
            record.put("isSubmit", 2);   //今日是否提交，1是，2否
            if (status > 2 && status < 6) {
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
                        record.put("isSubmit", 1);   //今日是否提交，1是，2否
                    }
                } else {
                    if (!sbwx.isEmpty()) {
                        record.put("isSubmit", 1);   //今日是否提交，1是，2否
                    }
                }
            }
        }
        return mapIPage;
    }

    @Override
    public List<Map<String, Object>> loadByQuery(JSONObject query) {
        Map map = JSONObject.toJavaObject(query, Map.class);
        return taskManagerMapper.loadByQuery(map);
    }

    @Override
    public List<Map<String, Object>> loadByAchievements(JSONObject query) {
        List<Map<String, Object>> result = taskManagerMapper.loadByAchievements(query);
        for (Map<String, Object> temp : result) {
            double totalScore = (double) temp.get("totalScore");
            String format = new DecimalFormat("#.00").format(totalScore);
            temp.put("totalScore", format);
            Integer userId = Integer.valueOf(temp.get("userId").toString());
//
//            JSONObject jsonObject = userClient.baseUserInfoById(Long.valueOf(userId));
//            JSONObject data = jsonObject.getJSONObject("data");
//            temp.put("head", data.getString("avatar"));
//            temp.put("realName", data.getString("name"));
            temp.put("departmentName", "");

          /*
          InternalUser internalUser = internalUserMapper.selectById(userId);
            if (null != internalUser) {
                Department department = departmentMapper.selectById(internalUser.getDepartmentId());
                temp.put("head", internalUser.getHead());
                temp.put("realName", internalUser.getRealName());
                temp.put("departmentName",department==null?"": department.getName());
            } else {
                temp.put("head", "员工已删除");
                temp.put("realName", "员工已删除");
                temp.put("departmentName", "员工已删除");
            }
*/
        }

        return result;
    }

    @Override
    public Map<String, Object> loadViewingTotal(JSONObject query) {
        Integer userId = ThreadLocalUtil.getUserId();
        query.put("userId", userId);
        //todo
      /*  InternalUser internalUser = internalUserMapper.selectById(userId);
        query.put("departmentId", internalUser.getDepartmentId());*/

        return taskManagerMapper.loadViewingTotal(query);
    }

    @Override
    public Map<String, Object> loadViewingTotalByUser(JSONObject query) {
        Map<String, Object> map = taskManagerMapper.loadViewingTotalByUser(query);
        if (map != null) {
            double totalScore = (double) map.get("totalScore");
            String format = new DecimalFormat("#.00").format(totalScore);
            //String totalScore = map.get("totalScore").toString();
            map.put("totalScore", format);
            return map;
        } else {
            Map<String, Object> map1 = new HashMap<>();
            map1.put("totalScore", "0.00");
            return map1;
        }

    }

    @Override
    public List<Map<String, Object>> loadTaskByUser(JSONObject query) {
        List<Map<String, Object>> maps = taskManagerMapper.loadTaskByUser(query);
        for (Map<String, Object> map : maps) {
            double totalScore = (double) map.get("totalScore");
            String format = new DecimalFormat("#.00").format(totalScore);
            map.put("totalScore", format);
        }
        return maps;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized void review(JSONObject query) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 1: 审核通过；2: 驳回;
        Integer type = query.getInteger("type");
//        JSONObject data = userClient.currentUser().getJSONObject("data");
        Integer userId =securityUtil.getCurrUser().getId().intValue();
        String remark = query.getString("remark");
        Integer taskDetailsId = query.getInteger("detailsId");
        TaskDetails taskDetails = taskDetailsMapper.selectById(taskDetailsId);
        if (taskDetails.getStatus() !=4){
            throw new BusinessErrorException("请勿重复操作");
        }
        if (type == 1) {
            taskDetails.setStatus(6);
            if (StringUtils.isEmpty(taskDetails.getReviewUserId())) {
                taskDetails.setReviewUserId(userId.toString());
                taskDetails.setReviewDate(simpleDateFormat.format(new Date()));
            } else {
                taskDetails.setReviewUserId(taskDetails.getReviewUserId() + "," + userId);
                taskDetails.setReviewDate(taskDetails.getReviewDate() + "," + simpleDateFormat.format(new Date()));
            }

            TaskManager taskManager = taskManagerMapper.selectById(taskDetails.getMasterId());
            if (StringUtils.isEmpty(taskDetails.getOverruleRemark())) {  //如果有驳回不进行算分
                //组装接单时间的系统评分
                long beginNow;
                // 固定
                if (taskManager.getCycleFixed() == 1) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                    Date beginDate = taskManager.getBeginDate();
                    String begin = sdf.format(beginDate);
                    String now = sdf.format(new Date());
                    String nowMin = now.substring(0, 8) + begin.substring(8);
                    beginNow = sdf.parse(nowMin).getTime();
                } else {
                    beginNow = taskDetails.getReceiveDate().getTime();
                }
                TaskTimelyScore taskTimelyScore = taskTimelyScoreMapper.selectById(1);
                StringBuilder systemScoreNums = new StringBuilder();
                // 系统计算分值，判断状态，再根据时间差异判断是否合格
                if (taskTimelyScore.getGrabOrderFirstStatus() == 1) {
                    // 接单时间减去派单时间
                    long diff = (beginNow - taskManager.getCreateDate().getTime()) / 1000 / 60;
                    if (taskTimelyScore.getGrabOrderFirst() >= diff) {
                        systemScoreNums.append(taskTimelyScore.getGrabOrderFirstNum());
                        systemScoreNums.append(",");
                    }
                }
                if (taskTimelyScore.getGrabOrderLastStatus() == 1) {
                    // 接单时间减去派单时间
                    long diff = (beginNow - taskManager.getCreateDate().getTime()) / 1000 / 60;
                    if (taskTimelyScore.getGrabOrderLast() >= diff) {
                        if (systemScoreNums.length() == 0) {
                            systemScoreNums.append(taskTimelyScore.getGrabOrderLastNum());
                            systemScoreNums.append(",");
                        }
                    }
                }
                if (systemScoreNums.length() > 0) {
                    systemScoreNums.setLength(systemScoreNums.length() - 1);
                    taskDetails.setSystemScoreIds(systemScoreNums.toString());
                }

                // 组装完成时间的系统评分
                if (taskTimelyScore.getCompleteOrderStatus() == 1) {
                    // 完成时间减去接单时间
                    long diff = (simpleDateFormat.parse(taskDetails.getCompleteDate()).getTime() - taskDetails.getReceiveDate().getTime()) / 1000 / 60;
                    if (taskTimelyScore.getCompleteOrder() >= diff) {
                        String systemScoreNum = taskTimelyScore.getCompleteOrderNum().toString();
                        String systemScoreIds = taskDetails.getSystemScoreIds();
                        if (org.apache.commons.lang3.StringUtils.isNotEmpty(systemScoreNum)) {
                            if (StringUtils.isNotEmpty(systemScoreIds)) {
                                taskDetails.setSystemScoreIds(systemScoreIds + "," + systemScoreNum);
                            } else {
                                taskDetails.setSystemScoreIds(systemScoreNum);
                            }
                        }
                    }
                }
            }
        } else {
            taskDetails.setStatus(5);
            if (StringUtils.isEmpty(taskDetails.getReviewUserId())) {
                taskDetails.setReviewUserId(userId.toString());
                taskDetails.setReviewDate(simpleDateFormat.format(new Date()));
                taskDetails.setOverruleRemark(remark);
            } else {
                taskDetails.setReviewUserId(taskDetails.getReviewUserId() + "," + userId);
                taskDetails.setReviewDate(taskDetails.getReviewDate() + "," + simpleDateFormat.format(new Date()));
                taskDetails.setOverruleRemark(taskDetails.getOverruleRemark() + "#%&" + remark);
            }
        }
        taskDetailsMapper.updateById(taskDetails);
        if (taskDetails.getStatus() == 6) {
            //计算工时
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("cood", "RWLB");
            jsonObject.put("detailsId", taskDetails.getId());
            BigDecimal workingHours = taskDetailsService.getWorkingHours(jsonObject);
            taskDetails.setWorkingHours(workingHours);
            taskDetailsMapper.updateById(taskDetails);
        }
    }


    @Override
    public Map<String, Object> loadEvaluationInfo(Integer taskManagerId) {
        TaskDetails taskDetails = taskDetailsMapper.selectOne(new QueryWrapper<TaskDetails>()
                .lambda().eq(TaskDetails::getMasterId, taskManagerId)
                .orderByDesc(TaskDetails::getReceiveDate).last("limit 1"));
        if (null == taskDetails || null == taskDetails.getEvaluationUserId()) {
            return null;
        } else {
            Map<String, Object> result = new HashMap<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            // 评分人
//            JSONObject jsonObject = userClient.baseUserInfoById(Long.valueOf(taskDetails.getEvaluationUserId()));
//            JSONObject data = jsonObject.getJSONObject("data");
//
//            result.put("userName", data.getString("name"));
//            result.put("userHeadPhoto", data.getString("avatar"));
            Map<String, Object> timelyMap = taskScoreService.loadTimely();
            // 系统评分
            String systemScoreIds = taskDetails.getSystemScoreIds();
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
                        taskTimely.put("remark", temp.get("time") + "分钟内抢单或接单");
                    } else {
                        taskTimely.put("remark", temp.get("time") + "分钟内完成");
                    }
                    // 当前单据包含对应系统分num
                    if (StringUtils.isNotEmpty(systemScoreIds)) {
                        String[] split = systemScoreIds.split(",");
                        List<String> strings = Arrays.asList(split);
                        if (strings.contains(temp.get("num").toString())) {
                            taskTimely.put("status", 1);
                            taskTimely.put("score", temp.get("score"));
                        }
                    } else {
                        taskTimely.put("status", 0);
                    }
                    timelyList.add(taskTimely);
                }
            }
            result.put("systemEvaluation", timelyList);
            return result;
        }
    }


    @Override
    public void completeTask(TaskDetails parameter) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TaskDetails taskDetails = taskDetailsMapper.selectById(parameter.getId());
        TaskManager taskManager = taskManagerMapper.selectById(taskDetails.getMasterId());
        taskDetails.setGridId(taskManager.getGridId());

        taskDetails.setStatus(4);
        Date date = new Date();
        if (StringUtils.isEmpty(taskDetails.getReviewUserId())) {
            taskDetails.setCompleteDate(simpleDateFormat.format(date));
            taskDetails.setRemark(parameter.getRemark());
            taskDetails.setCompletePhotos(parameter.getCompletePhotos());
        } else {
            taskDetails.setCompleteDate(taskDetails.getCompleteDate() + "," + simpleDateFormat.format(date));
            taskDetails.setRemark(taskDetails.getCompleteRemark() + "#%&" + parameter.getRemark());
            taskDetails.setCompletePhotos(taskDetails.getCompletePhotos() + "%" + parameter.getCompletePhotos());
        }

        TaskTimelyScore taskTimelyScore = taskTimelyScoreMapper.selectOne(new QueryWrapper<TaskTimelyScore>().eq("property_id", taskManager.getGridId()));
        if (taskTimelyScore.getCompleteOrderStatus() == 1) {
            // 完成时间减去接单时间
            long diff = (date.getTime() - taskDetails.getReceiveDate().getTime()) / 1000 / 60;
            if (taskTimelyScore.getCompleteOrder() >= diff) {
                String systemScoreNum = taskTimelyScore.getCompleteOrderNum().toString();
                String systemScoreIds = taskDetails.getSystemScoreIds();
                if (StringUtils.isNotEmpty(systemScoreNum)) {
                    if (StringUtils.isNotEmpty(systemScoreIds)) {
                        taskDetails.setSystemScoreIds(systemScoreIds + "," + systemScoreNum);
                    } else {
                        taskDetails.setSystemScoreIds(systemScoreNum);
                    }
                }
            }
        }
        taskDetailsMapper.updateById(taskDetails);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject orderTask(JSONObject parameter) throws Exception {
        try {
            Date date = new Date();
            refreshLock.lock();
            JSONObject jsonObject = new JSONObject();
            Integer taskDetailsId = parameter.getInteger("taskManagerId");
            Integer userId = parameter.getInteger("userId");
            // 设置主单状态为未接单
            TaskDetails taskDetails = taskDetailsMapper.selectById(taskDetailsId);
            if (taskDetails.getStatus() > 2) {
                jsonObject.put("code", "501");
                jsonObject.put("msg", "该单已被抢");
            }
            taskDetails.setStatus(3);
            taskDetails.setExecuteUserId(userId.toString());
            taskDetails.setReceiveDate(date);
            TaskTimelyScore taskTimelyScore = taskTimelyScoreMapper.selectById(1);

            StringBuilder systemScoreNums = new StringBuilder();
            // 系统计算分值，判断状态，再根据时间差异判断是否合格
            if (taskTimelyScore.getGrabOrderFirstStatus() == 1) {
                // 接单时间减去派单时间
                long diff = (date.getTime() - taskDetails.getAssignDate().getTime()) / 1000 / 60;
                if (taskTimelyScore.getGrabOrderFirst() >= diff) {
                    systemScoreNums.append(taskTimelyScore.getGrabOrderFirstNum());
                    systemScoreNums.append(",");
                }
            }
            if (taskTimelyScore.getGrabOrderLastStatus() == 1) {
                // 接单时间减去派单时间
                long diff = (date.getTime() - taskDetails.getAssignDate().getTime()) / 1000 / 60;
                if (taskTimelyScore.getGrabOrderLast() >= diff) {
                    if (systemScoreNums.length() == 0) {
                        systemScoreNums.append(taskTimelyScore.getGrabOrderLastNum());
                        systemScoreNums.append(",");
                    }
                }
            }
            if (systemScoreNums.length() > 0) {
                systemScoreNums.setLength(systemScoreNums.length() - 1);
                taskDetails.setSystemScoreIds(systemScoreNums.toString());
            }
            taskDetailsMapper.updateById(taskDetails);
       /*
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("property_id", taskDetails.getGridId());
            queryWrapper.eq("app_type", 2);
            queryWrapper.eq("message_type", 2);
            queryWrapper.eq("details_id", taskDetails.getId());
            queryWrapper.eq("type", 1);
            List<PushMsg> list = pushMsgMapper.selectList(queryWrapper);
            for (PushMsg pushMsg : list) {
                pushMsg.setUserIds(userId.toString());
                pushMsgMapper.updateById(pushMsg);
            }*/
            return jsonObject;
        } finally {
            refreshLock.unlock();
        }


    }

    @Override
    public void addEvaluation(JSONObject parameter) {
        Integer detailsId = parameter.getInteger("detailsId");
        String taskScoreIds = parameter.getString("taskScoreIds");
        Integer userId = parameter.getInteger("userId");
        TaskDetails taskDetails = taskDetailsMapper.selectById(detailsId);
        taskDetails.setStatus(7);
        taskDetails.setEvaluationUserId(userId);
        taskDetails.setEvaluateDate(new Date());
        taskDetails.setTaskScoreIds(taskScoreIds);

        Double countScore = 0.0;
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(taskDetails.getSystemScoreIds())) {
            TaskTimelyScore taskTimelyScore = taskTimelyScoreMapper.selectOne(new QueryWrapper<TaskTimelyScore>().eq("property_id", taskDetails.getGridId()));
            String[] split = taskDetails.getSystemScoreIds().split(",");
            for (String str : split) {
                if (str.equals(taskTimelyScore.getGrabOrderFirstNum().toString())) {
                    countScore += Double.valueOf(taskTimelyScore.getGrabOrderFirstScore().toString());
                }
                if (str.equals(taskTimelyScore.getGrabOrderLastNum().toString())) {
                    countScore += Double.valueOf(taskTimelyScore.getGrabOrderLastScore().toString());
                }
                if (str.equals(taskTimelyScore.getCompleteOrderNum().toString())) {
                    countScore += Double.valueOf(taskTimelyScore.getCompleteOrderScore().toString());
                }
            }
        }
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(taskDetails.getTaskScoreIds())) {
            List<Integer> scores = taskScoreMapper.loadScoreByIds(taskDetails.getTaskScoreIds());
            for (Integer integer : scores) {
                countScore += Double.valueOf(integer.toString());
            }
        }
        if (null != taskDetails.getEvaluationStarLevel()) {
            countScore += taskDetails.getEvaluationStarLevel();
        }
        taskDetails.setTotalScore(countScore);
        taskDetailsMapper.updateById(taskDetails);
        TaskManager taskManager = taskManagerMapper.selectById(taskDetails.getMasterId());
        taskManager.setStatus(2);
        taskManagerMapper.updateById(taskManager);
    }

    @Override
    public void cancel(Integer taskManagerId) {
        TaskManager taskManager = taskManagerMapper.selectById(taskManagerId);
        taskManager.setStatus(3);
        taskManager.setOrderStatus(2);
        List<TaskDetails> taskDetails = taskDetailsMapper.selectList(new QueryWrapper<TaskDetails>()
                .lambda().eq(TaskDetails::getMasterId, taskManagerId));
        for (TaskDetails taskDetail : taskDetails) {
            taskDetail.setStatus(6);
            taskDetailsMapper.updateById(taskDetail);
        }
        taskManagerMapper.updateById(taskManager);
    }

    @Override
    public Map<String, Object> loadOneDetails(Integer taskManagerId) {
        return taskDetailsMapper.loadOneDetails(taskManagerId);
    }

    @Override
    public Map<String, Object> loadCycleInfo(Integer taskManagerId) {
        Map<String, Object> result = taskManagerMapper.loadCycleInfo(taskManagerId);

        return result;
    }

    /*
     * 任务定时器
     * */
//    @Transactional(rollbackFor = Exception.class)
//    public ReturnT<String> execTimerByCycleInRWMK(String param) {
//        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        List<TaskManager> taskManagers = taskManagerMapper.selectList(new QueryWrapper<TaskManager>().lambda()
//                .eq(TaskManager::getCycleFixed, 1)
//                .eq(TaskManager::getStatus, 0)
//                .apply("date_format(next_exec_date,'%Y-%m-%d') = {0}", sdf.format(new Date())));
//        Date date = new Date();
//        for (TaskManager taskManager : taskManagers) {
//            TaskDetails taskDetails = new TaskDetails();
//            taskDetails.setStatus(1);
//            taskDetails.setGridId(taskManager.getGridId());
//            taskDetails.setCycleFixed(1);
//            taskDetails.setCreateDate(date);
//            taskDetails.setRemark(taskManager.getRemark());
//            taskDetails.setCategoryId(taskManager.getCategoryId());
//            taskDetails.setMasterId(taskManager.getId());
//            taskDetails.setRepairPhotos(taskManager.getPhotos());
//            taskDetails.setSubmitUserId(taskManager.getSubmitUserId());
//            taskDetails.setCode("RW" + format.format(date));
//            taskManager.setBeginDate(taskManager.getNextExecDate());
//            taskManager.setNextExecDate(buildNextExecDate(taskManager));
//            taskManagerMapper.updateById(taskManager);
//            taskDetailsMapper.insert(taskDetails);
//            //消息推送
//          /*  PushMsg pushMsg = new PushMsg();
//            Map<String, String> temp = new HashMap<>();
//            temp.put("code", "RWLB");
//            temp.put("type", "1");
//            temp.put("detailsId", taskDetails.getId().toString());
//            temp.put("status", "1");
//            temp.put("messageType", "2");
//            JSONObject jsonObject = new JSONObject();
//            //todo
//         *//*   List<JSONObject> deptList = departmentService.loadDeptByProperty(taskManager.getGridId());
//            jsonObject.put("deptList", deptList);
//            jsonObject.put("menuCode", "work_dailytask_dispatch");
//            List<JSONObject> jsonObjects = internalUserMapper.selectAllAuthUser(jsonObject);
//            String userIds = "";
//            if (!jsonObjects.isEmpty()) {
//                StringBuilder stringBuilder = new StringBuilder();
//                String[] air = new String[jsonObjects.size()];
//                for (int i = 0; i < jsonObjects.size(); i++) {
//                    air[i] = (String) jsonObjects.get(i).get("phone");
//                    stringBuilder.append(jsonObjects.get(i).get("id"));
//                    stringBuilder.append(",");
//                }
//                userIds = stringBuilder.toString();
//                jpushService.sendPush(2, "定期任务", "你有新的定时任务需要处理", temp, air);
//                pushMsg.setUserIds(userIds);
//                pushMsg.setGridId(taskDetails.getGridId());
//                pushMsg.setAppType(2);
//                pushMsg.setDetailsId(taskDetails.getId());
//                pushMsg.setTitle("定期任务");
//                pushMsg.setContent("你有新的定时任务需要处理");
//                pushMsg.setMessageType(2);
//                pushMsg.setType(1);
//                pushMsg.setStatus(1);
//                pushMsg.setCreateDate(new Date());
//                pushMsg.setIsRead(1);
//                pushMsgMapper.insert(pushMsg);
//            }*/
//        }
//        return ReturnT.SUCCESS;
//    }

    @Override
    public List<Map<String, Object>> loadTaskForApp(String gridId) {

        return taskManagerMapper.loadTaskForApp(gridId);
    }

    @Override
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

    @Override
    public List<Map<String, Object>> loadByDeparentAchievements(JSONObject query) {
        Integer userId = ThreadLocalUtil.getUserId();
        //todo
/*        InternalUser user = internalUserMapper.selectById(userId);
        query.put("gridId", query.getInteger("gridId"));
        query.put("userId", userId);
        query.put("departmentId",user.getDepartmentId());*/
        if (null != query.get("createDate")) {
            String receiveDate = (String) query.get("createDate");
            String substring = receiveDate.substring(0, 7);
            query.put("createDate", substring);
        }
        List<Map<String, Object>> result = taskManagerMapper.loadByDeparentAchievements(query);
        for (Map<String, Object> temp : result) {
            Integer id = Integer.valueOf(temp.get("userId").toString());
            double totalScore = (double) temp.get("totalScore");
            String format = new DecimalFormat("#.00").format(totalScore);
            //todo
/*            InternalUser internalUser = internalUserMapper.selectById(id);
            Department department = departmentMapper.selectById(internalUser.getDepartmentId());
            temp.put("head", internalUser.getHead());
            temp.put("realName", (null == internalUser ? "员工已删除" : internalUser.getRealName()));
            temp.put("departmentName", department.getName());
            temp.put("totalScore", format);*/
        }

        return result;
    }

    @Override
    public IPage<Map<String, Object>> loadAllPeriodicTask(JSONObject query) {
        Page<Map<String, Object>> page = new Page<>(query.getLongValue("pageNum"), query.getLongValue("pageSize"));
        IPage<Map<String, Object>> mapIPage = taskManagerMapper.loadAllPeriodicTask(page, query);
        for (Map<String, Object> record : mapIPage.getRecords()) {
            record.put("gridName", "");
            record.put("communityName", "");
            record.put("streetName", "");
            if (record.get("gridId") != null) {
                GridTree.Record gridId = gridTree.getGridInfomation(record.get("gridId").toString());
                record.put("gridName", org.apache.commons.lang3.StringUtils.isEmpty(gridId.getName()) ? "" : gridId.getName());
                record.put("communityName", org.apache.commons.lang3.StringUtils.isEmpty(gridId.getCommunityName()) ? "" : gridId.getCommunityName());
                record.put("streetName", org.apache.commons.lang3.StringUtils.isEmpty(gridId.getStreetName()) ? "" : gridId.getStreetName());
            }

        }
        return mapIPage;
    }

    @Override
    public void updateStatus(TaskManager taskManager) {
        taskManagerMapper.updateById(taskManager);
    }

    @Override
    public TaskManager getTaskManager(Integer taskManagerId) {
        TaskManager taskManager = taskManagerMapper.getTaskManager(taskManagerId);
        String gridId = taskManager.getGridId();
        GridTree.Record gridInfomation = gridTree.getGridInfomation(gridId);
        taskManager.setGridName(gridInfomation.getName());
        taskManager.setStreetName(gridInfomation.getStreetName());
        taskManager.setCommunityName(gridInfomation.getCommunityName());


        return taskManager;
    }

    @Override
    public Long loadManagerNew(Integer taskManagerId) {
        TaskManager taskManager = taskManagerMapper.selectById(taskManagerId);
        if ( taskManager.getCycleFixed()==2){
            throw new BusinessErrorException("该工单不是周期任务");
        }
        TaskDetails taskDetails = taskDetailsMapper.selectOne(new QueryWrapper<TaskDetails>().lambda()
                .eq(TaskDetails::getMasterId, taskManagerId)
                .orderByDesc(TaskDetails::getCreateDate)
                .last("limit 1"));
        if (taskDetails ==null){
            return 0L;
        }else {
            return taskDetails.getId();
        }

    }

}
