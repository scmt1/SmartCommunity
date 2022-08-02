package me.zhengjie.dao.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.aop.log.Log;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.utils.SecurityUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.dao.entity.SysSetting;
import me.zhengjie.dao.service.ISysSettingService;
import me.zhengjie.service.LogService;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dengjie
 * @since 2020-06-10
 */
@RestController
@RequestMapping("/api/sysSetting")
public class SysSettingController {

    @Autowired
    private ISysSettingService iSysSettingService;

    @Autowired
    private LogService logService;

    @Autowired
    private SecurityUtil securityUtil;

    /**
     * 功能描述：新增Setting数据
     * @param sysSetting 实体
     * @return 返回新增结果
     */
    @Log("新增Setting数据")
    @ApiOperation("新增Setting数据")
    @PostMapping("addSysSetting")
    public Result<Object> addSysSetting(SysSetting sysSetting){
        Long time = System.currentTimeMillis();
        sysSetting.setCreateId(securityUtil.getCurrUser().getId().toString());
        sysSetting.setCreateTime(new Date());
        try {
            boolean res = iSysSettingService.save(sysSetting);
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
    @Log("根据主键来删除Setting数据")
    @ApiOperation("根据主键来删除Setting数据")
    @PostMapping("deleteSysSetting")
    public Result<Object> deleteSysSetting(@RequestParam("ids[]")String[] ids){
        if (ids == null || ids.length == 0) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            boolean res = iSysSettingService.removeByIds(Arrays.asList(ids));
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
    @Log("根据主键来获取Setting数据")
    @ApiOperation("根据主键来获取Setting数据")
    @GetMapping("getSysSetting")
    public Result<Object> getSysSetting(@RequestParam(name = "id")String id){
        if (StringUtils.isBlank(id)) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            SysSetting res = iSysSettingService.getById(id);
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
     * 功能描述：更新数据
     * @param sysSetting 实体
     * @return 返回更新结果
     */
    @Log("更新Setting数据")
    @ApiOperation("更新Setting数据")
    @PostMapping("updateSysSetting")
    public Result<Object> updateSysSetting(SysSetting sysSetting){
        if (StringUtils.isBlank(sysSetting.getId())) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            boolean res = iSysSettingService.updateById(sysSetting);
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
     * 功能描述：实现分页查询
     * @param search 需要模糊查询的信息
     * @return 返回获取结果
     */
    @Log("分页查询Setting数据")
    @ApiOperation("分页查询Setting数据")
    @GetMapping("querySysSettingList")
    public Result<Object> querySysSettingList(@RequestParam(name = "search", required = false)String search, SearchVo searchVo, PageVo pageVo){
        Long time = System.currentTimeMillis();
        try {
            IPage<SysSetting> sysSettingIPage = iSysSettingService.querySysSettingListByPage(search, searchVo, pageVo);
            return ResultUtil.data(sysSettingIPage);
        } catch (Exception e) {
            logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }
}
