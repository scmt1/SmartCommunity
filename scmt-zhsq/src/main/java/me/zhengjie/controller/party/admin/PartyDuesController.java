package me.zhengjie.controller.party.admin;

import java.util.Date;

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
import me.zhengjie.entity.party.PartyDues;
import me.zhengjie.req.PartyDuesReq;
import me.zhengjie.service.party.IPartyDuesService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;

/**
 * <p>
 * 党费 前端控制器
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-13
 */

@Api(value = "党费缴纳", tags = "党费缴纳")
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/party")
public class PartyDuesController extends AbstractController {

    private final IPartyDuesService partyDuesServiceImpl;

    @ApiOperation("党员缴纳列表")
    @GetMapping("/partydues")
    public Result<Object> list(@ApiParam(value = "开始时间") @RequestParam(value = "startDate", defaultValue = "") Date startDate,
                                    @ApiParam(value = "结束") @RequestParam(value = "endDate", defaultValue = "") Date endDate,
                                    @ApiParam(value = "党员iD") @RequestParam(value = "partyMemberId", defaultValue = "") Long partyMemberId,
                                    @ApiParam(value = "当前页") @RequestParam(value = "current", defaultValue = "1") Integer current,
                                    @ApiParam(value = "每页大小") @RequestParam(value = "size", defaultValue = "10") Integer size) {
        IPage<PartyDues> pageResult = partyDuesServiceImpl.getList(partyMemberId, startDate, endDate, current, size);
        return ResultUtil.data(pageResult);
    }

    @ApiOperation("新增")
    @PostMapping("/partydues")
    public Result<Object> addPartyCommittee(@RequestBody @Valid PartyDuesReq partyDuesReq) {
        partyDuesServiceImpl.add(getUserId(), partyDuesReq);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    @ApiOperation("修改")
    @PutMapping("/partydues/{id}")
    public Result<Object> edit(@ApiParam(value = "Id", required = true) @PathVariable("id") Long id,
                          @RequestBody @Valid PartyDuesReq partyDuesReq) {
        partyDuesServiceImpl.edit(id, getUserId(), partyDuesReq);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    @ApiOperation("删除")
    @DeleteMapping("/partydues/{id}")
    public Result<Object> edit(@ApiParam(value = "Id", required = true) @PathVariable("id") Long id) {
        partyDuesServiceImpl.removeById(id);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

}
