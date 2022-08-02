package me.zhengjie.dao.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import javax.servlet.http.HttpServletResponse;

import me.zhengjie.aop.log.Log;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.dao.entity.TCompany;
import me.zhengjie.dao.service.ITCompanyService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import me.zhengjie.service.LogService;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
/**
 *@author
 **/
@RestController
@Api(tags =" company数据接口")
@RequestMapping("/api/tCompany")
public class TCompanyController{
    @Autowired
    private ITCompanyService tCompanyService;
    @Autowired
    private LogService logService;

    /**
     * 功能描述：新增company数据
     * @param tCompany 实体
     * @return 返回新增结果
     */
    @Log("新增company数据")
    @ApiOperation("新增company数据")
    @PostMapping("addTCompany")
    public Result<Object> addTCompany(TCompany tCompany){
        Long time = System.currentTimeMillis();
        try {
            tCompany.setIsDelete(1);
            tCompany.setCreateTime(new Timestamp(System.currentTimeMillis()));
            //tCompany.setUpdateTime(System.currentTimeMillis());
            boolean res = tCompanyService.save(tCompany);
            if (res) {
                return ResultUtil.data(res, "保存成功");
            } else {
                return ResultUtil.error("保存失败");
            }
        } catch (Exception e) {
            logService.addErrorLog("保存异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("保存异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：根据主键来删除数据
     * @param ids 主键集合
     * @return 返回删除结果
     */
    @Log("根据主键来删除company数据")
    @ApiOperation("根据主键来删除company数据")
    @PostMapping("deleteTCompany")
    public Result<Object> deleteTCompany(@RequestParam(name = "ids[]")String[] ids){
        if (ids == null || ids.length == 0) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
//        Collection<TCompany> resultList= new ArrayList(Arrays.asList(ids));
        try {
//            boolean res = tCompanyService.updateBatchById(resultList);
            boolean res = tCompanyService.removeByIds(Arrays.asList(ids));
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
     * @param id 主键
     * @return 返回获取结果
     */
    @Log("根据主键来获取company数据")
    @ApiOperation("根据主键来获取company数据")
    @GetMapping("getTCompany")
    public Result<Object> getTCompany(@RequestParam(name = "id")String id){
        if (StringUtils.isBlank(id)) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            TCompany res = tCompanyService.getById(id);
            if (res != null) {
                return ResultUtil.data(res, "查询成功");
            } else {
                return ResultUtil.error("查询失败");
            }
        } catch (Exception e) {
            logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：实现分页查询
     * @param tCompany 需要模糊查询的信息
     * @return 返回获取结果
     */
    @Log("分页查询company数据")
    @ApiOperation("分页查询company数据")
    @GetMapping("queryTCompanyList")
    public Result<Object> queryTCompanyList(TCompany tCompany, SearchVo searchVo, PageVo pageVo){
        Long time = System.currentTimeMillis();
        try {
            return tCompanyService.queryTCompanyListByPage(tCompany, searchVo, pageVo);
        } catch (Exception e) {
            logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }
    /**
     * 功能描述：更新数据
     * @param tCompany 实体
     * @return 返回更新结果
     */
    @Log("更新company数据")
    @ApiOperation("更新company数据")
    @PostMapping("updateTCompany")
    public Result<Object> updateTCompany(TCompany tCompany){
        if (StringUtils.isBlank(tCompany.getId())) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            tCompany.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            boolean res = tCompanyService.updateById(tCompany);
            if (res) {
                return ResultUtil.data(res, "保存成功");
            } else {
                return ResultUtil.error("保存失败");
            }
        } catch (Exception e) {
            logService.addErrorLog("保存异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("保存异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：导出数据
     * @return
     */
    @Log("导出company数据")
    @ApiOperation("导出company数据")
    @PostMapping("download")
    public void download(HttpServletResponse response, TCompany tCompany){
        Long time = System.currentTimeMillis();
        try {
            tCompanyService.download(tCompany,response);
        } catch (Exception e) {
            logService.addErrorLog("导出异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
        }
    }
}
