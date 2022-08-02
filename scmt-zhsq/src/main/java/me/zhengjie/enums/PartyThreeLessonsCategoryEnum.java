/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2020 All Rights Reserved.
 */
package me.zhengjie.enums;

import java.util.ArrayList;
import java.util.List;

import me.zhengjie.dto.PartyCategoryDto;

/**
 * 三会一课分类
 * @author zoutao
 * @version $Id: PartyThreeLessonsCategoryEnum.java, v 0.1 2020年8月13日 下午9:21:28 zoutao Exp $
 */
public enum PartyThreeLessonsCategoryEnum {

                                           DXZH(1,
                                                "党小组会"), ZBWYH(2,
                                                               "支部委员会"), DZBDH(3,
                                                                               "党支部大会"), DK(4,
                                                                                            "党课");

    /** 编码 */
    private int    code;

    /** 描述 */
    private String desc;

    private PartyThreeLessonsCategoryEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static List<PartyCategoryDto> getPartCateoryList() {
        PartyThreeLessonsCategoryEnum[] categories = PartyThreeLessonsCategoryEnum.values();
        List<PartyCategoryDto> partyCategoryDtos = new ArrayList<>();
        for (PartyThreeLessonsCategoryEnum partyCategoryEnum : categories) {
            PartyCategoryDto categoryDto = new PartyCategoryDto();
            categoryDto.setPartyCategoryId(partyCategoryEnum.getCode());
            categoryDto.setPartyCategoryName(partyCategoryEnum.getDesc());
            partyCategoryDtos.add(categoryDto);
        }
        return partyCategoryDtos;
    }

    public static String getNameByCode(Integer code) {
        PartyThreeLessonsCategoryEnum[] categories = PartyThreeLessonsCategoryEnum.values();
        for (PartyThreeLessonsCategoryEnum partyCategoryEnum : categories) {
            if (code.intValue() == partyCategoryEnum.getCode()) {
                return partyCategoryEnum.getDesc();
            }
        }
        return "";
    }

}
