package me.zhengjie.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.entity.DeptType;

import java.util.List;

public interface IDeptTypeService extends IService<DeptType> {


    /**
     * 新增部门类型
     * @return
     */
    boolean add(DeptType deptType);

    /**
     * 分页查询
     * @param query
     * @return
     */
    IPage<DeptType> loadAllByQuery(JSONObject query);

    /**
     * 获取所有类型
     * @return
     */
    List<JSONObject> loadAll4Select();

    /**
     * 删除部门类型
     * @return
     */
    boolean delete(Integer deptTypeId);

    /**
     *修改部门类型
     * @return
     */
    boolean modify(DeptType deptType);
}
