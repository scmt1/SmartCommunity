package me.zhengjie.controller.party.admin;

import java.util.List;

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

import me.zhengjie.common.utils.ResultCode;
import me.zhengjie.controller.party.AbstractController;
import me.zhengjie.entity.party.PartyBanner;
import me.zhengjie.req.PartyBannerReq;
import me.zhengjie.service.party.IPartyBannerService;

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
@Api(value = "党务公开-轮播图", tags = "党务公开-轮播图")
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/party")
public class PartyBannerController extends AbstractController {

    private final IPartyBannerService iPartyBannerService;

    @ApiOperation("列表")
    @GetMapping("/banner")
    public Result<List<PartyBanner>> list(@ApiParam(value = "title") @RequestParam(value = "title", defaultValue = "") String title) {
        List<PartyBanner> banners = iPartyBannerService.getList(title);
        return ResultUtil.data(banners);
    }

    @ApiOperation("新增")
    @PostMapping("/banner")
    public Result<Object> add(@RequestBody @Valid PartyBannerReq partyBannerReq) {
        iPartyBannerService.add(getUserId(), partyBannerReq);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    @ApiOperation("修改")
    @PutMapping("/banner/{id}")
    public Result<Object> edit(@ApiParam(value = "Id", required = true) @PathVariable("id") Long id,
                          @RequestBody @Valid PartyBannerReq partyBannerReq) {
        iPartyBannerService.edit(id, getUserId(), partyBannerReq);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    @ApiOperation("删除")
    @DeleteMapping("/banner/{id}")
    public Result<Object> delete(@ApiParam(value = "Id", required = true) @PathVariable("id") Long id) {
        iPartyBannerService.removeById(id);
        return ResultUtil.success(ResultCode.SUCCESS);
    }
}
