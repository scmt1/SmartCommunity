package me.zhengjie.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.AllArgsConstructor;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.BasicHousingManage;
import me.zhengjie.mapper.BasicHousingManageMapper;
import me.zhengjie.service.IBasicHousingManageService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

//import  me.zhengjie.aop.log.Log;
//import  me.zhengjie.service.LogService;

/**
 * housing数据接口
 *@author
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/api/basicHousingManage")
public class BasicHousingManageController{
    
    private final IBasicHousingManageService basicHousingManageService;
    
    private final BasicHousingManageMapper mapper;

    /**
     * 新增housing数据
     * @param basicHousingManage
     * @return
     */
    @PostMapping("addBasicHousingManage")
    public Result<Object> addBasicHousingManage(BasicHousingManage basicHousingManage){
        Long time = System.currentTimeMillis();
        try {
            basicHousingManage.setIsDelete(0);
            basicHousingManage.setCreateTime(new Timestamp(System.currentTimeMillis()));
            boolean res = basicHousingManageService.save(basicHousingManage);
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
     * 根据主键来删除housing数据
     * @param map
     * @return
     */
    @PostMapping("deleteBasicHousingManage")
    public Result<Object> deleteBasicHousingManage(@RequestBody Map<String ,Object> map) {
        if (map == null || map.size() == 0 ||!map.containsKey("ids")) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            List<String> ids = (List<String>) map.get("ids");
            if (ids == null || ids.size() == 0 ) {
                return ResultUtil.error("参数为空，请联系管理员！！");
            }
            boolean res = basicHousingManageService.removeByIds(ids);
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
     * 根据主键来获取housing数据
     * @param id
     * @return
     */
    @GetMapping("getBasicHousingManage")
    public Result<Object> getBasicHousingManage(@RequestParam(name = "id")String id){
        if (StringUtils.isBlank(id)) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            BasicHousingManage res = basicHousingManageService.getById(id);
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
     * 分页查询housing数据
     * @param basicHousingManage
     * @param searchVo
     * @param pageVo
     * @return
     */
    @GetMapping("queryBasicHousingManageList")
    public Result<Object> queryBasicHousingManageList(BasicHousingManage  basicHousingManage, SearchVo searchVo, PageVo pageVo){
        Long time = System.currentTimeMillis();
        try {
            return basicHousingManageService.queryBasicHousingManageListByPage(basicHousingManage, searchVo, pageVo);
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 不分页查询housing数据
     * @param basicHousingManage
     * @return
     */
    @GetMapping("getAllBasicHousingManage")
    public Result<Object> getAllBasicHousingManage(BasicHousingManage  basicHousingManage){
        QueryWrapper<BasicHousingManage> queryWrapper = new QueryWrapper<>();

        if(StringUtils.isNotBlank(basicHousingManage.getStreet())){
            queryWrapper.lambda().and(i -> i.like(BasicHousingManage::getStreet, basicHousingManage.getStreet()));
        }
        if(StringUtils.isNotBlank(basicHousingManage.getStreet())){
            queryWrapper.lambda().and(i -> i.like(BasicHousingManage::getCommunity, basicHousingManage.getStreet()));
        }
        if(StringUtils.isNotBlank(basicHousingManage.getStreet())){
            queryWrapper.lambda().and(i -> i.like(BasicHousingManage::getOwnedGrid, basicHousingManage.getStreet()));
        }
        queryWrapper.lambda().and(i -> i.eq(BasicHousingManage::getIsDelete, 0));
        List<BasicHousingManage> list = mapper.selectList(queryWrapper);
        return ResultUtil.data(list);
    }

    /**
     * 更新housing数据
     * @param basicHousingManage
     * @return
     */
    @PostMapping("updateBasicHousingManage")
    public Result<Object> updateBasicHousingManage(BasicHousingManage basicHousingManage){
        if (StringUtils.isBlank(basicHousingManage.getId())) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            basicHousingManage.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            boolean res = basicHousingManageService.updateById(basicHousingManage);
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
     * 导出housing数据
     * @param response
     * @param basicHousingManage
     */
    @PostMapping("/download")
    public void download(HttpServletResponse response, BasicHousingManage basicHousingManage){
        Long time = System.currentTimeMillis();
        try {
            basicHousingManageService.download( basicHousingManage,response);
        } catch (Exception e) {
//            logService.addErrorLog("导出异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
        }
    }

    /**
     * 模糊查询全部basicGrids数据
     * @param basicHousingManage
     * @return
     */
    @GetMapping("queryHousingManage")
    public Result<Object> queryHousingManage(BasicHousingManage basicHousingManage){
        Long time = System.currentTimeMillis();
        try {
            return basicHousingManageService.queryHousingManage(basicHousingManage);
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }
}
