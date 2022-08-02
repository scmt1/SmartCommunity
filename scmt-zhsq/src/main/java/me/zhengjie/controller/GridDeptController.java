package me.zhengjie.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.baomidou.mybatisplus.extension.api.R;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.utils.SecurityUtil;
import me.zhengjie.common.vo.Result;
import me.zhengjie.entity.GridDept;
import me.zhengjie.service.IGridDeptService;
import me.zhengjie.system.service.UserService;
import me.zhengjie.system.service.dto.UserDto;
import me.zhengjie.utils.SecurityUtils;
import me.zhengjie.utils.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/gridDept")
@AllArgsConstructor
public class GridDeptController {
    
    private final IGridDeptService deptService;

    private final SecurityUtil securityUtil;

    /**
     * 添加部门信息
     * @param dept
     * @return
     */
    @PostMapping("/add")
    public Result<Object> addGridDept(@RequestBody GridDept dept){
        boolean result = deptService.add(dept);
        return ResultUtil.data(result);
    }

    /**
     * 删除部门信息
     * @param deptId
     * @return
     */
    @GetMapping("/delete")
    public Result<Object> deleteGridDept(Integer deptId){
        boolean result = deptService.delete(deptId);
        return ResultUtil.data(result);
    }

    /**
     * 修改部门信息
     * @param dept
     * @return
     */
    @PostMapping("/modify")
    public Result<Object> modifyGridDept(@RequestBody GridDept dept){
        boolean result = deptService.modify(dept);
        return ResultUtil.data(result);
    }

    /**
     * 获取单个部门
     * @param deptId
     * @return
     */
    @GetMapping("/loadOne")
    public Result<Object> loadOne(Integer deptId){
        GridDept dept = deptService.getById(deptId);
        return ResultUtil.data(dept);
    }

    /**
     * 获取街道部门
     * @param flag 是否限制权限 true限制权限 false查询所有
     * @return
     */
    @GetMapping("/loadStreetGridDept")
    public Result<Object> loadStreetGridDept(@RequestParam(name = "flag",required = false,defaultValue = "true") Boolean flag){
        List<JSONObject> dept = null;
        if (flag != null){//flag != null 判断用户数据权限
            if (flag){//flag = true 限制权限
                //用户数据权限
                UserDto user = securityUtil.getCurrUser();
                if (user.getPower() != null){
                    if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
                        Integer attribute = user.getPower().getAttribute();
                        String deptId = user.getPower().getDeptId();
                        dept = deptService.selectStreetByDeptId(attribute, deptId);
                    }
                }else {
                    dept = deptService.loadStreetDept();
                }
            }else{//flag = false 查询所有
                dept = deptService.loadStreetDept();
            }
        }else {//flag = null 查询所有
            dept = deptService.loadStreetDept();
        }
        return ResultUtil.data(dept);
    }

    /**
     * 获取社区部门
     * @param streetId 街道id
     * @param flag 是否限制权限 true限制权限 false查询所有
     * @return
     */
    @GetMapping("/loadCommunityGridDeptByStreet")
    public Result<Object> loadCommunityGridDeptByStreet(Long streetId,
                                                        @RequestParam(name = "flag",required = false,defaultValue = "true") Boolean flag){
        List<JSONObject> dept = null;
        if (flag != null){
            if (flag){// flag = true 限制权限
                //用户数据权限
                UserDto user = securityUtil.getCurrUser();
                if (user.getPower() != null){
                    if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
                        Integer attribute = user.getPower().getAttribute();
                        String deptId = user.getPower().getDeptId();
                        dept = deptService.selectCommunityByDeptId(attribute, deptId, streetId);
                    }
                }else{
                    dept = deptService.loadCommunityDeptByStreet(streetId);
                }
            }else{// flag = false 查询所有
                dept = deptService.loadCommunityDeptByStreet(streetId);
            }
        } else {//flag = null 查询所有
            dept = deptService.loadCommunityDeptByStreet(streetId);
        }
        return ResultUtil.data(dept);
    }

    /**
     * 获取社区下普通部门
     * @return
     */
    @PostMapping("/loadSelectByType")
    public Result<Object> loadSelectByType(@RequestBody JSONObject params){
        List<JSONObject> dept = deptService.loadSelectByType(params);
        return ResultUtil.data(dept);
    }

    /**
     * 获取所有部门（树级）
     * @return
     */
    @GetMapping("/loadGridDeptTree")
    public Result<Object> loadGridDeptTree(){
        List<JSONObject> deptList = deptService.loadDeptTree();
        return ResultUtil.data(deptList);
    }
    /**
     * 分页获取街道社区（树级）
     * @return
     */
    @PostMapping("/loadGridDeptTreeByPage")
    public Result<Object> loadGridDeptTreeByPage(@RequestBody JSONObject params){
        IPage<GridDept> gridDeptIPage = deptService.loadGridDeptTreeByPage(params);
        return ResultUtil.data(gridDeptIPage);
    }
    /**
     * 获取街道社区 不分页（树级）
     * @return
     */
    @PostMapping("/loadGridDeptTreeNotPage")
    public Result<Object> loadGridDeptTreeNotPage(@RequestBody JSONObject params){
        List<GridDept> gridDeptIPage = deptService.loadGridDeptTreeNotPage(params);
        return ResultUtil.data(gridDeptIPage);
    }
    /**
     * 获取街道社区网格（树级）
     * @return
     */
    @GetMapping("/loadGridDeptAndGridTree")
    public Result<Object> loadGridDeptAndGridTree(){
        try{
            //用户数据权限
            UserDto user = securityUtil.getCurrUser();
            Map<String,Object> powerMap = null;
            if (user.getPower() != null){
                if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
                    powerMap = new HashMap<>();
                    String deptId = user.getPower().getDeptId();
                    Integer attribute = user.getPower().getAttribute();
                    if (attribute == 1){// 街道
                        powerMap.put("streetId",deptId);
                    }else if (attribute == 2) {// 社区
                        powerMap.put("communityId",deptId);
                    }else if (attribute == 3) {// 网格
                        powerMap.put("gridId",deptId);
                    }
                }
            }
            List<Map<String,Object>>  gridDeptIPage = deptService.loadGridDeptAndGridTree(powerMap);
            return ResultUtil.data(gridDeptIPage);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.error(e.getMessage());
        }

    }


    /**
     * 获取所有部门
     * @param query
     * @return
     */
    @PostMapping("/loadAllByQuery")
    public Result<Object> loadAllByQuery(@RequestBody JSONObject query){
        IPage<GridDept> deptList = deptService.loadAllByQuery(query);
        return ResultUtil.data(deptList);
    }

    /**
     * 获取所有部门
     * @param query
     * @return
     */
    @PostMapping("/loadByQuery")
    public Result<Object> loadByQuery(@RequestBody JSONObject query){
        List<GridDept> deptList = deptService.loadByQuery(query);
        return ResultUtil.data(deptList);
    }

    /**
     * 获取所有职务
     * @return
     */
    @GetMapping("/post/loadAll4Select")
    public Result<Object> loadAll4Select(){
        List<JSONObject> all4Select = deptService.getAll4Select();
        return ResultUtil.data(all4Select);
    }
}
