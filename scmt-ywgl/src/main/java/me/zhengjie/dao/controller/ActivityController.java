package me.zhengjie.dao.controller;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.aop.log.Log;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.utils.SecurityUtil;
import me.zhengjie.common.vo.Result;
import me.zhengjie.dao.entity.TFormGenerator;
import me.zhengjie.entity.ActBusiness;
import me.zhengjie.service.ActBusinessService;
import me.zhengjie.service.LogService;
import me.zhengjie.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import  me.zhengjie.dao.service.ActivityService;

import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@Api(tags =" 流程表单管理数据接口")
@RequestMapping("/api/activityDynamics")
public class ActivityController {

    @Autowired
    private LogService logService;
    @Autowired
    private SecurityUtil securityUtil;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ActBusinessService actBusinessService;

    /**
     * 功能描述：新增流程表单数据
     * @return 返回新增结果
     */
    @Log("新增流程表单数据")
    @ApiOperation("新增流程表单数据")
    @PostMapping("addActivity")
    public Result<Object> addActivity(@RequestBody Map<String,Object> map){

        Long time = System.currentTimeMillis();
        String path = "";
        if(map==null||!map.containsKey("tableName") ||!map.containsKey("model")||!map.containsKey("title")||!map.containsKey("procDefId")){
            return ResultUtil.error("保存异常:参数为空，请联系管理员" );
        }
        try {
            //获取要插入的数据
            LinkedHashMap<String,Object> linkedHashMap = (LinkedHashMap) map.get("model");
            if(linkedHashMap==null){
                return ResultUtil.error("保存异常:参数为空，请联系管理员" );
            }
            long id = IdWorker.getId();
            linkedHashMap.put("id",id);//统一添加id

            linkedHashMap.put("create_id",securityUtil.getCurrUser().getId().toString());//统一添加创建id
            linkedHashMap.put("create_time",new Date());//统一添加创建时间
            linkedHashMap.put("status",0);//统一添加状态
            //保存实体信息
            boolean res = activityService.AddActivity(map);
            if (res) {

                // 保存至我的申请业务
                String userId = securityUtil.getCurrUser().getId().toString();
                ActBusiness actBusiness = new ActBusiness();
                actBusiness.setProcessName(map.get("title").toString());
                actBusiness.setUserId(userId);
                actBusiness.setCreateBy(userId);
                actBusiness.setCreateTime(new Date());
                actBusiness.setTitle(map.get("title").toString());
                actBusiness.setTableId(Long.toString(id));
                actBusiness.setProcDefId(map.get("procDefId").toString());
                ActBusiness save = actBusinessService.save(actBusiness);

                return ResultUtil.data(res, "保存成功");
            } else {
                return ResultUtil.error("保存失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logService.addErrorLog("保存异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("保存异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：编辑流程表单数据
     * @return 返回新增结果
     */
    @Log("编辑流程表单数据")
    @ApiOperation("编辑流程表单数据")
    @PostMapping("updateActivity")
    public Result<Object> updateActivity(@RequestBody Map<String,Object> map){

        Long time = System.currentTimeMillis();
        String path = "";
        if(map==null||!map.containsKey("tableName") ||!map.containsKey("model")){
            return ResultUtil.error("保存异常:参数为空，请联系管理员" );
        }
        try {

            //获取要插入的数据
            LinkedHashMap<String,Object> linkedHashMap = (LinkedHashMap) map.get("model");
            if(linkedHashMap==null){
                return ResultUtil.error("保存异常:参数为空，请联系管理员" );
            }

            linkedHashMap.put("update_id",securityUtil.getCurrUser().getId().toString());//统一添加更新人id
            linkedHashMap.put("update_time",new Date());//统一添加更新时间

            //保存实体信息
            boolean res = activityService.UpdateActivity(map);
            if (res) {
                return ResultUtil.data(res, "保存成功");
            } else {
                return ResultUtil.error("保存失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logService.addErrorLog("保存异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("保存异常:" + e.getMessage());
        }
    }
    /**
     * 功能描述：编辑流程表单数据
     * @return 返回新增结果
     */
    @Log("编辑流程表单数据")
    @ApiOperation("编辑流程表单数据")
    @GetMapping("getActivityById")
    public Result<Object> getActivityById(@RequestParam(name = "id")String id,@RequestParam(name = "tableName")String tableName){

        Long time = System.currentTimeMillis();
        String path = "";
        if(StringUtils.isBlank(id)||StringUtils.isBlank(tableName)){
            return ResultUtil.error("查询失败:参数为空，请联系管理员" );
        }
        try {
           return ResultUtil.data(activityService.getActivityById(id,tableName), "查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }
}
