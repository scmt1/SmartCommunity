/**
 * yammii.com Inc.
 * Copyright (c) 2018-2020 All Rights Reserved.
 */
package me.zhengjie.controller.party.admin;

import java.util.List;

import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.zhengjie.dto.PartyCategoryDto;
import me.zhengjie.enums.PartyCategoryEnum;
import me.zhengjie.enums.PartyThreeLessonsCategoryEnum;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;

/**
 * 
 * @author yongyan.pu
 * @version $Id: PartyCategoryController.java, v 1.0 2020年8月12日 下午9:43:34 yongyan.pu Exp $
 */

@Api(value = "党员分类", tags = "党员分类")
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/party")
public class PartyCategoryController {

    @ApiOperation("分类列表")
    @GetMapping("/category")
    public Result<Object> list() {
        List<PartyCategoryDto> list = PartyCategoryEnum.getPartCateoryList();
        return ResultUtil.data(list);
    }

    @ApiOperation("三学一做分类列表")
    @GetMapping("/threelessonscategory")
    public Result<Object> threeLessonsCategory() {
        List<PartyCategoryDto> list = PartyThreeLessonsCategoryEnum.getPartCateoryList();
        return ResultUtil.data(list);
    }
}
