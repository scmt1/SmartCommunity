package me.zhengjie.controller.gridevents;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import me.zhengjie.common.constant.ActivitiConstant;
import me.zhengjie.common.exception.XbootException;
import me.zhengjie.common.utils.ResultCode;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.utils.SecurityUtil;
import me.zhengjie.common.utils.SnowFlakeUtil;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.ActBusiness;
import me.zhengjie.entity.ActProcess;
import me.zhengjie.entity.business.Leave;
import me.zhengjie.entity.GridStatistics;
import me.zhengjie.entity.gridevents.EventsType;
import me.zhengjie.entity.gridevents.GridEvents;
import me.zhengjie.service.ActBusinessService;
import me.zhengjie.service.ActProcessService;
import me.zhengjie.service.business.LeaveService;
import me.zhengjie.service.gridevents.IEventsTypeService;
import me.zhengjie.service.gridevents.IGridEventsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.service.mybatis.IHistoryIdentityService;
import me.zhengjie.service.mybatis.IRunIdentityService;
import me.zhengjie.system.service.UserService;
import me.zhengjie.system.service.dto.UserDto;
import me.zhengjie.utils.MessageUtil;
import me.zhengjie.vo.Assignee;
import me.zhengjie.vo.HistoricTaskVo;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/events")
@AllArgsConstructor
public class GridEventsController {

    private final IGridEventsService gridEventsService;

    private final IEventsTypeService eventsTypeService;

    private final SecurityUtil securityUtil;

    @Autowired
    private ActBusinessService actBusinessService;

    @Autowired
    private ActProcessService actProcessService;

    @Autowired
    private LeaveService leaveService;

    //
    @Autowired
    private HistoryService historyService;

    @Autowired
    private UserService userService;

    @Autowired
    private IHistoryIdentityService iHistoryIdentityService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private IRunIdentityService iRunIdentityService;

    @Autowired
    private MessageUtil messageUtil;

    /**
     * 新增网格事件 提交申请 启动流程
     *
     * @param event
     * @return
     */
    @PostMapping("/addEvents")
    public Result<Object> addEvents(@RequestBody GridEvents event) throws Exception {
        /*先新增事件*/
        event.setIsDeleted(0);
        event.setCreateTime(new Timestamp(System.currentTimeMillis()));
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        event.setCode(sdf.format(date));
        gridEventsService.save(event);
        //事件类型判断逻辑
        if(event.getEventsTypeId()==0||event.getEventsTypeId()==null){
            return ResultUtil.error("事件类型为空");
        }
        /*事件类型绑定类型逻辑判断逻辑*/
        if(event.getEventsTypes()==null|| event.getEventsTypes().getId()==null){
            EventsType eventsType = eventsTypeService.getById(event.getEventsTypeId());
            if(eventsType==null||StringUtils.isBlank(eventsType.getProcDefId())){
                return ResultUtil.error("事件类型绑定类型不存在，请联系管理员");
            }
            event.setEventsTypes(eventsType);
        }
        ActProcess actProcess = actProcessService.get(event.getEventsTypes().getProcDefId());
        if(actProcess==null || StringUtils.isBlank(actProcess.getId())){
            return ResultUtil.error("事件流程不存在");
        }

        /*获取流程实例id*/
        ActBusiness actBusiness =new ActBusiness();
        //关联表id
        actBusiness.setTableId(""+event.getId());
        //参数(可以不要)
        actBusiness.getParams().put("test", "tets");
        //流程定义id
        actBusiness.setProcDefId(event.getEventsTypes().getProcDefId());
        //id
        actBusiness.setId(""+event.getId());
        //标题(事件内容)
        actBusiness.setTitle(event.getTitle());
        //第一个节点是否为网关
        actBusiness.setFirstGateway(true);
        //是否发送站内消息
        actBusiness.setSendMessage(true);
        //是否发送短信通知
        actBusiness.setSendSms(true);
        //优先级
        actBusiness.setPriority(0);
        //流程实例id
        String processInstanceId = actProcessService.startProcess(actBusiness);

        /*更新网格事件*/
        GridEvents events = new GridEvents();
        //获取当前处理人 节点id 节点名
        List<HistoricTaskInstance> taskList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId).orderByHistoricTaskInstanceEndTime().asc().list();
        taskList.forEach(e -> {
            if(events.getNodeId() == null && events.getNodeName() == null){
                //节点id 存入事件
                events.setNodeId(e.getTaskDefinitionKey());
                //节点名 存入事件
                events.setNodeName(e.getName());
            }
            //图片视频 数据拼接并 存入事件
            if(StringUtils.isNotBlank(event.getMediaAddress())){
                String[] mediaAddress = event.getMediaAddress().split(";");
                String mediaAddressNew = "";
                for(int i = 0;i < mediaAddress.length;i ++){
                    mediaAddressNew += "'nodeId':'"+e.getTaskDefinitionKey()+"','nodeName':'"+e.getName()+"','url':'"+mediaAddress[i]+"';";
                }
                events.setMediaAddress(mediaAddressNew);
            }
            //候选
            List<HistoricIdentityLink> identityLinks = historyService.getHistoricIdentityLinksForTask(e.getId());
            String userIds = "";
            int x = 1;
            for(HistoricIdentityLink hik : identityLinks){
                // 候选用户（分配的候选用户审批人）
                if("candidate".equals(hik.getType())&&StrUtil.isNotBlank(hik.getUserId())){
                    Long userId = userService.findById(Long.parseLong(hik.getUserId())).getId();
                    if(x == identityLinks.size()){
                        userIds += "" + userId + "";
                    }else{
                        userIds += "" + userId + ",";
                    }
                }
                x ++;
            }
            //执行人 存入事件
            events.setExecutor(userIds);
        });
        //id
        events.setId(event.getId());
        //流程实例id 存入事件
        events.setProcInstId(processInstanceId);
        //状态 存入事件
        events.setStatus(1);
        gridEventsService.updateById(events);

        return ResultUtil.success("操作成功");
    }

    /**
     * 新增网格事件 提交申请 启动流程
     *
     * @param event
     * @return
     */
    @PostMapping("/addEventsSelf")
    public Result<Object> addEventsSelf(@RequestBody GridEvents event) throws Exception {
        /*先新增事件*/
        event.setHandleSelf(event.getCreateUser());
        event.setIsDeleted(0);
        event.setCreateTime(new Timestamp(System.currentTimeMillis()));
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        event.setCode(sdf.format(date));
        gridEventsService.save(event);

        /*流程判断*/
        ActProcess actProcess = actProcessService.get(event.getEventsTypes().getProcDefId());
        if(actProcess==null || StringUtils.isBlank(actProcess.getId())){
            return ResultUtil.error("事件流程不存在");
        }

        /*获取流程实例id*/
        ActBusiness actBusiness =new ActBusiness();
        //关联表id
        actBusiness.setTableId(""+event.getId());
        //参数(可以不要)
        actBusiness.getParams().put("test", "tets");
        //流程定义id
        actBusiness.setProcDefId(event.getEventsTypes().getProcDefId());
        //id
        actBusiness.setId(""+event.getId());
        //标题(事件内容)
        actBusiness.setTitle(event.getTitle());
        //第一个节点是否为网关
        actBusiness.setFirstGateway(true);
        //是否发送站内消息
        actBusiness.setSendMessage(true);
        //是否发送短信通知
        actBusiness.setSendSms(true);
        //优先级
        actBusiness.setPriority(0);
        //流程实例id
        String processInstanceId = actProcessService.startProcess(actBusiness);

        /*更新网格事件*/
        GridEvents events = new GridEvents();
        //获取当前处理人 节点id 节点名
        List<HistoricTaskInstance> taskList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId).orderByHistoricTaskInstanceEndTime().asc().list();
        taskList.forEach(e -> {
            if(events.getNodeId() == null && events.getNodeName() == null){
                //节点id 存入事件
                events.setNodeId(e.getTaskDefinitionKey());
                //节点名 存入事件
                events.setNodeName(e.getName());
            }
            //图片视频 数据拼接并 存入事件
            if(StringUtils.isNotBlank(event.getMediaAddress())){
                String[] mediaAddress = event.getMediaAddress().split(";");
                String mediaAddressNew = "";
                for(int i = 0;i < mediaAddress.length;i ++){
                    mediaAddressNew += "'nodeId':'"+e.getTaskDefinitionKey()+"','nodeName':'"+e.getName()+"','url':'"+mediaAddress[i]+"';";
                }
                events.setMediaAddress(mediaAddressNew);
            }
            //执行人 存入事件(自办自结,创建人就是执行人)
            events.setExecutor("" + event.getCreateUser());
        });
        //id
        events.setId(event.getId());
        //流程实例id 存入事件
        events.setProcInstId(processInstanceId);
        //状态 存入事件
        events.setStatus(2);
        gridEventsService.updateById(events);

        return ResultUtil.success("操作成功");
    }

    @RequestMapping(value = "/historicFlow/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "流程流转历史")
    public Result<Object> historicFlow(@ApiParam("流程实例id") @PathVariable String id){

        List<HistoricTaskVo> list = new ArrayList<>();

        List<HistoricTaskInstance> taskList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(id).orderByHistoricTaskInstanceEndTime().asc().list();

        // 转换vo
        taskList.forEach(e -> {
            HistoricTaskVo htv = new HistoricTaskVo(e);
            List<Assignee> assignees = new ArrayList<>();
            // 关联分配人（委托用户时显示该人）
            if(StrUtil.isNotBlank(htv.getAssignee())&& StrUtil.isNotBlank(htv.getOwner())){
                String assignee = userService.findById(Long.parseLong(htv.getAssignee())).getUsername();
                String owner = userService.findById(Long.parseLong(htv.getOwner())).getUsername();
                assignees.add(new Assignee(assignee+"(受"+owner+"委托)", true));
            }
            List<HistoricIdentityLink> identityLinks = historyService.getHistoricIdentityLinksForTask(e.getId());
            // 获取实际审批用户id
            String userId = iHistoryIdentityService.findUserIdByTypeAndTaskId(ActivitiConstant.EXECUTOR_TYPE, e.getId());
            for(HistoricIdentityLink hik : identityLinks){
                // 关联候选用户（分配的候选用户审批人）
                if("candidate".equals(hik.getType())&&StrUtil.isNotBlank(hik.getUserId())){
                    String username = userService.findById(Long.parseLong(hik.getUserId())).getUsername();
                    Assignee assignee = new Assignee(username, false);
                    if(StrUtil.isNotBlank(userId)&&userId.equals(hik.getUserId())){
                        assignee.setIsExecutor(true);
                    }
                    assignees.add(assignee);
                }
            }
            htv.setAssignees(assignees);
            // 关联审批意见
            List<Comment> comments = taskService.getTaskComments(htv.getId(), "comment");
            if(comments!=null&&comments.size()>0){
                htv.setComment(comments.get(0).getFullMessage());
            }
            list.add(htv);
        });
        return ResultUtil.data(list);
    }

    @RequestMapping(value = "/pass", method = RequestMethod.POST)
    @ApiOperation(value = "任务节点审批通过")
    public Result<Object> pass(@ApiParam("任务id") @RequestParam String id,
                               @ApiParam("流程实例id") @RequestParam String procInstId,
                               @ApiParam("受理人") @RequestParam String assigneeId,
                               @ApiParam("下个节点审批人") @RequestParam(required = false) String[] assignees,
                               @ApiParam("优先级") @RequestParam(required = false) Integer priority,
                               @ApiParam("意见评论") @RequestParam(required = false) String comment,
                               @ApiParam("是否发送站内消息") @RequestParam(defaultValue = "false") Boolean sendMessage,
                               @ApiParam("是否发送短信通知") @RequestParam(defaultValue = "false") Boolean sendSms,
                               @ApiParam("是否发送邮件通知") @RequestParam(defaultValue = "false") Boolean sendEmail){

        if(StrUtil.isBlank(comment)){
            comment = "";
        }
        //存入意见评论
        taskService.addComment(id, procInstId, comment);
        if(StringUtils.isNotBlank(assigneeId)){
            //存入受理人
            taskService.setAssignee(id, assigneeId);
        }
        QueryWrapper<GridEvents> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(i -> i.like("grid_events.proc_inst_id", procInstId));
        queryWrapper.and(i -> i.like("grid_events.is_deleted", 0));
        GridEvents gridEvents = gridEventsService.getOne(queryWrapper);
        if(gridEvents==null || gridEvents.getId() ==null){
            return  ResultUtil.error("操作失败：查询");

        }
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(procInstId).singleResult();
        Task task = taskService.createTaskQuery().taskId(id).singleResult();
        if(StrUtil.isNotBlank(task.getOwner())&&!("RESOLVED").equals(task.getDelegationState().toString())){
            // 未解决的委托任务 先resolve
            String oldAssignee = task.getAssignee();
            taskService.resolveTask(id);
            taskService.setAssignee(id, oldAssignee);
        }
        taskService.complete(id);
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(procInstId).list();
        // 判断下一个节点
        if(tasks!=null&&tasks.size()>0){
            for(Task t : tasks){
                if(assignees==null||assignees.length<1){
                    // 如果下个节点未分配审批人为空 取消结束流程
                    List<UserDto> users = actProcessService.getNode(t.getTaskDefinitionKey()).getUsers();
                    if(users==null||users.size()==0){
                        runtimeService.deleteProcessInstance(procInstId, "canceled-审批节点未分配审批人，流程自动中断取消");
//                        ActBusiness actBusiness = actBusinessService.get(pi.getBusinessKey());
////                        actBusiness.setStatus(ActivitiConstant.STATUS_CANCELED);
////                        actBusiness.setResult(ActivitiConstant.RESULT_TO_SUBMIT);
////                        actBusinessService.update(actBusiness);
                        gridEvents.setStatus(ActivitiConstant.STATUS_CANCELED);
                        gridEventsService.updateById(gridEvents);
                        break;
                    }else{
                        // 避免重复添加
                        List<String> list = iRunIdentityService.selectByConditions(t.getId(), "candidate");
                        if(list==null||list.size()==0) {
                            String userId = "";
                            // 分配了节点负责人分发给全部
                            for (UserDto user : users) {
                                taskService.addCandidateUser(t.getId(), user.getId().toString());
                                userId = userId+user.getId()+",";
                                // 异步发消息
                                messageUtil.sendActMessage(user.getId().toString(), ActivitiConstant.MESSAGE_TODO_CONTENT, sendMessage, sendSms, sendEmail);
                            }

                            taskService.setPriority(t.getId(), task.getPriority());
                            gridEvents.setExecutor(userId);
                            gridEvents.setStatus(ActivitiConstant.STATUS_DEALING);
                            gridEventsService.updateById(gridEvents);
                        }
                    }
                }else{
                    // 避免重复添加
                    List<String> list = iRunIdentityService.selectByConditions(t.getId(), "candidate");
                    if(list==null||list.size()==0) {
                        String userId = "";
                        for(String assignee : assignees){
                            taskService.addCandidateUser(t.getId(), assignee);
                            // 异步发消息
                            messageUtil.sendActMessage(assignee, ActivitiConstant.MESSAGE_TODO_CONTENT, sendMessage, sendSms, sendEmail);
                            taskService.setPriority(t.getId(), priority);
                            userId = userId+assignee+",";
                        }
                        gridEvents.setExecutor(userId);
                        gridEvents.setStatus(ActivitiConstant.STATUS_DEALING);
                        gridEventsService.updateById(gridEvents);
                    }
                }
            }
        } else {//代表任务已完成
            gridEvents.setExecutor(" ");
            gridEvents.setEndDate(new Timestamp(System.currentTimeMillis()));
            gridEvents.setStatus(ActivitiConstant.STATUS_FINISH);
            gridEventsService.updateById(gridEvents);
            // 异步发消息
            messageUtil.sendActMessage(gridEvents.getCreateUser()+"", ActivitiConstant.MESSAGE_PASS_CONTENT, sendMessage, sendSms, sendEmail);
        }
        // 记录实际审批人员
        iHistoryIdentityService.insert(String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId()),
                ActivitiConstant.EXECUTOR_TYPE, securityUtil.getCurrUser().getId().toString(), id, procInstId);
        return ResultUtil.success("操作成功");
    }

    /**
     * 放入相应流程所需变量 此处仅做演示
     * 【推荐使用绑定监听器设置变量 更加灵活】
     * @param act
     * @return
     */
    public ActBusiness putParams(ActBusiness act){

        if(StrUtil.isBlank(act.getTableId())){
            throw new XbootException("关联业务表TableId不能为空");
        }
        // 如果属于请假流程
        Leave leave = leaveService.get(act.getTableId());
        if(leave!=null){
            // 放入变量
            act.getParams().put("duration", leave.getDuration());
        }
        return act;
    }

    /**
     * 新增网格事件
     *
     * @param gridEvents
     * @return
     */
    @PostMapping("/add")
    public Result<Object> add(@RequestBody GridEvents gridEvents) throws Exception {
        gridEvents.setIsDeleted(0);
        gridEvents.setCreateTime(new Timestamp(System.currentTimeMillis()));
        gridEvents.setHandleSelf(gridEvents.getCreateUser());
        gridEventsService.add(gridEvents);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 修改网格事件
     *
     * @param gridEvents
     * @return
     * @throws Exception
     */
    @PostMapping("/modify")
    public Result<Object> modify(@RequestBody GridEvents gridEvents) throws Exception {
        //图片视频 数据拼接并 存入事件
        String[] mediaAddress = gridEvents.getMediaAddress().split(";");
        /*if(StringUtils.isNotBlank(gridEvents.getMediaAddress())){
            String mediaAddressNew = "";
            for(int i = 0;i < mediaAddress.length;i ++){
                mediaAddressNew += "'nodeId':'"+gridEvents.getNodeId()+"','nodeName':'"+gridEvents.getNodeName()+"','url':'"+mediaAddress[i]+"';";
            }
            gridEvents.setMediaAddress(mediaAddressNew);
        }*/
        gridEventsService.updateById(gridEvents);
        return ResultUtil.success(ResultCode.SUCCESS);
    }


    /**
     * 获取单个网格事件
     *
     * @param id
     * @return
     */
    @GetMapping("/loadOne")
    public Result<Object> loadOne(Integer id) {
        GridEvents gridEvents = gridEventsService.loadOne(id);
//        GridEvents gridEvents = gridEventsService.getOne(id);
        return ResultUtil.data(gridEvents);
    }

    /**
     * 删除网格事件
     *
     * @param gridEventsId
     * @return
     */
    @GetMapping("/delete")
    public Result<Object> delete(Integer gridEventsId) {
        gridEventsService.delete(gridEventsId);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 获取所有任务详情
     *
     * @param query
     * @return
     */
    @PostMapping("/loadAllByQuery")
    public Result<Object> loadAllByQuery(@RequestBody JSONObject query) {
        //用户数据权限
        UserDto user = securityUtil.getCurrUser();
        if (user.getPower() != null){
            if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
                String deptId = user.getPower().getDeptId();
                Integer attribute = user.getPower().getAttribute();
                if (attribute == 1){//街道
                    query.put("streetId",deptId);
                }else if (attribute == 2){//社区
                    query.put("communityId",deptId);
                }else if (attribute == 3){//网格
                    query.put("gridId",deptId);
                }
            }
        }
        IPage<GridEvents> GridEventss = gridEventsService.loadAllByQuery(query);
        return ResultUtil.data(GridEventss);
    }

    /**
     * 获取所有任务详情
     *
     * @param query
     * @return
     */
    @PostMapping("/loadByQuery")
    public Result<Object> loadByQuery(@RequestBody JSONObject query) {
        //用户数据权限
        UserDto user = securityUtil.getCurrUser();
        if (user.getPower() != null){
            if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
                String deptId = user.getPower().getDeptId();
                Integer attribute = user.getPower().getAttribute();
                if (attribute == 1){//街道
                    query.put("streetId",deptId);
                }else if (attribute == 2){//社区
                    query.put("communityId",deptId);
                }else if (attribute == 3){//网格
                    query.put("gridId",deptId);
                }
            }
        }
        List<GridEvents> GridEventss = gridEventsService.loadByQuery(query);
        return ResultUtil.data(GridEventss);
    }

    /**
     * 获取所有的状态
     *
     * @param query
     * @return
     */
    @PostMapping("/getAllStatus")
    public Result<Object> getAllStatus(@RequestBody JSONObject query) {
        List<Map<String, Object>> results = gridEventsService.getAllStatus(query);
        return ResultUtil.data(results);
    }

    /**
     * 获取订单详情
     *
     * @param gridEventsId
     * @return
     */
    @GetMapping("/getOrderDetails")
    public Result<Object> getOrderDetails(Long gridEventsId) {
        GridEvents results = gridEventsService.getOrderDetails(gridEventsId);
        return ResultUtil.data(results);
    }

    /**
     * 获取统计
     *
     * @param
     * @return
     */
    @PostMapping("/getStatistics")
    public Result<Object> getStatistics(@RequestBody JSONObject query) {
        //用户数据权限
        UserDto user = securityUtil.getCurrUser();
        if (user.getPower() != null){
            if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
                String deptId = user.getPower().getDeptId();
                Integer attribute = user.getPower().getAttribute();
                if (attribute == 1){//街道
                    query.put("streetId",deptId);
                }else if (attribute == 2){//社区
                    query.put("communityId",deptId);
                }else if (attribute == 3){//网格
                    query.put("gridId",deptId);
                }
            }
        }
        List<Map<String,Object>> results = gridEventsService.getStatistics(query);
        return ResultUtil.data(results);
    }
    /**
     * 获取统计
     *
     * @param
     * @return
     */
    @PostMapping("/getStatisticsPage")
    public Result<Object> getStatisticsPage() {
        List<Map<String,Object>> results = gridEventsService.getStatisticsPage();
        return ResultUtil.data(results);
    }

    /**
     * 获取统计
     *
     * @param
     * @return
     */
    @PostMapping("/getBigData")
    public Result<Object> getBigData(@RequestBody JSONObject query) {
        Map<String,Object> results = gridEventsService.getBigData(query);
        return ResultUtil.data(results);
    }

    /**
     * 统计事件 全部 待处理 已办结 已超时
     * @param searchVo
     * @return
     */
    @GetMapping("/statisticsEvent")
    public Result<Object> statisticsEvent(SearchVo searchVo) {
        JSONObject query = new JSONObject();
        //用户数据权限
        UserDto user = securityUtil.getCurrUser();
        if (user.getPower() != null){
            if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
                String deptId = user.getPower().getDeptId();
                Integer attribute = user.getPower().getAttribute();
                if (attribute == 1){//街道
                    query.put("streetId",deptId);
                }else if (attribute == 2){//社区
                    query.put("communityId",deptId);
                }else if (attribute == 3){//网格
                    query.put("gridId",deptId);
                }
            }
        }
        GridEvents results = gridEventsService.statisticsEvent(searchVo,query);
        return ResultUtil.data(results);
    }

    /**
     * 突发事件概况 统计
     * @param searchVo
     * @return
     */
    @GetMapping("/echartEvent")
    public Result<Object> echartEvent(SearchVo searchVo) {
        JSONObject query = new JSONObject();
        //用户数据权限
        UserDto user = securityUtil.getCurrUser();
        if (user.getPower() != null){
            if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
                String deptId = user.getPower().getDeptId();
                Integer attribute = user.getPower().getAttribute();
                if (attribute == 1){//街道
                    query.put("streetId",deptId);
                }else if (attribute == 2){//社区
                    query.put("communityId",deptId);
                }else if (attribute == 3){//网格
                    query.put("gridId",deptId);
                }
            }
        }
        List<GridEvents> results = gridEventsService.echartEvent(searchVo,query);
        return ResultUtil.data(results);
    }

    /**
     * 网格辖区概况 统计
     * @param searchVo
     * @return
     */
    @GetMapping("/gridStatistics")
    public Result<Object> gridStatistics(SearchVo searchVo) {
        JSONObject query = new JSONObject();
        //用户数据权限
        UserDto user = securityUtil.getCurrUser();
        if (user.getPower() != null){
            if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
                String deptId = user.getPower().getDeptId();
                Integer attribute = user.getPower().getAttribute();
                if (attribute == 1){//街道
                    query.put("streetId",deptId);
                }else if (attribute == 2){//社区
                    query.put("communityId",deptId);
                }else if (attribute == 3){//网格
                    query.put("gridId",deptId);
                }
            }
        }
        GridStatistics results = gridEventsService.gridStatistics(searchVo,query);
        return ResultUtil.data(results);
    }
}
