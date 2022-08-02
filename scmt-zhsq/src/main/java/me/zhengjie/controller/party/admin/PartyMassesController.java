package me.zhengjie.controller.party.admin;

import javax.validation.Valid;

import com.baomidou.mybatisplus.extension.api.R;
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
import me.zhengjie.entity.party.PartyMasses;
import me.zhengjie.entity.party.PartyMassessEnroll;
import me.zhengjie.req.PartyMassesReq;
import me.zhengjie.service.party.IPartyMassesService;
import me.zhengjie.service.party.IPartyMassessEnrollService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;

/**
 * <p>
 * 党群  前端控制器
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-13
 */
@Api(value = "党群管理", tags = "党群管理")
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/party")
public class PartyMassesController extends AbstractController {

    private final IPartyMassesService        partyMassesServiceImpl;

    private final IPartyMassessEnrollService partyMassessEnrollService;

    @ApiOperation("党群列表")
    @GetMapping("/partymasses")
    public Result<Object> list(@ApiParam(value = "标题") @RequestParam(value = "title", defaultValue = "") String title,
                                      @ApiParam(value = "支部Id") @RequestParam(value = "partBranchId", defaultValue = "") Long partBranchId,
                                      @ApiParam(value = "党委Id") @RequestParam(value = "partyCommitteeId", defaultValue = "") Long partyCommitteeId,
                                      @ApiParam(value = "分类") @RequestParam(value = "partyCategoryId", defaultValue = "") Integer partyCategoryId,
                                      @ApiParam(value = "当前页") @RequestParam(value = "current", defaultValue = "1") Integer current,
                                      @ApiParam(value = "每页大小") @RequestParam(value = "size", defaultValue = "10") Integer size) {
        IPage<PartyMasses> pageResult = partyMassesServiceImpl.getList(title, partBranchId, partyCommitteeId,
            partyCategoryId, current, size);
        return ResultUtil.data(pageResult);
    }

    @ApiOperation("党群列表-报名列表")
    @GetMapping("/partymasses/{partymassesId}/enroll")
    public Result<Object> enrollList(@ApiParam(value = "partymassesId", required = true) @PathVariable("partymassesId") Long partymassesId,
                                                   @ApiParam(value = "当前页") @RequestParam(value = "current", defaultValue = "1") Integer current,
                                                   @ApiParam(value = "每页大小") @RequestParam(value = "size", defaultValue = "10") Integer size) {
        IPage<PartyMassessEnroll> pageResult = partyMassessEnrollService.getByPartymassesId(partymassesId, current,
            size);
        return ResultUtil.data(pageResult);
    }

    @ApiOperation("新增")
    @PostMapping("/partymasses")
    public Result<Object> addPartyCommittee(@RequestBody @Valid PartyMassesReq partyMassesReq) {
        partyMassesServiceImpl.add(getUserId(), partyMassesReq);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    @ApiOperation("修改")
    @PutMapping("/partymasses/{id}")
    public Result<Object> edit(@ApiParam(value = "Id", required = true) @PathVariable("id") Long id,
                          @RequestBody @Valid PartyMassesReq partyMassesReq) {
        partyMassesServiceImpl.edit(id, getUserId(), partyMassesReq);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    @ApiOperation("删除")
    @DeleteMapping("/partymasses/{id}")
    public Result<Object> del(@ApiParam(value = "Id", required = true) @PathVariable("id") Long id) {
        partyMassesServiceImpl.removeById(id);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    @ApiOperation("取消")
    @PutMapping("/partymasses/{id}/cancel")
    public Result<Object> cancel(@ApiParam(value = "Id", required = true) @PathVariable("id") Long id) {
        partyMassesServiceImpl.cancel(id);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

}
