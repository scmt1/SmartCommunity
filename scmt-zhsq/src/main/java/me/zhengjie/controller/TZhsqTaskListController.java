package me.zhengjie.controller;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.service.ITZhsqTaskListService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.entity.TZhsqTaskList;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

/**
 * 任务列表数据接口
 *@author
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/api/tZhsqTaskList")
public class TZhsqTaskListController{

    private final ITZhsqTaskListService tZhsqTaskListService;

    /**
     * 新增任务列表数据
     * @param tZhsqTaskList
     * @return
     */
    @PostMapping("addTZhsqTaskList")
    public Result<Object> addTZhsqTaskList(TZhsqTaskList tZhsqTaskList){
        Long time = System.currentTimeMillis();
        try {
            tZhsqTaskList.setIsDelete(0);
            tZhsqTaskList.setCreateTime(new Timestamp(System.currentTimeMillis()));
            boolean res = tZhsqTaskListService.save(tZhsqTaskList);
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
    @PostMapping("deleteTZhsqTaskList")
    public Result<Object> deleteTZhsqTaskList(@RequestBody Map<String ,Object> map) {
        if (map == null || map.size() == 0 ||!map.containsKey("ids")) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            List<String> ids = (List<String>) map.get("ids");
            if (ids == null || ids.size() == 0 ) {
                return ResultUtil.error("参数为空，请联系管理员！！");
            }
            boolean res = tZhsqTaskListService.removeByIds(ids);
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
    @GetMapping("getTZhsqTaskList")
    public Result<Object> getTZhsqTaskList(@RequestParam(name = "id")String id){
        if (StringUtils.isBlank(id)) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            TZhsqTaskList res = tZhsqTaskListService.getById(id);
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
     * @param tZhsqTaskList
     * @param searchVo
     * @param pageVo
     * @return
     */
    @GetMapping("queryTZhsqTaskListList")
    public Result<Object> queryTZhsqTaskListList(TZhsqTaskList  tZhsqTaskList, SearchVo searchVo, PageVo pageVo){
        Long time = System.currentTimeMillis();
        try {
            return tZhsqTaskListService.queryTZhsqTaskListListByPage(tZhsqTaskList, searchVo, pageVo);
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 更新任务列表数据
     * @param tZhsqTaskList
     * @return
     */
    @PostMapping("updateTZhsqTaskList")
    public Result<Object> updateTZhsqTaskList(TZhsqTaskList tZhsqTaskList){
        if (StringUtils.isBlank(tZhsqTaskList.getId())) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            tZhsqTaskList.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            boolean res = tZhsqTaskListService.updateById(tZhsqTaskList);
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
     * @param tZhsqTaskList
     */
    @PostMapping("/download")
    public void download(HttpServletResponse response,TZhsqTaskList  tZhsqTaskList){
        Long time = System.currentTimeMillis();
        try {
            tZhsqTaskListService.download( tZhsqTaskList,response);
        } catch (Exception e) {
//            logService.addErrorLog("导出异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
        }
    }
}
