/**
 * yammii.com Inc.
 * Copyright (c) 2018-2020 All Rights Reserved.
 */
package me.zhengjie.controller.party.app;

import com.baomidou.mybatisplus.extension.api.R;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.Result;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;

import me.zhengjie.controller.party.AbstractController;
import me.zhengjie.dto.MyArchivesDto;
import me.zhengjie.dto.MyDuesDto;
import me.zhengjie.dto.UserInfomation;
import me.zhengjie.dto.Userinfo;
import me.zhengjie.service.party.IPartyDuesService;
import me.zhengjie.service.party.IPartyInOutApplyService;
import me.zhengjie.service.party.IPartyMemberService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author yongyan.pu
 * @version $Id: UserCenterConotroller.java, v 1.0 2020年8月13日 下午11:35:51 yongyan.pu Exp $
 */
@Slf4j
@Api(value = "个人中心", tags = "个人中心")
@RestController
@AllArgsConstructor
@RequestMapping("/app/user")
public class UserCenterConotroller extends AbstractController {

    private final IPartyMemberService     partyMemberService;

    private final IPartyDuesService       partyDuesService;

    private final IPartyInOutApplyService partyInOutApplyService;

    @ApiOperation("个人中心")
    @GetMapping("/userInfo")
    public Result<Object>  userInfo() {
        UserInfomation userInfomation = getUserInfomation();
        Userinfo userinfo = partyInOutApplyService.getUserInfo(userInfomation);
        userinfo.setIdCardNo(userInfomation.getIdNumber());
        userinfo.setNickName(userInfomation.getNickName());

        if (!StringUtils.isEmpty(userInfomation.getSex())) {
            userinfo.setGender(userInfomation.getSex().equals("男") ? 1 : 0);
        }
        if (!StringUtils.isEmpty(userInfomation.getAvatar())) {
            userinfo.setHeadSculpture(userInfomation.getAvatar());
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(userinfo.getName())) {
            userinfo.setName(userInfomation.getRealName());
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(userinfo.getHeadSculpture())) {
            userinfo.setHeadSculpture(userInfomation.getAvatar());
        }
        if (StringUtils.isEmpty(userinfo.getPhoneNumber())) {
            userinfo.setPhoneNumber(userInfomation.getPhone());
        }
        log.info("userInfo:{}", JSON.toJSONString(userinfo));
        return ResultUtil.data(userinfo);
    }

    @ApiOperation("我的档案")
    @GetMapping("/archives")
    public Result<Object> detail() {
        UserInfomation userInfomation = getUserInfomation();
        String IdNumber = userInfomation.getIdNumber();
        MyArchivesDto myArchivesDto = partyMemberService.getArchivesByIdCardNo(IdNumber);
        if (!StringUtils.isEmpty(userInfomation.getAvatar())) {
            myArchivesDto.setHeadSculpture(userInfomation.getAvatar());
        }
        return ResultUtil.data(myArchivesDto);
    }

    @ApiOperation("党费缴纳列表情况")
    @GetMapping("/dues")
    public Result<Object>  dues(@ApiParam(value = "当前页") @RequestParam(value = "current", defaultValue = "1") Integer current,
                                    @ApiParam(value = "每页大小") @RequestParam(value = "size", defaultValue = "10") Integer size) {
        String IdNumber = getUserInfomation().getIdNumber();
        IPage<MyDuesDto> page = partyDuesService.getDuesDtoByIdCardNo(IdNumber, current, size);
        return ResultUtil.data(page);
    }

}
