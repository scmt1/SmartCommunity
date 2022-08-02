/**
 * yammii.com Inc.
 * Copyright (c) 2018-2020 All Rights Reserved.
 */
package me.zhengjie.controller.party.admin;

import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import me.zhengjie.dto.PartyBaseInformaction;
import me.zhengjie.service.party.IPartyMemberService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;

/**
 * 
 * @author yongyan.pu
 * @version $Id: BaseInformaction.java, v 1.0 2020年8月13日 下午2:32:09 yongyan.pu Exp $
 */
@Api(value = "基本信息", tags = "基本信息")
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/party")
public class BaseInformaction {

    private final IPartyMemberService partyMemberServiceImpl;

    @ApiOperation("基本信息")
    @GetMapping("/baseInfomation")
    public Result<PartyBaseInformaction> baseInfomation(@ApiParam(value = "党委Id") @RequestParam(value = "partyCommitteeId", defaultValue = "") Long partyCommitteeId,
                                                        @ApiParam(value = "支部Id") @RequestParam(value = "partBranchId", defaultValue = "") Long partBranchId) {
        PartyBaseInformaction partyBaseInformaction = partyMemberServiceImpl.getbaseInfomation(partyCommitteeId,
            partBranchId);
        return ResultUtil.data(partyBaseInformaction);
    }

}
