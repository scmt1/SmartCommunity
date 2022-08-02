package me.zhengjie.util;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import me.zhengjie.entity.GridDictionary;
import me.zhengjie.mapper.GridDeptMapper;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 字典数据
 */
public class DictionaryUtils {

    private GridDeptMapper deptMapper;

    private static List<GridDictionary> gridDictionaries;

    public DictionaryUtils(GridDeptMapper deptMapper) {
        this.deptMapper = deptMapper;
    }

    public void init() {
        gridDictionaries = deptMapper.loadAll();
    }

    /**
     * 物业类型
     *
     * @param type
     * @return
     */
    public String propertyType(String type) {
        String propertyType = selectDictionaries("communityPriorityData", type);
        return propertyType;
    }

    /**
     * 性别
     *
     * @param type
     * @return
     */
    public String sexType(String type) {
        String sexType = "";
        switch (type) {
            case "男": {
                sexType = "1";
                break;
            }
            case "女": {
                sexType = "2";
                break;
            }
            default: {
                sexType = "1";
            }
        }
        return sexType;
    }

    /**
     * 民族
     *
     * @param type
     * @return
     */
    public String nationType(String type) {
        String nationType = selectDictionaries("nationData", type);
        return nationType;
    }

    /**
     * 特殊人群
     *
     * @param type
     * @return
     */
    public String specialPopulationType(String type) {
        String specialPopulationType = selectDictionaries("specialPersonData", type);
        return specialPopulationType;
    }

    /**
     * 人口类型
     *
     * @param type
     * @return
     */
    public String personType(String type) {
        String personType = selectDictionaries("personTypeDatas", type);
        return personType;
    }

    /**
     * 政治面貌
     *
     * @param type
     * @return
     */
    public String politicalFaceType(String type) {
        String personType = selectDictionaries("politicalData", type);
        return personType;
    }

    /**
     * 档案类型
     *
     * @param type
     * @return
     */
    public Integer tableType(String type) {
        Integer tableType = Integer.parseInt(selectDictionaries("tableTypeData", type));
        return tableType;
    }

    /**
     * 与户主关系
     *
     * @param type
     * @return
     */
    public String accRelationType(String type) {
        String accRelation = selectDictionaries("relationShipData", type);
        ;
        return accRelation;
    }


    /**
     * 岗位级别
     *
     * @param type
     * @return
     */
    public String levelData(String type) {
        String acc = selectDictionaries("levelData", type);
        return acc;
    }

    /**
     * 婚姻状况
     *
     * @param type
     * @return
     */
    public String maritalStatus(String type) {
        String acc = selectDictionaries("maritalStatus", type);
        return acc;
    }

    /**
     * 学历
     *
     * @param type
     * @return
     */
    public String education(String type) {
        String acc = selectDictionaries("education", type);
        return acc;
    }

    /**
     * 宗教信仰
     *
     * @param type
     * @return
     */
    public String religious(String type) {
        String acc = selectDictionaries("religious", type);
        return acc;
    }

    /**
     * 党员关系管理地
     *
     * @param type
     * @return
     */
    public String partyRelationshipManagemen(String type) {
        String acc = selectDictionaries("partyRelationshipManagemen", type);
        return acc;
    }

    /**
     * 兵役情况
     *
     * @param type
     * @return
     */
    public String militaryService(String type) {
        String acc = selectDictionaries("militaryService", type);
        return acc;
    }

    /**
     * 兴趣爱好
     *
     * @param type
     * @return
     */
    public String hobby(String type) {
        String acc = selectDictionaries("hobbyData", type);
        return acc;
    }

    /**
     * 户别
     *
     * @param type
     * @return
     */
    public String accTypeData(String type) {
        String acc = selectDictionaries("accTypeData", type);
        return acc;
    }

    /**
     * 房屋类型
     *
     * @param type
     * @return
     */
    public String houseType(String type) {
        String acc = selectDictionaries("houseType", type);
        return acc;
    }

    /**
     * 房屋产权
     *
     * @param type
     * @return
     */
    public String housePropertyData(String type) {
        String acc = selectDictionaries("housePropertyData", type);
        return acc;
    }

    /**
     * 房型
     *
     * @param type
     * @return
     */
    public String houseFormData(String type) {
        String acc = selectDictionaries("houseFormData", type);
        return acc;
    }

    /**
     * 房屋分类
     *
     * @param type
     * @return
     */
    public String houseClassification(String type) {
        String acc = selectDictionaries("houseClassification", type);
        return acc;
    }
    /**
     * 房屋性质
     *
     * @param type
     * @return
     */
    public String houseNature(String type) {
        String acc = selectDictionaries("houseNature", type);
        return acc;
    }
    /**
     * 建筑性质
     *
     * @param type
     * @return
     */
    public String buildingNatureData(String type) {
        String acc = selectDictionaries("buildingNatureData", type);
        return acc;
    }

    /**
     * 获取建筑类型
     *
     * @param type
     * @return
     */
    public String buildingType(String type) {
        String acc = selectDictionaries("building_type", type);
        return acc;
    }

    /**
     * 建筑结构
     *
     * @param type
     * @return
     */
    public String buildingStructureData(String type) {
        String acc = selectDictionaries("buildingStructureData", type);
        return acc;
    }

    /**
     * 根据 fileName 和 name 匹配字典
     *
     * @param fileName
     * @param name
     * @return
     */
    public String selectDictionaries(String fileName, String name) {
        String String = "";
        if (StringUtils.isBlank(fileName) || StringUtils.isBlank(name)) {
            return String;
        }
        List<GridDictionary> collect = gridDictionaries.stream().filter(item -> item.getName() != null && item.getFileName() != null && name.contains(item.getName()) && fileName.contains(item.getFileName())).collect(Collectors.toList());
        if (collect != null && collect.size() > 0) {
            GridDictionary gridDictionary = collect.get(0);
            if (gridDictionary != null) {
                String = gridDictionary.getNumber();
            }
        }
        return String;
    }

    /**
     * 校验姓名
     *
     * @param str
     * @return
     */
    public static boolean isChinese(String str) {
        if (StringUtils.isBlank(str)) {
            return true;
        }
        String regex = "^([\\u4e00-\\u9fa5]{2,50}|[a-zA-Z.\\s]{2,50})$";
        return str.matches(regex);
    }
    /**
     * 校验数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        for (int i = 0; i < str.length(); i++){
            System.out.println(str.charAt(i));
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;

    }

    /**
     * 校验是否包含字母
     *
     * @param str
     * @return
     */
    public static Boolean checkEnglish(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        Pattern p = Pattern.compile("[a-zA-Z]");
        if (p.matcher(str).find()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断字符串是否是yyyyMMdd格式
     *
     * @param mes
     * @return
     */
    public static Boolean IsDate(String mes) {
        if (StringUtils.isBlank(mes)) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyMMdd");
        try {
            sdf.parse(mes);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断字符串为是或者否
     *
     * @param mes
     * @return
     */
    public static Boolean IsTrue(String mes) {
        if (StringUtils.isBlank(mes)) {
            return false;
        }
        if ("是".equals(mes) || "否".equals(mes)) {
            return true;
        }
        return false;
    }
}
