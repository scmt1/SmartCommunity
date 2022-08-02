package me.zhengjie.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.aop.log.Log;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.TZhsqAnnounceType;
import me.zhengjie.service.ITZhsqAnnounceTypeService;
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
@Api(tags = " 公共类型数据接口")
@RequestMapping("/api/tZhsqAnnounceType")
public class TZhsqAnnounceTypeController {
    @Autowired
    private ITZhsqAnnounceTypeService tZhsqAnnounceTypeService;
    @Autowired
    private LogService logService;

    /**
     * 功能描述：新增公共类型数据
     *
     * @param tZhsqAnnounceType 实体
     * @return 返回新增结果
     */
    @Log("新增公共类型数据")
    @ApiOperation("新增公共类型数据")
    @PostMapping("addTZhsqAnnounceType")
    public Result<Object> addTZhsqAnnounceType(@RequestBody TZhsqAnnounceType tZhsqAnnounceType) {
        Long time = System.currentTimeMillis();
        try {
            tZhsqAnnounceType.setIsDelete("0");
            tZhsqAnnounceType.setCreateTime(new Timestamp(System.currentTimeMillis()));
            boolean res = tZhsqAnnounceTypeService.save(tZhsqAnnounceType);
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
    @Log("根据主键来删除公共类型数据")
    @ApiOperation("根据主键来删除公共类型数据")
    @PostMapping("deleteTZhsqAnnounceType")
    public Result<Object> deleteTZhsqAnnounceType(@RequestParam(name = "ids[]") String[] ids) {
        if (ids == null || ids.length == 0) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            boolean res = tZhsqAnnounceTypeService.removeByIds(Arrays.asList(ids));
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
    @Log("根据主键来获取公共类型数据")
    @ApiOperation("根据主键来获取公共类型数据")
    @GetMapping("getTZhsqAnnounceType")
    public Result<Object> getTZhsqAnnounceType(@RequestParam(name = "id") String id) {
        if (StringUtils.isBlank(id)) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            TZhsqAnnounceType res = tZhsqAnnounceTypeService.getById(id);
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
     * @param tZhsqAnnounceType 需要模糊查询的信息
     * @param searchVo          查询参数
     * @param pageVo            分页参数
     * @return 返回获取结果
     */
    @Log("分页查询公共类型数据")
    @ApiOperation("分页查询公共类型数据")
    @GetMapping("queryTZhsqAnnounceTypeList")
    public Result<Object> queryTZhsqAnnounceTypeList(TZhsqAnnounceType tZhsqAnnounceType, SearchVo searchVo, PageVo pageVo) {
        Long time = System.currentTimeMillis();
        try {
            return tZhsqAnnounceTypeService.queryTZhsqAnnounceTypeListByPage(tZhsqAnnounceType, searchVo, pageVo);
        } catch (Exception e) {
            e.printStackTrace();
            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：实现查询所有公共类型数据
     *
     * @return 返回获取结果
     */
    @Log("查询所有公共类型数据")
    @ApiOperation("查询所有公共类型数据")
    @GetMapping("queryAllTZhsqAnnounceTypeList")
    public Result<Object> queryAllTZhsqAnnounceTypeList() {
        Long time = System.currentTimeMillis();
        try {
            QueryWrapper<TZhsqAnnounceType> queryWrapper = new QueryWrapper<>();
            queryWrapper.and(i -> i.like("t_zhsq_announce_type.announce_show", 1));
            queryWrapper.and(i -> i.like("t_zhsq_announce_type.is_delete", 0));
            queryWrapper.orderByDesc("t_zhsq_announce_type.announce_sort");
            return ResultUtil.data(tZhsqAnnounceTypeService.list(queryWrapper));
        } catch (Exception e) {
            e.printStackTrace();
            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }
    /**
     * 功能描述：更新数据
     *
     * @param tZhsqAnnounceType 实体
     * @return 返回更新结果
     */
    @Log("更新公共类型数据")
    @ApiOperation("更新公共类型数据")
    @PostMapping("updateTZhsqAnnounceType")
    public Result<Object> updateTZhsqAnnounceType(@RequestBody TZhsqAnnounceType tZhsqAnnounceType) {
        if (StringUtils.isBlank(tZhsqAnnounceType.getId().toString())) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            tZhsqAnnounceType.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            boolean res = tZhsqAnnounceTypeService.updateById(tZhsqAnnounceType);
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
     * @param response          请求参数
     * @param tZhsqAnnounceType 查询参数
     * @return
     */
    @Log("导出公共类型数据")
    @ApiOperation("导出公共类型数据")
    @PostMapping("/download")
    public void download(HttpServletResponse response, TZhsqAnnounceType tZhsqAnnounceType) {
        Long time = System.currentTimeMillis();
        try {
            tZhsqAnnounceTypeService.download(tZhsqAnnounceType, response);
        } catch (Exception e) {
            e.printStackTrace();
            logService.addErrorLog("导出异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
        }
    }

    /**
     * 依据id更改状态
     */

    @Log("依据id更改状态")
    @ApiOperation("依据id更改状态")
    @PostMapping("/updateStatusById")
    public Result<Object> updateStatusByid(@RequestBody String id) {
        return null;
    }
}