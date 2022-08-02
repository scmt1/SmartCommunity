package me.zhengjie.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.entity.GridDept;
import me.zhengjie.entity.GridDeptTree;
import me.zhengjie.entity.GridDictionary;
import me.zhengjie.system.domain.Dept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface GridDeptMapper extends BaseMapper<GridDept> {

    List<JSONObject> selectDeptTree();

    //分页查询街道
    Page<GridDept> loadByPage(@Param("page") Page page, @Param("query") JSONObject query);

    //查询全部街道
    List<GridDept> loadNotPage(@Param("query") JSONObject query);

    // 查询社区、部门
    List<GridDept> selectDeptTreeByParentId(@Param("parentId") Long parentId);


    @Select("select id,name from grid_dept where type = 11")
    List<JSONObject> selectStreetDept();

    List<JSONObject> loadCommunityDeptByStreet(@Param("streetId") Long streetId);

    List<JSONObject> loadCommunityDeptByStreet(@Param("streetId") String streetId);

    List<GridDept> loadByQuery(@Param("query")JSONObject query);

    List<JSONObject> getSelectByType(@Param("params") JSONObject params);


    @Select("select id,name from grid_post where status =1")
    List<JSONObject> getAll4Select();

    /**
     * 查询用户数据权限树
     * @return
     */
    List<GridDeptTree> selectDept();

    /**
     * 根据id查询所属街道
     * @param id
     * @return
     */
    List<JSONObject> selectDeptById(@Param("id")String id);

    /**
     * 根据所属社区id查询所属街道
     * @param id
     * @return
     */
    List<JSONObject> selectDeptByCommunityId(@Param("id")String id);

    /**
     * 根据所属网格id查询所属街道
     * @param id
     * @return
     */
    List<JSONObject> selectDeptByGridId(@Param("id")String id);

    /**
     * 根据id查询所属社区
     * @param id
     * @return
     */
    List<JSONObject> selectCommunityById(@Param("id")String id);

    /**
     * 根据网格id查询所属社区
     * @param id
     * @return
     */
    List<JSONObject> selectCommunityByGridId(@Param("id")String id);

    /**
     * 获取区县街道社区树级数据
     * @return
     */
    List<Map<String, Object>> loadGridDeptAndGridTree(@Param(value = "powerMap")Map<String,Object> powerMap);

    /**
     * 获取所有字典
     * @return
     */
    List<GridDictionary> loadAll();


    /**
     * 获取所有街道
     * @return
     */
    @Select("select id,name from grid_dept where type = 11")
    List<GridDept> LoadAllStreetDept();

    /**
     * 获取所有社区
     * @return
     */
    List<GridDept> loadAllCommunity();

    /**
     * 获取所有部门
     * @return
     */
    List<GridDept> loadAllDept();

    /**
     * 获取所有职位
     * @return
     */
    @Select("select id,name from grid_post where status =1")
    List<GridDept> LoadAllPost();
}
