package me.zhengjie.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.baomidou.mybatisplus.extension.api.R;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.Result;
import me.zhengjie.entity.DeptType;
import me.zhengjie.service.IDeptTypeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/deptType")
public class DeptTypeController {

    private final IDeptTypeService deptTypeService;

    /**
     * 添加部门类型信息
     * @param deptType
     * @return
     */
    @PostMapping("/add")
    public Result<Object> addDeptType(@RequestBody DeptType deptType){
        boolean result = deptTypeService.add(deptType);
        return ResultUtil.data(result);
    }

    /**
     * 删除部门类型信息
     * @param deptTypeId
     * @return
     */
    @GetMapping("/delete")
    public Result<Object> deleteDeptType(Integer deptTypeId){
        boolean result = deptTypeService.delete(deptTypeId);
        return ResultUtil.data(result);
    }

    /**
     * 修改部门类型信息
     * @param deptType
     * @return
     */
    @PostMapping("/modify")
    public Result<Object> modifyDeptType(@RequestBody DeptType deptType){
        boolean result = deptTypeService.modify(deptType);
        return ResultUtil.data(result);
    }

    /**
     * 获取单个部门类型
     * @param deptTypeId
     * @return
     */
    @GetMapping("/loadOne")
    public Result<Object> loadOne(Integer deptTypeId){
        DeptType deptType = deptTypeService.getById(deptTypeId);
        return ResultUtil.data(deptType);
    }

    /**
     * 获取所有下拉部门类型
     * @return
     */
    @GetMapping("/loadAll4Select")
    public Result<Object> loadAll4Select(){
        List<JSONObject> deptTypeList = deptTypeService.loadAll4Select();
        return ResultUtil.data(deptTypeList);
    }

    /**
     * 获取所有部门类型
     * @param query
     * @return
     */
    @PostMapping("/loadAllByQuery")
    public Result<Object> loadAllByQuery(@RequestBody JSONObject query){
        IPage<DeptType> deptTypeList = deptTypeService.loadAllByQuery(query);
        return ResultUtil.data(deptTypeList);
    }
}
