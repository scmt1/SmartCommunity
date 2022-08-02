package me.zhengjie.dao.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.common.constant.ActivitiConstant;
import me.zhengjie.common.exception.XbootException;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.utils.SecurityUtil;
import me.zhengjie.common.utils.SnowFlakeUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.ActBusiness;
import me.zhengjie.entity.ActProcess;
import me.zhengjie.service.ActBusinessService;
import me.zhengjie.service.ActProcessService;
import me.zhengjie.service.mybatis.IHistoryIdentityService;
import me.zhengjie.service.mybatis.IRunIdentityService;
import me.zhengjie.system.service.UserService;
import me.zhengjie.system.service.dto.UserDto;
import me.zhengjie.utils.MessageUtil;
import me.zhengjie.vo.ActPage;
import me.zhengjie.vo.Assignee;
import me.zhengjie.vo.HistoricTaskVo;
import me.zhengjie.vo.TaskVo;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * @author Exrick
 */
@Slf4j
@RestController
@Api(description = "任务管理接口")
@RequestMapping("/api/actApplyTask")
@Transactional
public class ActApplyTaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ManagementService managementService;

    @Autowired
    private ActProcessService actProcessService;

    @Autowired
    private ActBusinessService actBusinessService;

    @Autowired
    private UserService userService;

    @Autowired
    private IHistoryIdentityService iHistoryIdentityService;

    @Autowired
    private IRunIdentityService iRunIdentityService;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private MessageUtil messageUtil;
    

    @RequestMapping(value = "/todoList", method = RequestMethod.GET)
    @ApiOperation(value = "代办列表")
    public Result<Object> todoList(@RequestParam(required = false) String name,
                                   @RequestParam(required = false) String categoryId,
                                   @RequestParam(required = false) Integer priority,
                                   SearchVo searchVo,
                                   PageVo pageVo){

        ActPage<TaskVo> page = new ActPage<TaskVo>();
        List<TaskVo> list = new ArrayList<>();

        String userId = securityUtil.getCurrUser().getId().toString();
        TaskQuery query = taskService.createTaskQuery().taskCandidateOrAssigned(userId);

        // 多条件搜索
        if("createTime".equals(pageVo.getSort())&&"asc".equals(pageVo.getOrder())){
            query.orderByTaskCreateTime().asc();
        }else if("priority".equals(pageVo.getSort())&&"asc".equals(pageVo.getOrder())){
            query.orderByTaskPriority().asc();
        }else if("priority".equals(pageVo.getSort())&&"desc".equals(pageVo.getOrder())){
            query.orderByTaskPriority().desc();
        }else{
            query.orderByTaskCreateTime().desc();
        }
        if(StrUtil.isNotBlank(name)){
            query.taskNameLike("%"+name+"%");
        }
        if(StrUtil.isNotBlank(categoryId)){
            query.taskCategory(categoryId);
        }
        if(priority!=null){
            query.taskPriority(priority);
        }
        if(StrUtil.isNotBlank(searchVo.getStartDate())&&StrUtil.isNotBlank(searchVo.getEndDate())){
            Date start = DateUtil.parse(searchVo.getStartDate());
            Date end = DateUtil.parse(searchVo.getEndDate());
            query.taskCreatedAfter(start);
            query.taskCreatedBefore(DateUtil.endOfDay(end));
        }

        page.setTotalElements(query.count());
        int first =  (pageVo.getPageNumber()-1) * pageVo.getPageSize();
        List<Task> taskList = query.listPage(first, pageVo.getPageSize());

        // 转换vo
        taskList.forEach(e -> {
            TaskVo tv = new TaskVo(e);

            // 关联委托人
            if(StrUtil.isNotBlank(tv.getOwner())){
                tv.setOwner(userService.findById(Long.parseLong(tv.getOwner())).getUsername());
            }
            List<IdentityLink> identityLinks = runtimeService.getIdentityLinksForProcessInstance(tv.getProcInstId());
            for(IdentityLink ik : identityLinks){
                // 关联发起人
                if("starter".equals(ik.getType())&&StrUtil.isNotBlank(ik.getUserId())){
                    tv.setApplyer(userService.findById(Long.parseLong(ik.getUserId())).getUsername());
                }
            }
            // 关联流程信息
            ActProcess actProcess = actProcessService.get(tv.getProcDefId());
            if(actProcess!=null){
                tv.setProcessName(actProcess.getName());
                tv.setRouteName(actProcess.getRouteName());
            }
            // 关联业务key
            ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(tv.getProcInstId()).singleResult();
            tv.setBusinessKey(pi.getBusinessKey());
            ActBusiness actBusiness = actBusinessService.get(pi.getBusinessKey());

            //Map<String,Object> variables  =  actBusinessYewuService.findByIdOrderBySortOrder(actBusiness.getTableId());
//    		act.getParams().putAll(map);;
            //variables.put("YWcode", "JTLM20200331");
            //tv.setVariables(variables);
            if(actBusiness!=null){
                tv.setTableId(actBusiness.getTableId());
            }

            list.add(tv);
        });
        page.setContent(list);
        return ResultUtil.data(page);
    }

    @RequestMapping(value = "/doneList", method = RequestMethod.GET)
    @ApiOperation(value = "已办列表")
    public Result<Object> doneList(@RequestParam(required = false) String name,
                                   @RequestParam(required = false) String categoryId,
                                   @RequestParam(required = false) Integer priority,
                                   SearchVo searchVo,
                                   PageVo pageVo){

        ActPage<HistoricTaskVo> page = new ActPage<HistoricTaskVo>();
        List<HistoricTaskVo> list = new ArrayList<>();

        String userId = securityUtil.getCurrUser().getId().toString();
        HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery().or().taskCandidateUser(userId).
                taskAssignee(userId).endOr().finished();

        // 多条件搜索
        if("createTime".equals(pageVo.getSort())&&"asc".equals(pageVo.getOrder())){
            query.orderByHistoricTaskInstanceEndTime().asc();
        }else if("priority".equals(pageVo.getSort())&&"asc".equals(pageVo.getOrder())){
            query.orderByTaskPriority().asc();
        }else if("priority".equals(pageVo.getSort())&&"desc".equals(pageVo.getOrder())){
            query.orderByTaskPriority().desc();
        }else if("duration".equals(pageVo.getSort())&&"asc".equals(pageVo.getOrder())){
            query.orderByHistoricTaskInstanceDuration().asc();
        }else if("duration".equals(pageVo.getSort())&&"desc".equals(pageVo.getOrder())){
            query.orderByHistoricTaskInstanceDuration().desc();
        }else{
            query.orderByHistoricTaskInstanceEndTime().desc();
        }
        if(StrUtil.isNotBlank(name)){
            query.taskNameLike("%"+name+"%");
        }
        if(StrUtil.isNotBlank(categoryId)){
            query.taskCategory(categoryId);
        }
        if(priority!=null){
            query.taskPriority(priority);
        }
        if(StrUtil.isNotBlank(searchVo.getStartDate())&&StrUtil.isNotBlank(searchVo.getEndDate())){
            Date start = DateUtil.parse(searchVo.getStartDate());
            Date end = DateUtil.parse(searchVo.getEndDate());
            query.taskCompletedAfter(start);
            query.taskCompletedBefore(DateUtil.endOfDay(end));
        }

        page.setTotalElements((long) query.list().size());
        int first =  (pageVo.getPageNumber()-1) * pageVo.getPageSize();
        List<HistoricTaskInstance> taskList = query.listPage(first, pageVo.getPageSize());
        // 转换vo
        taskList.forEach(e -> {
            HistoricTaskVo htv = new HistoricTaskVo(e);
            // 关联委托人
            if(StrUtil.isNotBlank(htv.getOwner())){
                htv.setOwner(userService.findById(Long.parseLong(htv.getOwner())).getUsername());
            }
            List<HistoricIdentityLink> identityLinks = historyService.getHistoricIdentityLinksForProcessInstance(htv.getProcInstId());
            for(HistoricIdentityLink hik : identityLinks){
                // 关联发起人
                if("starter".equals(hik.getType())&&StrUtil.isNotBlank(hik.getUserId())){
                    htv.setApplyer(userService.findById(Long.parseLong(hik.getUserId())).getUsername());
                }
            }
            // 关联审批意见
            List<Comment> comments = taskService.getTaskComments(htv.getId(), "comment");
            if(comments!=null&&comments.size()>0){
                htv.setComment(comments.get(0).getFullMessage());
            }
            // 关联流程信息
            ActProcess actProcess = actProcessService.get(htv.getProcDefId());
            if(actProcess!=null){
                htv.setProcessName(actProcess.getName());
                htv.setRouteName(actProcess.getRouteName());
            }
            // 关联业务key
            HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery().processInstanceId(htv.getProcInstId()).singleResult();
            htv.setBusinessKey(hpi.getBusinessKey());
            ActBusiness actBusiness = actBusinessService.get(hpi.getBusinessKey());
            if(actBusiness!=null){
                htv.setTableId(actBusiness.getTableId());
            }

            list.add(htv);
        });
        page.setContent(list);
        return ResultUtil.data(page);
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
                               @ApiParam("下个节点审批人") @RequestParam(required = false) String[] assignees,
                               @ApiParam("优先级") @RequestParam(required = false) Integer priority,
                               @ApiParam("意见评论") @RequestParam(required = false) String comment,
                               @ApiParam("是否发送站内消息") @RequestParam(defaultValue = "false") Boolean sendMessage,
                               @ApiParam("是否发送短信通知") @RequestParam(defaultValue = "false") Boolean sendSms,
                               @ApiParam("是否发送邮件通知") @RequestParam(defaultValue = "false") Boolean sendEmail){

        if(StrUtil.isBlank(comment)){
            comment = "";
        }
        taskService.addComment(id, procInstId, comment);
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
                        ActBusiness actBusiness = actBusinessService.get(pi.getBusinessKey());
                        actBusiness.setStatus(ActivitiConstant.STATUS_CANCELED);
                        actBusiness.setResult(ActivitiConstant.RESULT_TO_SUBMIT);
                        actBusinessService.update(actBusiness);
                        break;
                    }else{
                        // 避免重复添加
                        List<String> list = iRunIdentityService.selectByConditions(t.getId(), "candidate");
                        if(list==null||list.size()==0) {
                            // 分配了节点负责人分发给全部
                            for (UserDto user : users) {
                                taskService.addCandidateUser(t.getId(), user.getId().toString());
                                // 异步发消息
                                messageUtil.sendActMessage(user.getId().toString(), ActivitiConstant.MESSAGE_TODO_CONTENT, sendMessage, sendSms, sendEmail);
                            }
                            taskService.setPriority(t.getId(), task.getPriority());
                        }
                    }
                }else{
                    // 避免重复添加
                    List<String> list = iRunIdentityService.selectByConditions(t.getId(), "candidate");
                    if(list==null||list.size()==0) {
                        for(String assignee : assignees){
                            taskService.addCandidateUser(t.getId(), assignee);
                            // 异步发消息
                            messageUtil.sendActMessage(assignee, ActivitiConstant.MESSAGE_TODO_CONTENT, sendMessage, sendSms, sendEmail);
                            taskService.setPriority(t.getId(), priority);
                        }
                    }
                }
            }
        } else {
            ActBusiness actBusiness = actBusinessService.get(pi.getBusinessKey());
            actBusiness.setStatus(ActivitiConstant.STATUS_FINISH);
            actBusiness.setResult(ActivitiConstant.RESULT_PASS);
            actBusinessService.update(actBusiness);
            // 异步发消息
            messageUtil.sendActMessage(actBusiness.getUserId(), ActivitiConstant.MESSAGE_PASS_CONTENT, sendMessage, sendSms, sendEmail);
        }
        // 记录实际审批人员
        iHistoryIdentityService.insert(String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId()),
                ActivitiConstant.EXECUTOR_TYPE, securityUtil.getCurrUser().getId().toString(), id, procInstId);
        return ResultUtil.success("操作成功");
    }

    @RequestMapping(value = "/passAll/{ids}", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过")
    public Result<Object> passAll(@ApiParam("任务ids") @PathVariable String[] ids,
                                  @ApiParam("是否发送站内消息") @RequestParam(defaultValue = "false") Boolean sendMessage,
                                  @ApiParam("是否发送短信通知") @RequestParam(defaultValue = "false") Boolean sendSms,
                                  @ApiParam("是否发送邮件通知") @RequestParam(defaultValue = "false") Boolean sendEmail){

        for(String id:ids){
            Task task = taskService.createTaskQuery().taskId(id).singleResult();
            ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
            if(StrUtil.isNotBlank(task.getOwner())&&!("RESOLVED").equals(task.getDelegationState().toString())){
                String oldAssignee = task.getAssignee();
                taskService.resolveTask(id);
                taskService.setAssignee(id, oldAssignee);
            }
            taskService.complete(id);
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).list();
            // 判断下一个节点
            if(tasks!=null&&tasks.size()>0){
                for(Task t : tasks){
                    List<UserDto> users = actProcessService.getNode(t.getTaskDefinitionKey()).getUsers();
                    // 如果下个节点未分配审批人为空 取消结束流程
                    if(users==null||users.size()==0){
                        runtimeService.deleteProcessInstance(pi.getId(), "canceled-审批节点未分配审批人，流程自动中断取消");
                        ActBusiness actBusiness = actBusinessService.get(pi.getBusinessKey());
                        actBusiness.setStatus(ActivitiConstant.STATUS_CANCELED);
                        actBusiness.setResult(ActivitiConstant.RESULT_TO_SUBMIT);
                        actBusinessService.update(actBusiness);
                        break;
                    }else{
                        // 避免重复添加
                        List<String> list = iRunIdentityService.selectByConditions(t.getId(), "candidate");
                        if(list==null||list.size()==0) {
                            // 分配了节点负责人分发给全部
                            for (UserDto user : users) {
                                taskService.addCandidateUser(t.getId(), user.getId().toString());
                                // 异步发消息
                                messageUtil.sendActMessage(user.getId().toString(), ActivitiConstant.MESSAGE_TODO_CONTENT, sendMessage, sendSms, sendEmail);
                                taskService.setPriority(t.getId(), task.getPriority());
                            }
                        }
                    }
                }
            } else {
                ActBusiness actBusiness = actBusinessService.get(pi.getBusinessKey());
                actBusiness.setStatus(ActivitiConstant.STATUS_FINISH);
                actBusiness.setResult(ActivitiConstant.RESULT_PASS);
                actBusinessService.update(actBusiness);
                // 异步发消息
                messageUtil.sendActMessage(actBusiness.getUserId(), ActivitiConstant.MESSAGE_PASS_CONTENT, sendMessage, sendSms, sendEmail);
            }
            // 记录实际审批人员
            iHistoryIdentityService.insert(String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId()),
                    ActivitiConstant.EXECUTOR_TYPE, securityUtil.getCurrUser().getId().toString(), id, pi.getId());
        }
        return ResultUtil.success("操作成功");
    }

    @RequestMapping(value = "/getBackList/{procInstId}", method = RequestMethod.GET)
    @ApiOperation(value = "获取可返回的节点")
    public Result<Object> getBackList(@PathVariable String procInstId){

        List<HistoricTaskVo> list = new ArrayList<>();
        List<HistoricTaskInstance> taskInstanceList = historyService.createHistoricTaskInstanceQuery().processInstanceId(procInstId)
                .finished().list();

        taskInstanceList.forEach(e -> {
            HistoricTaskVo htv = new HistoricTaskVo(e);
            list.add(htv);
        });

        // 去重
        LinkedHashSet<String> set = new LinkedHashSet<String>(list.size());
        List<HistoricTaskVo> newList = new ArrayList<>();
        list.forEach(e->{
            if(set.add(e.getName())){
                newList.add(e);
            }
        });

        return ResultUtil.data(newList);
    }

    @RequestMapping(value = "/backToTask", method = RequestMethod.POST)
    @ApiOperation(value = "任务节点审批驳回至指定历史节点")
    public Result<Object> backToTask(@ApiParam("任务id") @RequestParam String id,
                                     @ApiParam("驳回指定节点key") @RequestParam String backTaskKey,
                                     @ApiParam("流程实例id") @RequestParam String procInstId,
                                     @ApiParam("流程定义id") @RequestParam String procDefId,
                                     @ApiParam("原节点审批人") @RequestParam(required = false) String[] assignees,
                                     @ApiParam("优先级") @RequestParam(required = false) Integer priority,
                                     @ApiParam("意见评论") @RequestParam(required = false) String comment,
                                     @ApiParam("是否发送站内消息") @RequestParam(defaultValue = "false") Boolean sendMessage,
                                     @ApiParam("是否发送短信通知") @RequestParam(defaultValue = "false") Boolean sendSms,
                                     @ApiParam("是否发送邮件通知") @RequestParam(defaultValue = "false") Boolean sendEmail){


        if(StrUtil.isBlank(comment)){
            comment = "";
        }
        taskService.addComment(id, procInstId, comment);
        // 取得流程定义
        ProcessDefinitionEntity definition = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(procDefId);
        // 获取历史任务的Activity
        ActivityImpl hisActivity = definition.findActivity(backTaskKey);
        // 实现跳转
        managementService.executeCommand(new JumpTask(procInstId, hisActivity.getId()));
        // 重新分配原节点审批人
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(procInstId).list();
        if(tasks!=null&&tasks.size()>0){
            tasks.forEach(e->{
                for(String assignee:assignees){
                    taskService.addCandidateUser(e.getId(), assignee);
                    // 异步发消息
                    messageUtil.sendActMessage(assignee, ActivitiConstant.MESSAGE_TODO_CONTENT, sendMessage, sendSms, sendEmail);
                }
                if(priority!=null){
                    taskService.setPriority(e.getId(), priority);
                }
            });
        }
        // 记录实际审批人员
        iHistoryIdentityService.insert(String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId()),
                ActivitiConstant.EXECUTOR_TYPE, securityUtil.getCurrUser().getId().toString(), id, procInstId);
        return ResultUtil.success("操作成功");
    }

    public class JumpTask implements Command<ExecutionEntity> {

        private String procInstId;
        private String activityId;

        public JumpTask(String procInstId, String activityId) {
            this.procInstId = procInstId;
            this.activityId = activityId;
        }

        @Override
        public ExecutionEntity execute(CommandContext commandContext) {

            ExecutionEntity executionEntity = commandContext.getExecutionEntityManager().findExecutionById(procInstId);
            executionEntity.destroyScope("backed");
            ProcessDefinitionImpl processDefinition = executionEntity.getProcessDefinition();
            ActivityImpl activity = processDefinition.findActivity(activityId);
            executionEntity.executeActivity(activity);

            return executionEntity;
        }
    }

    @RequestMapping(value = "/back", method = RequestMethod.POST)
    @ApiOperation(value = "任务节点审批驳回至发起人")
    public Result<Object> back(@ApiParam("任务id") @RequestParam String id,
                               @ApiParam("流程实例id") @RequestParam String procInstId,
                               @ApiParam("意见评论") @RequestParam(required = false) String comment,
                               @ApiParam("是否发送站内消息") @RequestParam(defaultValue = "false") Boolean sendMessage,
                               @ApiParam("是否发送短信通知") @RequestParam(defaultValue = "false") Boolean sendSms,
                               @ApiParam("是否发送邮件通知") @RequestParam(defaultValue = "false") Boolean sendEmail){


        if(StrUtil.isBlank(comment)){
            comment = "";
        }
        taskService.addComment(id, procInstId, comment);
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(procInstId).singleResult();
        // 删除流程实例
        runtimeService.deleteProcessInstance(procInstId, "backed");
        ActBusiness actBusiness = actBusinessService.get(pi.getBusinessKey());
        actBusiness.setStatus(ActivitiConstant.STATUS_FINISH);
        actBusiness.setResult(ActivitiConstant.RESULT_FAIL);
        actBusinessService.update(actBusiness);
        // 异步发消息
        messageUtil.sendActMessage(actBusiness.getUserId(), ActivitiConstant.MESSAGE_BACK_CONTENT, sendMessage, sendSms, sendEmail);
        // 记录实际审批人员
        iHistoryIdentityService.insert(String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId()),
                ActivitiConstant.EXECUTOR_TYPE, securityUtil.getCurrUser().getId().toString(), id, procInstId);
        return ResultUtil.success("操作成功");
    }

    @RequestMapping(value = "/backAll/{procInstIds}", method = RequestMethod.POST)
    @ApiOperation(value = "批量驳回至发起人")
    public Result<Object> backAll(@ApiParam("流程实例ids") @PathVariable String[] procInstIds,
                                  @ApiParam("是否发送站内消息") @RequestParam(defaultValue = "false") Boolean sendMessage,
                                  @ApiParam("是否发送短信通知") @RequestParam(defaultValue = "false") Boolean sendSms,
                                  @ApiParam("是否发送邮件通知") @RequestParam(defaultValue = "false") Boolean sendEmail){

        for(String procInstId:procInstIds){
            // 记录实际审批人员
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(procInstId).list();
            tasks.forEach(t->{
                iHistoryIdentityService.insert(String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId()),
                        ActivitiConstant.EXECUTOR_TYPE, securityUtil.getCurrUser().getId().toString(), t.getId(), procInstId);
            });
            ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(procInstId).singleResult();
            // 删除流程实例
            try {
                runtimeService.deleteProcessInstance(procInstId, ActivitiConstant.BACKED_FLAG);
            }catch (Exception e){
                throw new XbootException("请确保无重复所属的流程，或尝试对单条数据进行驳回");
            }
            ActBusiness actBusiness = actBusinessService.get(pi.getBusinessKey());
            actBusiness.setStatus(ActivitiConstant.STATUS_FINISH);
            actBusiness.setResult(ActivitiConstant.RESULT_FAIL);
            actBusinessService.update(actBusiness);
            // 异步发消息
            messageUtil.sendActMessage(actBusiness.getUserId(), ActivitiConstant.MESSAGE_BACK_CONTENT, sendMessage, sendSms, sendEmail);
        }
        return ResultUtil.success("操作成功");
    }

    @RequestMapping(value = "/delegate", method = RequestMethod.POST)
    @ApiOperation(value = "委托他人代办")
    public Result<Object> delegate(@ApiParam("任务id") @RequestParam String id,
                                   @ApiParam("委托用户id") @RequestParam String userId,
                                   @ApiParam("流程实例id") @RequestParam String procInstId,
                                   @ApiParam("意见评论") @RequestParam(required = false) String comment,
                                   @ApiParam("是否发送站内消息") @RequestParam(defaultValue = "false") Boolean sendMessage,
                                   @ApiParam("是否发送短信通知") @RequestParam(defaultValue = "false") Boolean sendSms,
                                   @ApiParam("是否发送邮件通知") @RequestParam(defaultValue = "false") Boolean sendEmail){

        if(StrUtil.isBlank(comment)){
            comment = "";
        }
        taskService.addComment(id, procInstId, comment);
        taskService.delegateTask(id, userId);
        taskService.setOwner(id, securityUtil.getCurrUser().getId().toString());
        // 异步发消息
        messageUtil.sendActMessage(userId, ActivitiConstant.MESSAGE_DELEGATE_CONTENT, sendMessage, sendSms, sendEmail);
        return ResultUtil.success("操作成功");
    }

    @RequestMapping(value = "/delete/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除任务")
    public Result<Object> delete(@ApiParam("任务id") @PathVariable String[] ids,
                                 @ApiParam("原因") @RequestParam(required = false) String reason){

        if(StrUtil.isBlank(reason)){
            reason = "";
        }
        for(String id : ids){
            taskService.deleteTask(id, reason);
        }
        return ResultUtil.success("操作成功");
    }

    @RequestMapping(value = "/deleteHistoric/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除任务历史")
    public Result<Object> deleteHistoric(@ApiParam("任务id") @PathVariable String[] ids){

        for(String id : ids){
            historyService.deleteHistoricTaskInstance(id);
        }
        return ResultUtil.success("操作成功");
    }
}
