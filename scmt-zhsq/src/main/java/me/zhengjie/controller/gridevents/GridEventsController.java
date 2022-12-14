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
     * ?????????????????? ???????????? ????????????
     *
     * @param event
     * @return
     */
    @PostMapping("/addEvents")
    public Result<Object> addEvents(@RequestBody GridEvents event) throws Exception {
        /*???????????????*/
        event.setIsDeleted(0);
        event.setCreateTime(new Timestamp(System.currentTimeMillis()));
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        event.setCode(sdf.format(date));
        gridEventsService.save(event);
        //????????????????????????
        if(event.getEventsTypeId()==0||event.getEventsTypeId()==null){
            return ResultUtil.error("??????????????????");
        }
        /*??????????????????????????????????????????*/
        if(event.getEventsTypes()==null|| event.getEventsTypes().getId()==null){
            EventsType eventsType = eventsTypeService.getById(event.getEventsTypeId());
            if(eventsType==null||StringUtils.isBlank(eventsType.getProcDefId())){
                return ResultUtil.error("??????????????????????????????????????????????????????");
            }
            event.setEventsTypes(eventsType);
        }
        ActProcess actProcess = actProcessService.get(event.getEventsTypes().getProcDefId());
        if(actProcess==null || StringUtils.isBlank(actProcess.getId())){
            return ResultUtil.error("?????????????????????");
        }

        /*??????????????????id*/
        ActBusiness actBusiness =new ActBusiness();
        //?????????id
        actBusiness.setTableId(""+event.getId());
        //??????(????????????)
        actBusiness.getParams().put("test", "tets");
        //????????????id
        actBusiness.setProcDefId(event.getEventsTypes().getProcDefId());
        //id
        actBusiness.setId(""+event.getId());
        //??????(????????????)
        actBusiness.setTitle(event.getTitle());
        //??????????????????????????????
        actBusiness.setFirstGateway(true);
        //????????????????????????
        actBusiness.setSendMessage(true);
        //????????????????????????
        actBusiness.setSendSms(true);
        //?????????
        actBusiness.setPriority(0);
        //????????????id
        String processInstanceId = actProcessService.startProcess(actBusiness);

        /*??????????????????*/
        GridEvents events = new GridEvents();
        //????????????????????? ??????id ?????????
        List<HistoricTaskInstance> taskList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId).orderByHistoricTaskInstanceEndTime().asc().list();
        taskList.forEach(e -> {
            if(events.getNodeId() == null && events.getNodeName() == null){
                //??????id ????????????
                events.setNodeId(e.getTaskDefinitionKey());
                //????????? ????????????
                events.setNodeName(e.getName());
            }
            //???????????? ??????????????? ????????????
            if(StringUtils.isNotBlank(event.getMediaAddress())){
                String[] mediaAddress = event.getMediaAddress().split(";");
                String mediaAddressNew = "";
                for(int i = 0;i < mediaAddress.length;i ++){
                    mediaAddressNew += "'nodeId':'"+e.getTaskDefinitionKey()+"','nodeName':'"+e.getName()+"','url':'"+mediaAddress[i]+"';";
                }
                events.setMediaAddress(mediaAddressNew);
            }
            //??????
            List<HistoricIdentityLink> identityLinks = historyService.getHistoricIdentityLinksForTask(e.getId());
            String userIds = "";
            int x = 1;
            for(HistoricIdentityLink hik : identityLinks){
                // ????????????????????????????????????????????????
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
            //????????? ????????????
            events.setExecutor(userIds);
        });
        //id
        events.setId(event.getId());
        //????????????id ????????????
        events.setProcInstId(processInstanceId);
        //?????? ????????????
        events.setStatus(1);
        gridEventsService.updateById(events);

        return ResultUtil.success("????????????");
    }

    /**
     * ?????????????????? ???????????? ????????????
     *
     * @param event
     * @return
     */
    @PostMapping("/addEventsSelf")
    public Result<Object> addEventsSelf(@RequestBody GridEvents event) throws Exception {
        /*???????????????*/
        event.setHandleSelf(event.getCreateUser());
        event.setIsDeleted(0);
        event.setCreateTime(new Timestamp(System.currentTimeMillis()));
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        event.setCode(sdf.format(date));
        gridEventsService.save(event);

        /*????????????*/
        ActProcess actProcess = actProcessService.get(event.getEventsTypes().getProcDefId());
        if(actProcess==null || StringUtils.isBlank(actProcess.getId())){
            return ResultUtil.error("?????????????????????");
        }

        /*??????????????????id*/
        ActBusiness actBusiness =new ActBusiness();
        //?????????id
        actBusiness.setTableId(""+event.getId());
        //??????(????????????)
        actBusiness.getParams().put("test", "tets");
        //????????????id
        actBusiness.setProcDefId(event.getEventsTypes().getProcDefId());
        //id
        actBusiness.setId(""+event.getId());
        //??????(????????????)
        actBusiness.setTitle(event.getTitle());
        //??????????????????????????????
        actBusiness.setFirstGateway(true);
        //????????????????????????
        actBusiness.setSendMessage(true);
        //????????????????????????
        actBusiness.setSendSms(true);
        //?????????
        actBusiness.setPriority(0);
        //????????????id
        String processInstanceId = actProcessService.startProcess(actBusiness);

        /*??????????????????*/
        GridEvents events = new GridEvents();
        //????????????????????? ??????id ?????????
        List<HistoricTaskInstance> taskList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId).orderByHistoricTaskInstanceEndTime().asc().list();
        taskList.forEach(e -> {
            if(events.getNodeId() == null && events.getNodeName() == null){
                //??????id ????????????
                events.setNodeId(e.getTaskDefinitionKey());
                //????????? ????????????
                events.setNodeName(e.getName());
            }
            //???????????? ??????????????? ????????????
            if(StringUtils.isNotBlank(event.getMediaAddress())){
                String[] mediaAddress = event.getMediaAddress().split(";");
                String mediaAddressNew = "";
                for(int i = 0;i < mediaAddress.length;i ++){
                    mediaAddressNew += "'nodeId':'"+e.getTaskDefinitionKey()+"','nodeName':'"+e.getName()+"','url':'"+mediaAddress[i]+"';";
                }
                events.setMediaAddress(mediaAddressNew);
            }
            //????????? ????????????(????????????,????????????????????????)
            events.setExecutor("" + event.getCreateUser());
        });
        //id
        events.setId(event.getId());
        //????????????id ????????????
        events.setProcInstId(processInstanceId);
        //?????? ????????????
        events.setStatus(2);
        gridEventsService.updateById(events);

        return ResultUtil.success("????????????");
    }

    @RequestMapping(value = "/historicFlow/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "??????????????????")
    public Result<Object> historicFlow(@ApiParam("????????????id") @PathVariable String id){

        List<HistoricTaskVo> list = new ArrayList<>();

        List<HistoricTaskInstance> taskList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(id).orderByHistoricTaskInstanceEndTime().asc().list();

        // ??????vo
        taskList.forEach(e -> {
            HistoricTaskVo htv = new HistoricTaskVo(e);
            List<Assignee> assignees = new ArrayList<>();
            // ????????????????????????????????????????????????
            if(StrUtil.isNotBlank(htv.getAssignee())&& StrUtil.isNotBlank(htv.getOwner())){
                String assignee = userService.findById(Long.parseLong(htv.getAssignee())).getUsername();
                String owner = userService.findById(Long.parseLong(htv.getOwner())).getUsername();
                assignees.add(new Assignee(assignee+"(???"+owner+"??????)", true));
            }
            List<HistoricIdentityLink> identityLinks = historyService.getHistoricIdentityLinksForTask(e.getId());
            // ????????????????????????id
            String userId = iHistoryIdentityService.findUserIdByTypeAndTaskId(ActivitiConstant.EXECUTOR_TYPE, e.getId());
            for(HistoricIdentityLink hik : identityLinks){
                // ??????????????????????????????????????????????????????
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
            // ??????????????????
            List<Comment> comments = taskService.getTaskComments(htv.getId(), "comment");
            if(comments!=null&&comments.size()>0){
                htv.setComment(comments.get(0).getFullMessage());
            }
            list.add(htv);
        });
        return ResultUtil.data(list);
    }

    @RequestMapping(value = "/pass", method = RequestMethod.POST)
    @ApiOperation(value = "????????????????????????")
    public Result<Object> pass(@ApiParam("??????id") @RequestParam String id,
                               @ApiParam("????????????id") @RequestParam String procInstId,
                               @ApiParam("?????????") @RequestParam String assigneeId,
                               @ApiParam("?????????????????????") @RequestParam(required = false) String[] assignees,
                               @ApiParam("?????????") @RequestParam(required = false) Integer priority,
                               @ApiParam("????????????") @RequestParam(required = false) String comment,
                               @ApiParam("????????????????????????") @RequestParam(defaultValue = "false") Boolean sendMessage,
                               @ApiParam("????????????????????????") @RequestParam(defaultValue = "false") Boolean sendSms,
                               @ApiParam("????????????????????????") @RequestParam(defaultValue = "false") Boolean sendEmail){

        if(StrUtil.isBlank(comment)){
            comment = "";
        }
        //??????????????????
        taskService.addComment(id, procInstId, comment);
        if(StringUtils.isNotBlank(assigneeId)){
            //???????????????
            taskService.setAssignee(id, assigneeId);
        }
        QueryWrapper<GridEvents> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(i -> i.like("grid_events.proc_inst_id", procInstId));
        queryWrapper.and(i -> i.like("grid_events.is_deleted", 0));
        GridEvents gridEvents = gridEventsService.getOne(queryWrapper);
        if(gridEvents==null || gridEvents.getId() ==null){
            return  ResultUtil.error("?????????????????????");

        }
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(procInstId).singleResult();
        Task task = taskService.createTaskQuery().taskId(id).singleResult();
        if(StrUtil.isNotBlank(task.getOwner())&&!("RESOLVED").equals(task.getDelegationState().toString())){
            // ???????????????????????? ???resolve
            String oldAssignee = task.getAssignee();
            taskService.resolveTask(id);
            taskService.setAssignee(id, oldAssignee);
        }
        taskService.complete(id);
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(procInstId).list();
        // ?????????????????????
        if(tasks!=null&&tasks.size()>0){
            for(Task t : tasks){
                if(assignees==null||assignees.length<1){
                    // ?????????????????????????????????????????? ??????????????????
                    List<UserDto> users = actProcessService.getNode(t.getTaskDefinitionKey()).getUsers();
                    if(users==null||users.size()==0){
                        runtimeService.deleteProcessInstance(procInstId, "canceled-?????????????????????????????????????????????????????????");
//                        ActBusiness actBusiness = actBusinessService.get(pi.getBusinessKey());
////                        actBusiness.setStatus(ActivitiConstant.STATUS_CANCELED);
////                        actBusiness.setResult(ActivitiConstant.RESULT_TO_SUBMIT);
////                        actBusinessService.update(actBusiness);
                        gridEvents.setStatus(ActivitiConstant.STATUS_CANCELED);
                        gridEventsService.updateById(gridEvents);
                        break;
                    }else{
                        // ??????????????????
                        List<String> list = iRunIdentityService.selectByConditions(t.getId(), "candidate");
                        if(list==null||list.size()==0) {
                            String userId = "";
                            // ???????????????????????????????????????
                            for (UserDto user : users) {
                                taskService.addCandidateUser(t.getId(), user.getId().toString());
                                userId = userId+user.getId()+",";
                                // ???????????????
                                messageUtil.sendActMessage(user.getId().toString(), ActivitiConstant.MESSAGE_TODO_CONTENT, sendMessage, sendSms, sendEmail);
                            }

                            taskService.setPriority(t.getId(), task.getPriority());
                            gridEvents.setExecutor(userId);
                            gridEvents.setStatus(ActivitiConstant.STATUS_DEALING);
                            gridEventsService.updateById(gridEvents);
                        }
                    }
                }else{
                    // ??????????????????
                    List<String> list = iRunIdentityService.selectByConditions(t.getId(), "candidate");
                    if(list==null||list.size()==0) {
                        String userId = "";
                        for(String assignee : assignees){
                            taskService.addCandidateUser(t.getId(), assignee);
                            // ???????????????
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
        } else {//?????????????????????
            gridEvents.setExecutor(" ");
            gridEvents.setEndDate(new Timestamp(System.currentTimeMillis()));
            gridEvents.setStatus(ActivitiConstant.STATUS_FINISH);
            gridEventsService.updateById(gridEvents);
            // ???????????????
            messageUtil.sendActMessage(gridEvents.getCreateUser()+"", ActivitiConstant.MESSAGE_PASS_CONTENT, sendMessage, sendSms, sendEmail);
        }
        // ????????????????????????
        iHistoryIdentityService.insert(String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId()),
                ActivitiConstant.EXECUTOR_TYPE, securityUtil.getCurrUser().getId().toString(), id, procInstId);
        return ResultUtil.success("????????????");
    }

    /**
     * ?????????????????????????????? ??????????????????
     * ?????????????????????????????????????????? ???????????????
     * @param act
     * @return
     */
    public ActBusiness putParams(ActBusiness act){

        if(StrUtil.isBlank(act.getTableId())){
            throw new XbootException("???????????????TableId????????????");
        }
        // ????????????????????????
        Leave leave = leaveService.get(act.getTableId());
        if(leave!=null){
            // ????????????
            act.getParams().put("duration", leave.getDuration());
        }
        return act;
    }

    /**
     * ??????????????????
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
     * ??????????????????
     *
     * @param gridEvents
     * @return
     * @throws Exception
     */
    @PostMapping("/modify")
    public Result<Object> modify(@RequestBody GridEvents gridEvents) throws Exception {
        //???????????? ??????????????? ????????????
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
     * ????????????????????????
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
     * ??????????????????
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
     * ????????????????????????
     *
     * @param query
     * @return
     */
    @PostMapping("/loadAllByQuery")
    public Result<Object> loadAllByQuery(@RequestBody JSONObject query) {
        //??????????????????
        UserDto user = securityUtil.getCurrUser();
        if (user.getPower() != null){
            if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
                String deptId = user.getPower().getDeptId();
                Integer attribute = user.getPower().getAttribute();
                if (attribute == 1){//??????
                    query.put("streetId",deptId);
                }else if (attribute == 2){//??????
                    query.put("communityId",deptId);
                }else if (attribute == 3){//??????
                    query.put("gridId",deptId);
                }
            }
        }
        IPage<GridEvents> GridEventss = gridEventsService.loadAllByQuery(query);
        return ResultUtil.data(GridEventss);
    }

    /**
     * ????????????????????????
     *
     * @param query
     * @return
     */
    @PostMapping("/loadByQuery")
    public Result<Object> loadByQuery(@RequestBody JSONObject query) {
        //??????????????????
        UserDto user = securityUtil.getCurrUser();
        if (user.getPower() != null){
            if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
                String deptId = user.getPower().getDeptId();
                Integer attribute = user.getPower().getAttribute();
                if (attribute == 1){//??????
                    query.put("streetId",deptId);
                }else if (attribute == 2){//??????
                    query.put("communityId",deptId);
                }else if (attribute == 3){//??????
                    query.put("gridId",deptId);
                }
            }
        }
        List<GridEvents> GridEventss = gridEventsService.loadByQuery(query);
        return ResultUtil.data(GridEventss);
    }

    /**
     * ?????????????????????
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
     * ??????????????????
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
     * ????????????
     *
     * @param
     * @return
     */
    @PostMapping("/getStatistics")
    public Result<Object> getStatistics(@RequestBody JSONObject query) {
        //??????????????????
        UserDto user = securityUtil.getCurrUser();
        if (user.getPower() != null){
            if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
                String deptId = user.getPower().getDeptId();
                Integer attribute = user.getPower().getAttribute();
                if (attribute == 1){//??????
                    query.put("streetId",deptId);
                }else if (attribute == 2){//??????
                    query.put("communityId",deptId);
                }else if (attribute == 3){//??????
                    query.put("gridId",deptId);
                }
            }
        }
        List<Map<String,Object>> results = gridEventsService.getStatistics(query);
        return ResultUtil.data(results);
    }
    /**
     * ????????????
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
     * ????????????
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
     * ???????????? ?????? ????????? ????????? ?????????
     * @param searchVo
     * @return
     */
    @GetMapping("/statisticsEvent")
    public Result<Object> statisticsEvent(SearchVo searchVo) {
        JSONObject query = new JSONObject();
        //??????????????????
        UserDto user = securityUtil.getCurrUser();
        if (user.getPower() != null){
            if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
                String deptId = user.getPower().getDeptId();
                Integer attribute = user.getPower().getAttribute();
                if (attribute == 1){//??????
                    query.put("streetId",deptId);
                }else if (attribute == 2){//??????
                    query.put("communityId",deptId);
                }else if (attribute == 3){//??????
                    query.put("gridId",deptId);
                }
            }
        }
        GridEvents results = gridEventsService.statisticsEvent(searchVo,query);
        return ResultUtil.data(results);
    }

    /**
     * ?????????????????? ??????
     * @param searchVo
     * @return
     */
    @GetMapping("/echartEvent")
    public Result<Object> echartEvent(SearchVo searchVo) {
        JSONObject query = new JSONObject();
        //??????????????????
        UserDto user = securityUtil.getCurrUser();
        if (user.getPower() != null){
            if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
                String deptId = user.getPower().getDeptId();
                Integer attribute = user.getPower().getAttribute();
                if (attribute == 1){//??????
                    query.put("streetId",deptId);
                }else if (attribute == 2){//??????
                    query.put("communityId",deptId);
                }else if (attribute == 3){//??????
                    query.put("gridId",deptId);
                }
            }
        }
        List<GridEvents> results = gridEventsService.echartEvent(searchVo,query);
        return ResultUtil.data(results);
    }

    /**
     * ?????????????????? ??????
     * @param searchVo
     * @return
     */
    @GetMapping("/gridStatistics")
    public Result<Object> gridStatistics(SearchVo searchVo) {
        JSONObject query = new JSONObject();
        //??????????????????
        UserDto user = securityUtil.getCurrUser();
        if (user.getPower() != null){
            if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
                String deptId = user.getPower().getDeptId();
                Integer attribute = user.getPower().getAttribute();
                if (attribute == 1){//??????
                    query.put("streetId",deptId);
                }else if (attribute == 2){//??????
                    query.put("communityId",deptId);
                }else if (attribute == 3){//??????
                    query.put("gridId",deptId);
                }
            }
        }
        GridStatistics results = gridEventsService.gridStatistics(searchVo,query);
        return ResultUtil.data(results);
    }
}
