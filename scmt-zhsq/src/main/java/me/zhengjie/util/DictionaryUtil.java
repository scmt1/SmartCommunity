package me.zhengjie.util;

import me.zhengjie.utils.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DictionaryUtil {
    /**
     * 住房状态
     */
    public static String rentStatus = "rentStatus";
    /**
     * 住房类型
     */
    public static String houseType = "houseType";
    /**
     * 建筑类型
     */
    public static String buildingType = "BuildingType";
    /**
     *  物业类型
     */
    public static String communityPriorityData = "communityPriorityData";
    /**
     * 商户类型
     */
    public static String merchantSortData = "merchantSortData";
    /**
     * 民族
     */
    public static String nationData = "nationData";
    /**
     * 特殊人群
     */
    public static String specialPersonData = "specialPersonData";
    /**
     * 政治面貌
     */
    public static String politicalData = "politicalData";
    /**
     * 人口类型
     */
    public static String personTypeData = "personTypeData";
    /**
     *职级
     */
    public static String levelData = "levelData";
    /**
     * 与亲属关系
     */
    public static String relationShipData = "relationShipData";
    /**
     * 社会组织类别
     */
    public static String socialOrganizationTypeData = "socialOrganizationTypeData";
    /**
     * 兴趣爱好
     */
    public static String hobbyData = "hobbyData";
    /**
     * 人群标签
     */
    public static String tableTypeData = "tableTypeData";
    /**
     * 建筑结构
     */
    public static String buildingStructureData = "buildingStructureData";
    /**
     * 建筑性质
     */
    public static String buildingNatureData = "buildingNatureData";
    /**
     * 户籍类型
     */
    public static String accTypeData = "accTypeData";
    /**
     * 宗教
     */
    public static String religiousData = "religiousData";
    /**
     * 学历
     */
    public static String educationData = "educationData";
    /**
     * 房产类型
     */
    public static String housePropertyData = "housePropertyData";
    /**
     * 部门类型
     */
    public static String houseFormData = "houseFormData";
    /**
     * 岗位类型
     */
    public static String postData = "postData";

    /**
     * 根据number值查询name
     * @param mapList
     * @param number
     * @return
     */
    public static String getNameByNumber(List<Map<String, Object>> mapList , String number){
        String name = "";
        if(mapList==null||mapList.size()==0|| StringUtils.isBlank(number)){
            return  name;
        }
        List<Map<String, Object>> newList = mapList.stream().filter(item ->item.containsKey("number") &&item.get("number")!=null&& item.get("number").toString().equals(number)).collect(Collectors.toList());
        if(newList!=null && newList.size()>0){
           if(newList.get(0)!=null && newList.get(0).containsKey("name")){
               name = newList.get(0).get("name").toString();
           }
           else {
               name = number;
           }
        }
        else {
            name = number;
        }
        return  name;
    }
}
