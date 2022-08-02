/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2020 All Rights Reserved.
 */
package me.zhengjie.controller.party.app;;

import javax.validation.Valid;

import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import me.zhengjie.common.utils.ResultCode;
import me.zhengjie.controller.party.AbstractController;
import me.zhengjie.req.ArchivesInReq;
import me.zhengjie.service.party.IPartyInOutApplyService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-12
 */

@Api(value = "我的档案", tags = "我的档案")
@RestController
@AllArgsConstructor
@RequestMapping("/app/party")
public class AppPartyInOutApplyController extends AbstractController {

    private final IPartyInOutApplyService partyInOutApplyService;

    @ApiOperation("转入")
    @PostMapping("/archivesin")
    public Result<Object> archivesIn(@RequestBody @Valid ArchivesInReq archivesInReq) {
        partyInOutApplyService.inApply(getUserInfomation(), archivesInReq);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    @ApiOperation("转出")
    @GetMapping("/archivesout")
    public Result<Object> archivesOut() {
        partyInOutApplyService.outApply(getUserInfomation());
        return ResultUtil.success(ResultCode.SUCCESS);
    }

}
