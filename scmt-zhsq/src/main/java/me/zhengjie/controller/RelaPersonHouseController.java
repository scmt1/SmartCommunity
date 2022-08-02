package me.zhengjie.controller;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.entity.BasicHousing;
import me.zhengjie.entity.RelaPersonHouse;
import me.zhengjie.service.IRelaPersonHouseService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.SearchVo;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

/**
 * 数据接口
 *@author
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/api/relaPersonHouse")
public class RelaPersonHouseController{

    private final IRelaPersonHouseService relaPersonHouseService;

    /**
     * 新增数据
     * @param relaPersonHouse
     * @return
     */
    @PostMapping("addRelaPersonHouse")
    public Result<Object> addRelaPersonHouse(RelaPersonHouse relaPersonHouse){
        Long time = System.currentTimeMillis();
        try {
            relaPersonHouse.setCreateTime(new Timestamp(System.currentTimeMillis()));
            boolean res = relaPersonHouseService.save(relaPersonHouse);
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
     * 根据主键来删除数据
     * @param map
     * @return
     */
    @PostMapping("deleteRelaPersonHouse")
    public Result<Object> deleteRelaPersonHouse(@RequestBody Map<String ,Object> map) {
        if (map == null || map.size() == 0 ||!map.containsKey("ids")) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            List<String> ids = (List<String>) map.get("ids");
            if (ids == null || ids.size() == 0 ) {
                return ResultUtil.error("参数为空，请联系管理员！！");
            }
            boolean res = relaPersonHouseService.removeByIds(ids);
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
     * 根据主键来获取数据
     * @param id
     * @return
     */
    @GetMapping("getRelaPersonHouse")
    public Result<Object> getRelaPersonHouse(@RequestParam(name = "id")String id){
        if (StringUtils.isBlank(id)) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            RelaPersonHouse res = relaPersonHouseService.getById(id);
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
     * 根据主键来获取数据
     * @param pid
     * @return
     */
    @GetMapping("getRelaPersonHouseByPid")
    public Result<Object> getRelaPersonHouseByPid(@RequestParam(name = "pid")String pid){
        if (StringUtils.isBlank(pid)) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            List<BasicHousing> res = relaPersonHouseService.getRelaPersonHouseByPid(pid);
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
     * 分页查询数据
     * @param relaPersonHouse
     * @param searchVo
     * @param pageVo
     * @return
     */
    @GetMapping("queryRelaPersonHouseList")
    public Result<Object> queryRelaPersonHouseList(RelaPersonHouse  relaPersonHouse, SearchVo searchVo, PageVo pageVo){
        Long time = System.currentTimeMillis();
        try {
            return relaPersonHouseService.queryRelaPersonHouseListByPage(relaPersonHouse, searchVo, pageVo);
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 更新数据
     * @param relaPersonHouse
     * @return
     */
    @PostMapping("updateRelaPersonHouse")
    public Result<Object> updateRelaPersonHouse(RelaPersonHouse relaPersonHouse){
        if (StringUtils.isBlank(relaPersonHouse.getId())) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            relaPersonHouse.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            boolean res = relaPersonHouseService.updateById(relaPersonHouse);
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
     * 导出数据
     * @param response
     * @param relaPersonHouse
     */
    @PostMapping("/download")
    public void download(HttpServletResponse response,RelaPersonHouse  relaPersonHouse){
        Long time = System.currentTimeMillis();
        try {
            relaPersonHouseService.download( relaPersonHouse,response);
        } catch (Exception e) {
//            logService.addErrorLog("导出异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
        }
    }
}
