package me.zhengjie.controller;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.AllArgsConstructor;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.utils.SecurityUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.TZhsqBottomTool;
import me.zhengjie.service.ITZhsqBottomToolService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  图层控制接口
 * </p>
 *
 * @author dengjie
 * @since 2021-08-24
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/tZhsqBottomTool")
public class TZhsqBottomToolController {

    private final ITZhsqBottomToolService tZhsqBottomToolService;

    private final SecurityUtil securityUtil;
    /**
     * 新增数据
     * @param tZhsqBottomTool
     * @return
     */
    @PostMapping("addTZhsqBottomTool")
    public Result<Object> addTZhsqBottomTool(TZhsqBottomTool tZhsqBottomTool){
        Long time = System.currentTimeMillis();
        try {
            tZhsqBottomTool.setIsDelete(0);
            tZhsqBottomTool.setCreateTime(new Timestamp(System.currentTimeMillis()));
            boolean res = tZhsqBottomToolService.save(tZhsqBottomTool);
            if (res) {
                return ResultUtil.data(res, "保存成功");
            } else {
                return ResultUtil.error("保存失败");
            }
        } catch (Exception e) {
//            logService.addErrorLog("保存异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("保存异常:" + e.getMessage());
        }
    }

    /**
     * 根据主键来删除任务列表数据
     * @param map
     * @return
     */
    @PostMapping("deleteTZhsqBottomTool")
    public Result<Object> deleteTZhsqBottomTool(@RequestBody Map<String ,Object> map) {
        if (map == null || map.size() == 0 ||!map.containsKey("ids")) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            List<String> ids = (List<String>) map.get("ids");
            if (ids == null || ids.size() == 0 ) {
                return ResultUtil.error("参数为空，请联系管理员！！");
            }
            boolean res = tZhsqBottomToolService.removeByIds(ids);
            if (res) {
                return ResultUtil.data(res, "删除成功");
            } else {
                return ResultUtil.error( "删除失败");
            }
        } catch (Exception e) {
//            logService.addErrorLog("删除异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("删除异常:" + e.getMessage());
        }
    }

    /**
     * 根据主键来获取任务列表数据
     * @param id
     * @return
     */
    @GetMapping("getTZhsqBottomTool")
    public Result<Object> getTZhsqBottomTool(@RequestParam(name = "id")String id){
        if (StringUtils.isBlank(id)) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            TZhsqBottomTool res = tZhsqBottomToolService.getById(id);
            if (res != null) {
                return ResultUtil.data(res, "查询成功");
            } else {
                return ResultUtil.error("查询失败");
            }
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 分页查询任务列表数据
     * @param tZhsqBottomTool
     * @param searchVo
     * @param pageVo
     * @return
     */
    @GetMapping("queryTZhsqBottomToolList")
    public Result<Object> queryTZhsqBottomToolList(TZhsqBottomTool  tZhsqBottomTool, SearchVo searchVo, PageVo pageVo){
        Long time = System.currentTimeMillis();
        try {
            return tZhsqBottomToolService.queryTZhsqBottomToolByPage(tZhsqBottomTool, searchVo, pageVo);
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 更新任务列表数据
     * @param tZhsqBottomTool
     * @return
     */
    @PostMapping("updateTZhsqBottomTool")
    public Result<Object> updateTZhsqBottomTool(TZhsqBottomTool tZhsqBottomTool){
        if (StringUtils.isBlank(tZhsqBottomTool.getId())) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            tZhsqBottomTool.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            boolean res = tZhsqBottomToolService.updateById(tZhsqBottomTool);
            if (res) {
                return ResultUtil.data(res, "保存成功");
            } else {
                return ResultUtil.error("保存失败");
            }
        } catch (Exception e) {
//            logService.addErrorLog("保存异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("保存异常:" + e.getMessage());
        }
    }

    /**
     * 导出任务列表数据
     * @param response
     * @param tZhsqBottomTool
     */
    @PostMapping("/download")
    public void download(HttpServletResponse response, TZhsqBottomTool  tZhsqBottomTool){
        Long time = System.currentTimeMillis();
        try {
            tZhsqBottomToolService.download(tZhsqBottomTool,response);
        } catch (Exception e) {
//            logService.addErrorLog("导出异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
        }
    }

    /**
     * 功能描述：获取树数据
     * @return
     */
    @GetMapping("/treeData")
    public Result<Object> treeData(){
        Long time = System.currentTimeMillis();
        try {
            return ResultUtil.data(tZhsqBottomToolService.getTreeData(), "查询成功");
        } catch (Exception e) {
            e.printStackTrace();
           // logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }
}
