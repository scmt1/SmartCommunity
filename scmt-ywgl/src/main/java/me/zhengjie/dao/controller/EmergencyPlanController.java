package me.zhengjie.dao.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.common.utils.SecurityUtil;
import me.zhengjie.system.service.DictDetailService;
import me.zhengjie.system.service.dto.DictDetailDto;
import me.zhengjie.system.service.dto.DictDetailQueryCriteria;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.aop.log.Log;
import me.zhengjie.dao.entity.EmergencyPlan;
import me.zhengjie.dao.service.IEmergencyPlanService;

import me.zhengjie.service.LogService;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Date;

/**
 * @author
 **/
@RestController
@Api(tags = " 应急预案数据接口")
@RequestMapping("/api/emergencyPlan")
public class EmergencyPlanController {
    @Autowired
    private IEmergencyPlanService emergencyPlanService;
    @Autowired
    private LogService logService;
    @Autowired
    private DictDetailService dictDetailService;
    @Autowired
    private SecurityUtil securityUtil;

    /**
     * 功能描述：新增应急预案数据
     *
     * @param emergencyPlan 实体
     * @return 返回新增结果
     */
    @Log("新增应急预案数据")
    @ApiOperation("新增应急预案数据")
    @AnonymousAccess
    @PostMapping("addEmergencyPlan")
    public Result<Object> addEmergencyPlan(@Validated @RequestBody EmergencyPlan emergencyPlan) {
        Long time = System.currentTimeMillis();
        emergencyPlan.setCreateId(securityUtil.getCurrUser().getId().toString());
        emergencyPlan.setDelFlag(1);
        Timestamp createTime = new Timestamp(System.currentTimeMillis());
        emergencyPlan.setCreateTime(createTime);
        try {
            boolean res = emergencyPlanService.save(emergencyPlan);
            if (res) {
                return ResultUtil.data(res, "保存成功");
            } else {
                return ResultUtil.error("保存失败");
            }
        } catch (Exception e) {
            logService.addErrorLog("保存异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("保存异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：根据主键来删除数据
     *
     * @param ids 主键集合
     * @return 返回删除结果
     */
    @Log("根据主键来删除应急预案数据")
    @ApiOperation("根据主键来删除应急预案数据")
    @PostMapping("deleteEmergencyPlan")
    public Result<Object> deleteEmergencyPlan(@RequestBody String[] ids) {
        if (ids == null || ids.length == 0) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            boolean res = emergencyPlanService.removeByIds(Arrays.asList(ids));
            if (res) {
                return ResultUtil.data(res, "删除成功");
            } else {
                return ResultUtil.error( "删除失败");
            }
        } catch (Exception e) {
            logService.addErrorLog("删除异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("删除异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：根据主键来获取数据
     *
     * @param id 主键
     * @return 返回获取结果
     */
    @Log("根据主键来获取应急预案数据")
    @ApiOperation("根据主键来获取应急预案数据")
    @GetMapping("getEmergencyPlan")
    public Result<Object> getEmergencyPlan(@RequestParam(name = "id") String id) {
        if (StringUtils.isBlank(id)) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            EmergencyPlan res = emergencyPlanService.getById(id);
            if (res != null) {
                return ResultUtil.data(res, "查询成功");
            } else {
                return ResultUtil.error("查询失败");
            }
        } catch (Exception e) {
            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：实现分页查询
     *
     * @param search 需要模糊查询的信息
     * @return 返回获取结果
     */
    @Log("分页查询应急预案数据")
    @ApiOperation("分页查询应急预案数据")
    @GetMapping("queryEmergencyPlanList")
    public Result<Object> queryEmergencyPlanList(@RequestParam(name = "search", required = false) String search,@RequestParam(name = "emergType", required = false) String emergType, SearchVo searchVo, PageVo pageVo) {
        Long time = System.currentTimeMillis();
        try {
            if(StringUtils.isNotBlank(emergType)){
                search = emergType;
            }
            return emergencyPlanService.queryEmergencyPlanListByPage(search, searchVo, pageVo);
        } catch (Exception e) {
            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：实现查询全部
     *
     * @param name 实现查询全部
     * @return 实现查询全部
     */
    @Log("实现查询全部")
    @ApiOperation("实现查询全部")
    @GetMapping("queryAllEmergencyPlanList")
    @AnonymousAccess
    public Result<Object> queryAllEmergencyPlanList(@RequestParam(name = "name", required = false) String name) {
        Long time = System.currentTimeMillis();
        try {
            QueryWrapper<EmergencyPlan> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().or(i -> i.like(EmergencyPlan::getEmergName, name));
            queryWrapper.lambda().and(i -> i.eq(EmergencyPlan::getDelFlag, 1));
            List<EmergencyPlan> list = emergencyPlanService.list(queryWrapper);
            return new ResultUtil<>().setData(list);
        } catch (Exception e) {
            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }


    /**
     * 功能描述：更新数据
     *
     * @param emergencyPlan 实体
     * @return 返回更新结果
     */
    @Log("更新应急预案数据")
    @ApiOperation("更新应急预案数据")
    @AnonymousAccess
    @PostMapping("updateEmergencyPlan")
    public Result<Object> updateEmergencyPlan(@Validated @RequestBody EmergencyPlan emergencyPlan) {
        if (StringUtils.isBlank(emergencyPlan.getId())) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        emergencyPlan.setUpdateId(securityUtil.getCurrUser().getId().toString());
        Timestamp createTime = new Timestamp(System.currentTimeMillis());
        emergencyPlan.setUpdateTime(createTime);
        try {
            boolean res = emergencyPlanService.updateById(emergencyPlan);
            if (res) {
                return ResultUtil.data(res, "保存成功");
            } else {
                return ResultUtil.error("保存失败");
            }
        } catch (Exception e) {
            logService.addErrorLog("保存异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("保存异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：导出数据
     *
     * @return
     */
    @Log("导出应急预案数据")
    @ApiOperation("导出应急预案数据")
    @PostMapping("/download")
    public void download(HttpServletResponse response, String likeValue) {
        Long time = System.currentTimeMillis();
        try {
            emergencyPlanService.download(likeValue, response);
        } catch (Exception e) {
            logService.addErrorLog("导出异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
        }
    }

    @Log("获取数据字典")
    @ApiOperation("获取数据字典")
    @PostMapping("/queryDict")
    public Result<Object> queryDict(@RequestParam(name = "search", required = false) String search) {
        DictDetailQueryCriteria detailQueryCriteria = new DictDetailQueryCriteria();
        detailQueryCriteria.setDictName(search);
        List<DictDetailDto> detailDtoList = dictDetailService.queryAll(detailQueryCriteria);

        Map<String, List<DictDetailDto>> hashMap = new HashMap<String, List<DictDetailDto>>();

        hashMap.put("detailDtoList", detailDtoList);

        return ResultUtil.data(hashMap, "成功");
    }
}
