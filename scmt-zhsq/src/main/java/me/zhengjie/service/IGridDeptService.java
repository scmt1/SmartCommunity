package me.zhengjie.service;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.entity.GridDept;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IGridDeptService extends IService<GridDept> {

    /**
     * 获取树级数据
     * @return
     */
    List<JSONObject> loadDeptTree();

    /**
     * 获取街道社区网格树级数据
     * @return
     */
    List<Map<String, Object>> loadGridDeptAndGridTree(Map<String,Object> powerMap);


    /**
     * 分页查询树级数据
     * @return
     */
    IPage<GridDept> loadGridDeptTreeByPage(JSONObject query);

    /**
     * 查询全部树级数据
     * @return
     */
    List<GridDept> loadGridDeptTreeNotPage(JSONObject query);


    /**
     * 获取查询数据
     * @param query
     * @return
     */
    IPage<GridDept> loadAllByQuery(JSONObject query);

    /**
     *  新增部门
     * @param dept
     * @return
     */
    boolean add(GridDept dept);

    /**
     *  删除部门
     * @param deptId
     * @return
     */
    boolean delete(Integer deptId);

    /**
     *  修改部门
     * @param dept
     * @return
     */
    boolean modify(GridDept dept);

    /**
     * 获取街道部门
     * @return
     */
    List<JSONObject> loadStreetDept();

    /**
     * 根据街道id获取社区部门
     * @param streetId
     * @return
     */
    List<JSONObject> loadCommunityDeptByStreet(Long streetId);

    /**
     * 获取社区部门
     * @param query
     * @return
     */
    List<GridDept> loadByQuery(JSONObject query);

    /**
     * 根据社区id获取普通部门
     * @param params
     * @return
     */
    List<JSONObject> loadSelectByType(JSONObject params);

    /**
     * 查询所有职务
     * @return
     */
    List<JSONObject> getAll4Select();

    /**
     *用户数据权限树
     * @return
     */
    Map<String, Object> selectDeptTree();

    /**
     * 根据用户数据权限查询所属街道
     * @param attribute
     * @param deptId
     * @return
     */
    List<JSONObject> selectStreetByDeptId(Integer attribute,String deptId);

    /**
     * 根据用户数据权限查询所属社区
     * @param attribute
     * @param deptId
     * @param streetId
     * @return
     */
    List<JSONObject> selectCommunityByDeptId(Integer attribute,String deptId,Long streetId);
}
