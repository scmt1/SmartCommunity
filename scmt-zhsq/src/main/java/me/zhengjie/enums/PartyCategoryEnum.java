/**
 * yammii.com Inc.
 * Copyright (c) 2018-2020 All Rights Reserved.
 */
package me.zhengjie.enums;

import java.util.ArrayList;
import java.util.List;

import me.zhengjie.dto.PartyCategoryDto;

/**
 * @author yongyan.pu
 * @version $Id: PartyCategoryEnum.java, v 1.0 2020年8月12日 下午9:36:12 yongyan.pu Exp $
 */
public enum PartyCategoryEnum {

   ALL(0,"全部"), ZBWY(1,"支部委员"), ZBDY(2,"支部党员"), RDJJFZ(3,"入党积极分子"), XZRDY(4,"新转入党员"), TCDY(5,"退出党员"), TDDY(6,"退党党员"), QCDY(7,"迁出党员"), SWDY(8,"死亡党员");

    /** 编码 */
    private int    code;

    /** 描述 */
    private String desc;

    private PartyCategoryEnum(int code, String desc) {
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
        PartyCategoryEnum[] partyCategoryEnums = PartyCategoryEnum.values();
        List<PartyCategoryDto> partyCategoryDtos = new ArrayList<>();
        for (PartyCategoryEnum partyCategoryEnum : partyCategoryEnums) {
            PartyCategoryDto categoryDto = new PartyCategoryDto();
            categoryDto.setPartyCategoryId(partyCategoryEnum.getCode());
            categoryDto.setPartyCategoryName(partyCategoryEnum.getDesc());
            partyCategoryDtos.add(categoryDto);
        }
        return partyCategoryDtos;
    }

    public static String getNameByCode(Integer code) {
        if (code == null) {
            return PartyCategoryEnum.ALL.desc;
        }
        PartyCategoryEnum[] partyCategoryEnums = PartyCategoryEnum.values();
        for (PartyCategoryEnum partyCategoryEnum : partyCategoryEnums) {
            if (code.intValue() == partyCategoryEnum.getCode()) {
                return partyCategoryEnum.getDesc();
            }
        }
        return "";
    }
}
