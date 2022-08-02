package me.zhengjie.util;

import me.zhengjie.entity.BasicGrids;
import me.zhengjie.entity.GridDept;
import me.zhengjie.mapper.BasicGridsMapper;
import me.zhengjie.mapper.GridDeptMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 字典数据
 */
public class GridUtil {

    private BasicGridsMapper basicGridsMapper;

    /**
     * 街道
     */
    public  List<BasicGrids> basicGrids;

    /**
     * PostConstruct注解启动时加载方法
     */
    public GridUtil(BasicGridsMapper deptMapper){
        this.basicGridsMapper = deptMapper;
    }

    /**
     * 初始化
     */
    public void init(){
        basicGrids = basicGridsMapper.selectList(null);
    }

    /**
     * 匹配网格
     * @param name 街道名
     * @return
     */
    public static String matchingGrid(String name ,String communityName,String StreetName,List<BasicGrids> streetDept){
        String res = "";
        if(streetDept==null || streetDept.size()==0){
            return  res;
        }
        List<BasicGrids> collect =streetDept.stream().filter(item -> name.contains(item.getName())&& communityName.contains(item.getCommunityName()) &&StreetName.contains(item.getStreetName())  ).collect(Collectors.toList());
        if(collect!=null && collect.size()>0){
            BasicGrids gridDictionary = collect.get(0);
            if(gridDictionary!=null){
                res=gridDictionary.getId().toString();
            }
        }
        return  res;
    }

}
