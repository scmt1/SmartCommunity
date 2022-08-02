/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2020 All Rights Reserved.
 */
package me.zhengjie.controller.party.admin;

import java.util.Date;

import javax.validation.Valid;

import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.Result;
import me.zhengjie.controller.party.AbstractController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;

import me.zhengjie.common.utils.ResultCode;

import me.zhengjie.entity.party.PartyInOutApply;
import me.zhengjie.req.AuditReq;
import me.zhengjie.req.ReasonReq;
import me.zhengjie.service.party.IPartyInOutApplyService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-12
 */

@Api(value = "党员管理-档案转入转出", tags = "党员管理-档案转入转出")
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/party")
public class PartyInOutApplyController extends AbstractController {

    private final IPartyInOutApplyService partyInOutApplyService;

    @ApiOperation("列表")
    @GetMapping("/inoutlist")
    public Result<Object> list(@ApiParam(value = "姓名") @RequestParam(value = "name", defaultValue = "") String name,
                                          @ApiParam(value = "性别（1、男 2、女）") @RequestParam(value = "gender", defaultValue = "") Integer gender,
                                          @ApiParam(value = "文化程度(（1、初中 2、高中 2、大专 3、本科 4、研究生 5、博士）)") @RequestParam(value = "educationId", defaultValue = "") Integer educationId,
                                          @ApiParam(value = "身份证号码") @RequestParam(value = "idCardNo", defaultValue = "") String idCardNo,
                                          @ApiParam(value = "电话号码") @RequestParam(value = "phoneNumber", defaultValue = "") String phoneNumber,
                                          @ApiParam(value = "状态( 1：转入 2：转出 3：通过 4：驳回)") @RequestParam(value = "applyStatus", defaultValue = "") Integer applyStatus,
                                          @ApiParam(value = "开始时间") @RequestParam(value = "startTime",required = false) Date startTime,
                                          @ApiParam(value = "结束时间") @RequestParam(value = "endTime" ,required = false) Date endTime,
                                          @ApiParam(value = "当前页") @RequestParam(value = "current", defaultValue = "1") Integer current,
                                          @ApiParam(value = "每页大小") @RequestParam(value = "size", defaultValue = "10") Integer size) {
        IPage<PartyInOutApply> pageResult = partyInOutApplyService.getList(name, gender, educationId, idCardNo,
            phoneNumber, applyStatus, startTime, endTime, current, size);
        return ResultUtil.data(pageResult);
    }

    @ApiOperation("审核通过")
    @PostMapping("/inoutadopt/{id}")
    public Result<Object> inoutAdopt(@ApiParam(value = "id", required = true) @PathVariable("id") Long id,
                                     @RequestBody @Valid AuditReq audit) {
        partyInOutApplyService.adopt(getUserId(), audit, id);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    @ApiOperation("审核驳回")
    @PostMapping("/inoutreject/{id}")
    public Result<Object> inoutReject(@ApiParam(value = "id", required = true) @PathVariable("id") Long id,
                                 @RequestBody ReasonReq ReasonReq) {
        partyInOutApplyService.reject(getUserId(), id, ReasonReq.getRejectReason());
        return ResultUtil.success(ResultCode.SUCCESS);
    }

}
