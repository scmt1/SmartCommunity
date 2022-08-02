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
import me.zhengjie.entity.party.PartyBranch;
import me.zhengjie.req.PartyBranchReq;
import me.zhengjie.service.party.IPartyBranchService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;

/**
 * <p>
 * 党支部 前端控制器
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-12
 */
@Api(value = "党支部管理", tags = "党支部管理")
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/party")
public class PartyBranchController extends AbstractController {

    private final IPartyBranchService partyBranchServiceImpl;

    @ApiOperation("党支部列表")
    @GetMapping("/partybranch")
    public Result<Object> list(@ApiParam(value = "组织名称") @RequestParam(value = "name", defaultValue = "") String name,
                                      @ApiParam(value = "辖区Id") @RequestParam(value = "jurisdictionId", defaultValue = "") Long jurisdictionId,
                                      @ApiParam(value = "党委Id") @RequestParam(value = "partyCommitteeId", defaultValue = "") Long partyCommitteeId,
                                      @ApiParam(value = "书记") @RequestParam(value = "secretary", defaultValue = "") String secretary,
                                      @ApiParam(value = "电话号码") @RequestParam(value = "phoneNumber", defaultValue = "") String phoneNumber,
                                      @ApiParam(value = "当前页") @RequestParam(value = "current", defaultValue = "1") Integer current,
                                      @ApiParam(value = "每页大小") @RequestParam(value = "size", defaultValue = "10") Integer size) {
        IPage<PartyBranch> pageResult = partyBranchServiceImpl.getList(name, jurisdictionId, partyCommitteeId,
            secretary, phoneNumber, current, size);
        return ResultUtil.data(pageResult);
    }

    @ApiOperation("新增")
    @PostMapping("/partybranch")
    public Result<Object> addPartyCommittee(@RequestBody @Valid PartyBranchReq partyBranchReq) {
        partyBranchServiceImpl.add(getUserId(), partyBranchReq);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    @ApiOperation("修改")
    @PutMapping("/partybranch/{id}")
    public Result<Object> edit(@ApiParam(value = "Id", required = true) @PathVariable("id") Long id,
                          @RequestBody @Valid PartyBranchReq partyBranchReq) {
        partyBranchServiceImpl.edit(id, getUserId(), partyBranchReq);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    @ApiOperation("删除")
    @DeleteMapping("/partybranch/{id}")
    public Result<Object> delete(@ApiParam(value = "Id", required = true) @PathVariable("id") Long id) {
        partyBranchServiceImpl.removeById(id);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

}
