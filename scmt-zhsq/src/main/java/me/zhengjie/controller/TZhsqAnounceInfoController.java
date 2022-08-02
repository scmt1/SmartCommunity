package me.zhengjie.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.aop.log.Log;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.TZhsqAnounceInfo;
import me.zhengjie.service.ITZhsqAnounceInfoService;
import me.zhengjie.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Arrays;

/**
 * @author
 **/
@RestController
@Api(tags = " 公共信息数据接口")
@RequestMapping("/api/tZhsqAnounceInfo")
public class TZhsqAnounceInfoController {
    @Autowired
    private ITZhsqAnounceInfoService tZhsqAnounceInfoService;
    @Autowired
    private LogService logService;

    /**
     * 功能描述：新增公共信息数据
     *
     * @param tZhsqAnounceInfo 实体
     * @return 返回新增结果
     */
    @Log("新增公共信息数据")
    @ApiOperation("新增公共信息数据")
    @PostMapping("addTZhsqAnounceInfo")
    public Result<Object> addTZhsqAnounceInfo(@RequestBody TZhsqAnounceInfo tZhsqAnounceInfo) {
        Long time = System.currentTimeMillis();
        try {
            tZhsqAnounceInfo.setIsDelete("0");
            tZhsqAnounceInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
            boolean res = tZhsqAnounceInfoService.save(tZhsqAnounceInfo);
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
     * 功能描述：根据主键来删除数据
     *
     * @param ids 主键集合
     * @return 返回删除结果
     */
    @Log("根据主键来删除公共信息数据")
    @ApiOperation("根据主键来删除公共信息数据")
    @PostMapping("deleteTZhsqAnounceInfo")
    public Result<Object> deleteTZhsqAnounceInfo(@RequestParam(name = "ids[]") String[] ids) {
        if (ids == null || ids.length == 0) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            boolean res = tZhsqAnounceInfoService.removeByIds(Arrays.asList(ids));
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
     * @param id 主键
     * @return 返回获取结果
     */
    @Log("根据主键来获取公共信息数据")
    @ApiOperation("根据主键来获取公共信息数据")
    @GetMapping("getTZhsqAnounceInfo")
    public Result<Object> getTZhsqAnounceInfo(@RequestParam(name = "id") String id) {
        if (StringUtils.isBlank(id)) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            TZhsqAnounceInfo res = tZhsqAnounceInfoService.getById(id);
            if (res != null) {
                return ResultUtil.data(res, "查询成功");
            } else {
                return ResultUtil.error("查询失败:暂无数据");
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
     * @param tZhsqAnounceInfo 需要模糊查询的信息
     * @param searchVo         查询参数
     * @param pageVo           分页参数
     * @return 返回获取结果
     */
    @Log("分页查询公共信息数据")
    @ApiOperation("分页查询公共信息数据")
    @PostMapping("queryTZhsqAnounceInfoList")
    public Result<Object> queryTZhsqAnounceInfoList(TZhsqAnounceInfo tZhsqAnounceInfo, SearchVo searchVo, PageVo pageVo) {
        Long time = System.currentTimeMillis();
        try {
            return tZhsqAnounceInfoService.queryTZhsqAnounceInfoListByPage(tZhsqAnounceInfo, searchVo, pageVo);
        } catch (Exception e) {
            e.printStackTrace();
            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：更新数据
     *
     * @param tZhsqAnounceInfo 实体
     * @return 返回更新结果
     */
    @Log("更新公共信息数据")
    @ApiOperation("更新公共信息数据")
    @PostMapping("updateTZhsqAnounceInfo")
    public Result<Object> updateTZhsqAnounceInfo(@RequestBody TZhsqAnounceInfo tZhsqAnounceInfo) {
        if (StringUtils.isBlank(tZhsqAnounceInfo.getId().toString())) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            tZhsqAnounceInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
            boolean res = tZhsqAnounceInfoService.updateById(tZhsqAnounceInfo);
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
     * @param response         请求参数
     * @param tZhsqAnounceInfo 查询参数
     * @return
     */
    @Log("导出公共信息数据")
    @ApiOperation("导出公共信息数据")
    @PostMapping("/download")
    public void download(HttpServletResponse response, TZhsqAnounceInfo tZhsqAnounceInfo) {
        Long time = System.currentTimeMillis();
        try {
            tZhsqAnounceInfoService.download(tZhsqAnounceInfo, response);
        } catch (Exception e) {
            e.printStackTrace();
            logService.addErrorLog("导出异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
        }
    }
}