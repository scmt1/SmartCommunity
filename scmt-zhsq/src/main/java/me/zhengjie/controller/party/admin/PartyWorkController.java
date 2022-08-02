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
import me.zhengjie.entity.party.PartyWork;
import me.zhengjie.req.PartyWorkReq;
import me.zhengjie.service.party.impl.PartyWorkServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;

/**
 * <p>
 * 党务公开 前端控制器
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-12
 */
@Api(value = "党务公开", tags = "党务公开")
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/party")
public class PartyWorkController extends AbstractController {

    private final PartyWorkServiceImpl partyWorkServiceImpl;

    @ApiOperation("党务公开列表")
    @GetMapping("/partywork")
    public Result<Object> list(@ApiParam(value = "标题") @RequestParam(value = "title", defaultValue = "") String title,
                                    @ApiParam(value = "支部Id") @RequestParam(value = "partBranchId", defaultValue = "") Long partBranchId,
                                    @ApiParam(value = "党委Id") @RequestParam(value = "partyCommitteeId", defaultValue = "") Long partyCommitteeId,
                                    @ApiParam(value = "分类") @RequestParam(value = "partyCategoryId", defaultValue = "") Integer partyCategoryId,
                                    @ApiParam(value = "当前页") @RequestParam(value = "current", defaultValue = "1") Integer current,
                                    @ApiParam(value = "每页大小") @RequestParam(value = "size", defaultValue = "10") Integer size) {
        IPage<PartyWork> pageResult = partyWorkServiceImpl.getList(title, partBranchId, partyCommitteeId,
            partyCategoryId, current, size);
        return ResultUtil.data(pageResult);
    }

    @ApiOperation("新增")
    @PostMapping("/partywork")
    public Result<Object> addPartyWork(@RequestBody @Valid PartyWorkReq partyWorkReq) {
        partyWorkServiceImpl.add(getUserId(), partyWorkReq);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    @ApiOperation("修改")
    @PutMapping("/partywork/{id}")
    public Result<Object> edit(@ApiParam(value = "Id", required = true) @PathVariable("id") Long id,
                          @RequestBody @Valid PartyWorkReq partyWorkReq) {
        partyWorkServiceImpl.edit(id, getUserId(), partyWorkReq);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    @ApiOperation("删除")
    @DeleteMapping("/partywork/{id}")
    public Result<Object> delete(@ApiParam(value = "Id", required = true) @PathVariable("id") Long id) {
        partyWorkServiceImpl.removeById(id);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

}
