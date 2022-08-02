package me.zhengjie.controller.party.admin;

import javax.validation.Valid;

import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.Result;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import me.zhengjie.common.utils.ResultCode;
import me.zhengjie.controller.party.AbstractController;
import me.zhengjie.entity.party.PartyCommend;
import me.zhengjie.req.PartyCommendReq;
import me.zhengjie.service.party.IPartyCommendService;

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
 * @since 2020-08-13
 */
@Api(value = "党员表彰", tags = "党员表彰")
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/party")
public class PartyCommendController extends AbstractController {

    private final IPartyCommendService partyCommendServiceImpl;

    @ApiOperation("表彰列表")
    @GetMapping("/partycommend")
    public Result<Object> list(@ApiParam(value = "开始时间") @RequestParam(value = "startDate", defaultValue = "") String startDate,
                                       @ApiParam(value = "结束时间") @RequestParam(value = "endDate", defaultValue = "") String endDate,
                                       @ApiParam(value = "党员Id") @RequestParam(value = "partyMemberId", defaultValue = "") Long partyMemberId,
                                       @ApiParam(value = "当前页") @RequestParam(value = "current", defaultValue = "1") Integer current,
                                       @ApiParam(value = "每页大小") @RequestParam(value = "size", defaultValue = "10") Integer size) {
        IPage<PartyCommend> pageResult = partyCommendServiceImpl.getList(partyMemberId, startDate, endDate, current,
            size);
        return ResultUtil.data(pageResult);
    }

    @ApiOperation("表彰详情")
    @GetMapping("/partycommend/{id}")
    public Result<Object> list(@ApiParam(value = "Id", required = true) @PathVariable("id") Long id) {
        PartyCommend partyCommend = partyCommendServiceImpl.getById(id);
        return ResultUtil.data(partyCommend);
    }

    @ApiOperation("新增")
    @PostMapping("/partycommend")
    public Result<Object> add(@RequestBody @Valid PartyCommendReq PartyCommendReq) {
        partyCommendServiceImpl.add(getUserId(), PartyCommendReq);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    @ApiOperation("修改")
    @PutMapping("/partycommend/{id}")
    public Result<Object> edit(@ApiParam(value = "Id", required = true) @PathVariable("id") Long id,
                          @RequestBody @Valid PartyCommendReq PartyCommendReq) {
        partyCommendServiceImpl.edit(id, getUserId(), PartyCommendReq);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    @ApiOperation("删除")
    @DeleteMapping("/partycommend/{id}")
    public Result<Object> delete(@ApiParam(value = "Id", required = true) @PathVariable("id") Long id) {
        partyCommendServiceImpl.removeById(id);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

}
