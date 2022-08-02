package me.zhengjie.dao.controller;

import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import me.zhengjie.aop.log.Log;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.dao.entity.EpidemicReport;
import me.zhengjie.dao.service.ActivityService;
import me.zhengjie.dao.service.IEpidemicReportService;
import org.apache.fop.util.LogUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import me.zhengjie.service.LogService;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author
 **/
@RestController
@Api(tags = " 返泸人员申报信息数据接口")
@RequestMapping("/api/epidemicReport")
public class EpidemicReportController {
    @Autowired
    private IEpidemicReportService epidemicReportService;
    @Autowired
    private LogService logService;
    @Autowired
    private ActivityService activityService;

    /**
     * 功能描述：新增返泸人员申报信息数据
     *
     * @return 返回新增结果
     */
    @Log("新增返泸人员申报信息数据")
    @ApiOperation("新增返泸人员申报信息数据")
    @PostMapping("addEpidemicReport")
    public Result<Object> addEpidemicReport(@RequestBody Map<String, Object> map) {
        Long time = System.currentTimeMillis();
        try {
            //获取要插入的数据
            LinkedHashMap<String, Object> linkedHashMap = (LinkedHashMap) map.get("model");
            if (linkedHashMap == null) {
                return ResultUtil.error("保存异常:参数为空，请联系管理员");
            }
            Object id = linkedHashMap.get("id");
            boolean flag = false;
            if (id == null || StringUtils.isBlank(id.toString())) {
                linkedHashMap.put("id", UUID.randomUUID().toString().replaceAll("-", ""));//统一添加id
                flag = true;
            }
            linkedHashMap.put("create_time", new Date());//统一添加创建时间
            linkedHashMap.put("promise_date", new Date());//统一添加创建时间
            if (linkedHashMap.get("province_and_city") != null) {
                ArrayList province_and_city = (ArrayList) linkedHashMap.get("province_and_city");
                linkedHashMap.put("origin_province", province_and_city.get(0));
                linkedHashMap.put("origin_city", province_and_city.get(1));
                linkedHashMap.put("origin_area", province_and_city.get(2));
            }

            String arrive_date = linkedHashMap.get("arrive_date").toString();
            String leave_date = linkedHashMap.get("leave_date").toString();

            linkedHashMap.put("arrive_date", arrive_date);
            linkedHashMap.put("leave_date", leave_date);
            linkedHashMap.put("province_and_city", "");
            //保存实体信息
            boolean res = false;
            if (flag) {
                res = activityService.AddActivity(map);
            } else {
                res = activityService.UpdateActivity(map);
            }
            if (res) {
                return ResultUtil.data(res, "保存成功");
            } else {
                return ResultUtil.error("保存失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logService.addErrorLog("保存异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("保存异常:" + e.getMessage());
        }
    }


    public String timestamp2Date(String str_num, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        if (str_num.length() == 13) {
            String date = sdf.format(new Date(Long.parseLong(str_num)));
            return date;
        } else {
            String date = sdf.format(new Date(Integer.parseInt(str_num) * 1000L));
            return date;
        }
    }

    /**
     * 功能描述：根据主键来删除数据
     *
     * @param ids 主键集合
     * @return 返回删除结果
     */
    @Log("根据主键来删除返泸人员申报信息数据")
    @ApiOperation("根据主键来删除返泸人员申报信息数据")
    @PostMapping("deleteEpidemicReport")
    public Result<Object> deleteEpidemicReport(@RequestParam(name = "ids[]") String[] ids) {
        if (ids == null || ids.length == 0) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            boolean res = epidemicReportService.removeByIds(Arrays.asList(ids));
            if (res) {
                return ResultUtil.data(res, "删除成功");
            } else {
                return ResultUtil.error("删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logService.addErrorLog("删除异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("删除异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：根据主键来获取数据
     *
     * @return 返回获取结果
     */
    @Log("根据身份证来获取返泸人员申报信息数据")
    @ApiOperation("根据身份证来获取返泸人员申报信息数据")
    @GetMapping("getEpidemicReport")
    public Result<Object> getEpidemicReport(@RequestParam(name = "personIdCard") String personIdCard) {
        if (StringUtils.isBlank(personIdCard)) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            QueryWrapper<EpidemicReport> epidemicReportQueryWrapper = new QueryWrapper<>();
            epidemicReportQueryWrapper.eq("person_id_card", personIdCard);
            epidemicReportQueryWrapper.orderByDesc("create_time");
            EpidemicReport res = epidemicReportService.getOne(epidemicReportQueryWrapper);
            if (res != null) {
                return ResultUtil.data(res, "查询成功");
            } else {
                return ResultUtil.error("查询失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：实现分页查询
     *
     * @param epidemicReport 需要模糊查询的信息
     * @param searchVo       查询参数
     * @param pageVo         分页参数
     * @return 返回获取结果
     */
    @Log("分页查询返泸人员申报信息数据")
    @ApiOperation("分页查询返泸人员申报信息数据")
    @GetMapping("queryEpidemicReportList")
    public Result<Object> queryEpidemicReportList(EpidemicReport epidemicReport, SearchVo searchVo, PageVo pageVo) {
        Long time = System.currentTimeMillis();
        try {
            return epidemicReportService.queryEpidemicReportListByPage(epidemicReport, searchVo, pageVo);
        } catch (Exception e) {
            e.printStackTrace();
            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：更新数据
     *
     * @param epidemicReport 实体
     * @return 返回更新结果
     */
    @Log("更新返泸人员申报信息数据")
    @ApiOperation("更新返泸人员申报信息数据")
    @PostMapping("updateEpidemicReport")
    public Result<Object> updateEpidemicReport(@RequestBody EpidemicReport epidemicReport) {
        if (StringUtils.isBlank(epidemicReport.getId())) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            boolean res = epidemicReportService.updateById(epidemicReport);
            if (res) {
                return ResultUtil.data(res, "保存成功");
            } else {
                return ResultUtil.error("保存失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logService.addErrorLog("保存异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("保存异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：导出数据
     *
     * @param response       请求参数
     * @param epidemicReport 查询参数
     * @return
     */
    @Log("导出返泸人员申报信息数据")
    @ApiOperation("导出返泸人员申报信息数据")
    @PostMapping("/download")
    public void download(HttpServletResponse response, EpidemicReport epidemicReport) {
        Long time = System.currentTimeMillis();
        try {
            epidemicReportService.download(epidemicReport, response);
        } catch (Exception e) {
            e.printStackTrace();
            logService.addErrorLog("导出异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
        }
    }
}
