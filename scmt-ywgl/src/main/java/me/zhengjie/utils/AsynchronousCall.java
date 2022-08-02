package me.zhengjie.utils;

import me.zhengjie.common.constant.ActivitiConstant;
import me.zhengjie.entity.ActBusiness;
import me.zhengjie.service.ActBusinessService;
import me.zhengjie.service.ActProcessService;
import me.zhengjie.service.mybatis.IRunIdentityService;
import me.zhengjie.system.service.dto.UserDto;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AsynchronousCall {
    @Autowired
    private TaskService taskService;
    @Autowired
    private ActProcessService actProcessService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private ActBusinessService actBusinessService;

    @Autowired
    private IRunIdentityService iRunIdentityService;

    @Autowired
    private MessageUtil messageUtil;

    @Async
    public void call(String procInstId, String[] assignees, Integer priority, Boolean sendMessage, Boolean sendSms, Boolean sendEmail){
        if(sendMessage||sendSms||sendEmail){
        	ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(procInstId).singleResult();
            List<Task> tasks = taskService.createTaskQuery() .processInstanceId(procInstId).list();
            // 判断下一个节点
            if (tasks != null && tasks.size() > 0) {
                for (Task t : tasks) {
                    if (assignees == null || assignees.length < 1) {
                        // 如果下个节点未分配审批人为空 取消结束流程
                        List<UserDto> users = actProcessService.getNode(t.getTaskDefinitionKey()).getUsers();
                        if (users == null || users.size() == 0) {
                            runtimeService.deleteProcessInstance(procInstId, "canceled-审批节点未分配审批人，流程自动中断取消");
                            ActBusiness actBusiness = actBusinessService.get(pi.getBusinessKey());
                            actBusiness.setStatus(ActivitiConstant.STATUS_CANCELED);
                            actBusiness.setResult(ActivitiConstant.RESULT_TO_SUBMIT);
                            actBusinessService.update(actBusiness);
                            break;
                        } else {
                            // 避免重复添加
                            List<String> list = iRunIdentityService.selectByConditions(t.getId(), "candidate");
                            if (list == null || list.size() == 0) {
                                // 分配了节点负责人分发给全部
                                for (UserDto user : users) {
                                    taskService.addCandidateUser(t.getId(), user.getId().toString());
                                    // 异步发消息
                                    messageUtil.sendActMessage(user.getId().toString(), ActivitiConstant.MESSAGE_TODO_CONTENT, sendMessage, sendSms, sendEmail);
                                }
                                taskService.setPriority(t.getId(), priority);
                            }
                        }
                    } else {
                        // 避免重复添加
                        List<String> list = iRunIdentityService.selectByConditions(t.getId(), "candidate");
                        if (list == null || list.size() == 0) {
                            for (String assignee : assignees) {
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
        }
    }
}
