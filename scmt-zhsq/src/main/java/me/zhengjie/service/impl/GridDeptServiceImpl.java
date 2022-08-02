package me.zhengjie.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.AllArgsConstructor;
import me.zhengjie.entity.BasicGrids;
import me.zhengjie.entity.GridDept;
import me.zhengjie.entity.GridDeptTree;
import me.zhengjie.mapper.GridDeptMapper;
import me.zhengjie.service.IGridDeptService;
import me.zhengjie.service.ITZhsqCommunityCadresService;
import me.zhengjie.system.domain.Dept;
import me.zhengjie.util.BusinessErrorException;
import me.zhengjie.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
@AllArgsConstructor
public class GridDeptServiceImpl extends ServiceImpl<GridDeptMapper, GridDept> implements IGridDeptService {

    private final GridDeptMapper deptMapper;

    private final ITZhsqCommunityCadresService tZhsqCommunityCadresService;

    @Override
    public List<JSONObject> loadDeptTree() {
        List<JSONObject> list = deptMapper.selectDeptTree();
        List<JSONObject> parentList = new ArrayList<>();
        list.forEach(dept -> {
            if (dept.getLongValue("parentId") == 0){
                parentList.add(dept);
            }
        });
        findSonDept(parentList,list);
        return parentList;
    }

    @Override
    public List<Map<String, Object>> loadGridDeptAndGridTree(Map<String,Object> powerMap) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        mapList = deptMapper.loadGridDeptAndGridTree(powerMap);
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        for(Map<String, Object> map:mapList ){
            if(map != null){
                if(map.containsKey("id") && map.containsKey("pid") && map.containsKey("title") && map.get("title")!=null){

                    String id = map.get("id").toString();
                    String pid = map.get("pid").toString();
                    String title = map.get("title")!=null?map.get("title").toString():"";
                    List<Map<String, Object>> children = new ArrayList<Map<String, Object>>();
                    for(Map<String, Object> mapChild:mapList ){
                        if(mapChild != null ){
                            if( mapChild.containsKey("pid") && mapChild.get("pid").toString().trim().equals(id)){
                                children.add(mapChild);
                                if(mapChild.containsKey("title") && mapChild.containsKey("lel") && "4".equals(mapChild.get("lel").toString().trim()) ){
                                    String mapChildTitle  = mapChild.get("title").toString();
                                    mapChild.put("title",title+mapChildTitle+"号楼");
                                }
                            }

                        }

                    }
                    map.put("children",children);

                    if("0".equals(pid)){
                        resultList.add(map);
                    }
                }
            }
        }
        return resultList;
    }

    @Override
    public IPage<GridDept> loadGridDeptTreeByPage(JSONObject query) {
        Page<GridDept> page = new Page<>(query.getLongValue("pageNum"),query.getLongValue("pageSize"));

        Page<GridDept> gridDeptPage = deptMapper.loadByPage(page,  query);
        if(gridDeptPage!=null && gridDeptPage.getRecords()!=null && gridDeptPage.getRecords().size()>0)
            gridDeptPage.getRecords().forEach(dept -> {
            if ( dept.getId()!=0){
                List<GridDept> gridDepts = deptMapper.selectDeptTreeByParentId(dept.getId());
                dept.setChildren(gridDepts);
                getChildren(gridDepts);
            }
        });
        return gridDeptPage;
    }

    @Override
    public List<GridDept> loadGridDeptTreeNotPage(JSONObject query) {
        List<GridDept> gridDeptNotPage = deptMapper.loadNotPage(query);
        if(gridDeptNotPage!=null){
            gridDeptNotPage.forEach(dept -> {
                if ( dept.getId()!=0){
                    List<GridDept> gridDepts = deptMapper.selectDeptTreeByParentId(dept.getId());
                    dept.setChildren(gridDepts);
                    getChildren(gridDepts);
                }
            });
        }
        return gridDeptNotPage;
    }

    public void getChildren( List<GridDept> gridDepts){
        if(gridDepts!=null && gridDepts.size()>0){
            gridDepts.forEach(dept -> {
                if ( dept.getId()!=0){
                    List<GridDept> gridDeptList = deptMapper.selectDeptTreeByParentId(dept.getId());
                    if(gridDeptList!=null && gridDeptList.size()>0){
                        dept.setChildren(gridDeptList);
                        getChildren(gridDeptList);
                    }
                }
            });
        }

    }

    private void findSonDept(List<JSONObject> parentList, List<JSONObject> list) {
        parentList.forEach(parent -> {
            List<JSONObject> childList = new ArrayList<>();
            list.forEach(son -> {
                if (son.getLongValue("parentId") == parent.getLongValue("id")){
                    childList.add(son);
                }
            });
            parent.put("child",childList);
            if (!childList.isEmpty()){
                findSonDept(childList,list);
            }
        });
    }

    @Override
    public IPage<GridDept> loadAllByQuery(JSONObject query) {
        Page<GridDept> page = new Page<>(query.getLongValue("pageNum"),query.getLongValue("pageSize"));
        QueryWrapper<GridDept> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(GridDept::getName,query.getString("queryStr"));
        return deptMapper.selectPage(page,queryWrapper);
    }

    @Override
    public boolean add(GridDept dept) {
        int insert = deptMapper.insert(dept);


        return insert>0;
    }

    @Override
    public boolean delete(Integer deptId) {
        int countDetails = tZhsqCommunityCadresService.getCountByDept(deptId);
        if (countDetails > 0) {
            throw new BusinessErrorException("部门下有社区干部档案存在，不能删除");
        }
        GridDept dept = deptMapper.selectById(deptId);
        int status = deptMapper.deleteById(deptId);

        return status == 1;
    }

    @Override
    public boolean modify(GridDept dept) {
        int i = deptMapper.updateById(dept);

        return i==1;
    }

    @Override
    public List<JSONObject> loadStreetDept() {
        return deptMapper.selectStreetDept();
    }

    @Override
    public List<JSONObject> loadCommunityDeptByStreet(Long streetId) {
        return deptMapper.loadCommunityDeptByStreet(streetId);
    }


    @Override
    public List<GridDept> loadByQuery(JSONObject query) {

        return  deptMapper.loadByQuery(query);
    }

    @Override
    public List<JSONObject> loadSelectByType(JSONObject params) {
        return deptMapper.getSelectByType(params);
    }

    @Override
    public List<JSONObject> getAll4Select() {
        return deptMapper.getAll4Select();
    }

    @Override
    public Map<String, Object> selectDeptTree() {
        List<GridDeptTree> gridDepts = deptMapper.selectDept();
        Set<GridDeptTree> trees = new LinkedHashSet<>();
        for (GridDeptTree gridDept : gridDepts){
            if ("0".equals(gridDept.getParentId())){
                trees.add(gridDept);
            }
            for (GridDeptTree dept : gridDepts){
                if (dept.getParentId().equals(gridDept.getId())){
                    if (gridDept.getChildren() == null){
                        gridDept.setChildren(new ArrayList<>());
                    }
                    gridDept.getChildren().add(dept);
                }
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("tree", trees);
        return map;
    }

    @Override
    public List<JSONObject> selectStreetByDeptId(Integer attribute, String deptId) {
        if (attribute == 1){//街道
            return deptMapper.selectDeptById(deptId);
        }else if (attribute == 2){//社区
            return deptMapper.selectDeptByCommunityId(deptId);
        }else if(attribute == 3) {//网格
            return deptMapper.selectDeptByGridId(deptId);
        }
        return deptMapper.selectStreetDept();
    }

    @Override
    public List<JSONObject> selectCommunityByDeptId(Integer attribute, String deptId, Long streetId) {
        if (attribute == 1){//街道
            return deptMapper.loadCommunityDeptByStreet(deptId);
        }else if (attribute == 2){//社区
            return deptMapper.selectCommunityById(deptId);
        }else if(attribute == 3){//网格
            return deptMapper.selectCommunityByGridId(deptId);
        }
        return deptMapper.loadCommunityDeptByStreet(streetId);
    }
}
