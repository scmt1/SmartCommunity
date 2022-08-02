package me.zhengjie.util;

import me.zhengjie.entity.GridDept;
import me.zhengjie.mapper.GridDeptMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 字典数据
 */
public class StreetUtil {

    private GridDeptMapper deptMapper;

    /**
     * 街道
     */
    public List<GridDept> streetDepts;
    /**
     * 社区
     */
    public List<GridDept> gridDepts;

    /**
     * 部门
     */
    public List<GridDept> Depts;
    /**
     * 岗位
     */
    public List<GridDept> gridPost;

    /**
     * PostConstruct注解启动时加载方法
     */
    public StreetUtil(GridDeptMapper deptMapper) {
        this.deptMapper = deptMapper;

    }

    /**
     * 初始化
     */
    public void Init() {
        streetDepts = deptMapper.LoadAllStreetDept();
        gridDepts = deptMapper.loadAllCommunity();
        Depts = deptMapper.loadAllDept();
        gridPost = deptMapper.LoadAllPost();
    }

    /**
     * 匹配街道
     *
     * @param name 街道名
     * @return
     */
    public static String matchingStreet(String name, List<GridDept> streetDept) {
        String res = "";
        if (streetDept == null || streetDept.size() == 0) {
            return res;
        }
        List<GridDept> collect = streetDept.stream().filter(item -> item.getName() != null && name.contains(item.getName())).collect(Collectors.toList());
        if (collect != null && collect.size() > 0) {
            GridDept gridDictionary = collect.get(0);
            if (gridDictionary != null) {
                res = gridDictionary.getId().toString();
            }
        }
        return res;
    }

    /**
     * 匹配社区
     *
     * @param streetName    街道名
     * @param communityName 社区名
     * @param streetDept    社区集合
     * @return
     */
    public static String matchingCommunity(String streetName, String communityName, List<GridDept> streetDept) {
        String res = "";
        if (streetDept == null || streetDept.size() == 0) {
            return res;
        }
        List<GridDept> collect = streetDept.stream().filter(item -> item.getName() != null && item.getParentName() != null && communityName.contains(item.getName()) && streetName.contains(item.getParentName())).collect(Collectors.toList());
        if (collect != null && collect.size() > 0) {
            GridDept gridDictionary = collect.get(0);
            if (gridDictionary != null) {
                res = gridDictionary.getId().toString();
            }
        }
        return res;
    }

    /**
     * 匹配部门
     *
     * @param deptName      部门
     * @param communityName 社区
     * @param streetDept    部门集
     * @return
     */
    public static String matchingDept(String deptName, String communityName, List<GridDept> streetDept) {
        String res = "";
        if (streetDept == null || streetDept.size() == 0) {
            return res;
        }
        List<GridDept> collect = streetDept.stream().filter(item -> item.getParentName() != null && item.getName() != null && communityName.contains(item.getParentName()) && deptName.contains(item.getName())).collect(Collectors.toList());
        if (collect != null && collect.size() > 0) {
            GridDept gridDictionary = collect.get(0);
            if (gridDictionary != null) {
                res = gridDictionary.getId().toString();
            }
        }
        return res;
    }


    /**
     * 匹配岗位
     *
     * @return
     */
    public static String matchingPost(String name, List<GridDept> gridPost) {
        String res = "";
        if (gridPost == null || gridPost.size() == 0) {
            return res;
        }
        List<GridDept> collect = gridPost.stream().filter(item -> name.contains(item.getName())).collect(Collectors.toList());
        if (collect != null && collect.size() > 0) {
            GridDept gridDictionary = collect.get(0);
            if (gridDictionary != null) {
                res = gridDictionary.getId().toString();
            }
        }
        return res;
    }

}
