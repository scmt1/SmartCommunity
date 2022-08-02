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
import me.zhengjie.entity.party.PartyCommittee;
import me.zhengjie.req.PartyCommitteeReq;
import me.zhengjie.service.party.IPartyCommitteeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;

/**
 * <p>
 * 党委 前端控制器
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-12
 */
@Api(value = "党委管理", tags = "党委管理")
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/party")
public class PartyCommitteeController extends AbstractController {

    private final IPartyCommitteeService partyCommitteeServiceImpl;

    @ApiOperation("党委列表")
    @GetMapping("/committee")
    public Result<Object> list(@ApiParam(value = "组织名称") @RequestParam(value = "name", defaultValue = "") String name,
                                         @ApiParam(value = "社区Id") @RequestParam(value = "communityId", defaultValue = "") Long communityId,
                                         @ApiParam(value = "街道Id") @RequestParam(value = "streetId", defaultValue = "") Long streetId,
                                         @ApiParam(value = "书记") @RequestParam(value = "secretary", defaultValue = "") String secretary,
                                         @ApiParam(value = "电话号码") @RequestParam(value = "phoneNumber", defaultValue = "") String phoneNumber,
                                         @ApiParam(value = "当前页") @RequestParam(value = "current", defaultValue = "1") Integer current,
                                         @ApiParam(value = "每页大小") @RequestParam(value = "size", defaultValue = "10") Integer size) {
        IPage<PartyCommittee> pageResult = partyCommitteeServiceImpl.getList(name, communityId, streetId, secretary,
            phoneNumber, current, size);
        return ResultUtil.data(pageResult);
    }

    @ApiOperation("新增")
    @PostMapping("/committee")
    public Result<Object> addPartyCommittee(@RequestBody @Valid PartyCommitteeReq partyCommitteeReq) {
        partyCommitteeServiceImpl.add(getUserId(), partyCommitteeReq);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    @ApiOperation("修改")
    @PutMapping("/committee/{id}")
    public Result<Object> edit(@ApiParam(value = "Id", required = true) @PathVariable("id") Long id,
                          @RequestBody @Valid PartyCommitteeReq partyCommitteeReq) {
        partyCommitteeServiceImpl.edit(id, getUserId(), partyCommitteeReq);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    @ApiOperation("删除")
    @DeleteMapping("/committee/{id}")
    public Result<Object> delete(@ApiParam(value = "Id", required = true) @PathVariable("id") Long id) {
        partyCommitteeServiceImpl.deleteById(id);
        return ResultUtil.success(ResultCode.SUCCESS);
    }
}
