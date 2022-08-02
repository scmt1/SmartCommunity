package me.zhengjie.controller.party.admin;

import javax.validation.Valid;

import com.baomidou.mybatisplus.extension.api.R;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.Result;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;

import me.zhengjie.common.utils.ResultCode;
import me.zhengjie.controller.party.AbstractController;
import me.zhengjie.entity.party.PartyMember;
import me.zhengjie.req.PartyMemberReq;
import me.zhengjie.service.party.IPartyMemberService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;

/**
 * <p>
 * 党员基本信息 前端控制器
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-13
 */
@Api(value = "党员管理", tags = "党员管理")
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/party")
public class PartyMemberController extends AbstractController {

    private final IPartyMemberService partyMemberServiceImpl;

    @ApiOperation("党员列表")
    @GetMapping("/partymember")
    public Result<Object> list(@ApiParam(value = "姓名") @RequestParam(value = "name", defaultValue = "") String name,
                                      @ApiParam(value = "性别") @RequestParam(value = "gender", defaultValue = "") Integer gender,
                                      @ApiParam(value = "学历") @RequestParam(value = "educationId", defaultValue = "") Integer educationId,
                                      @ApiParam(value = "身份证") @RequestParam(value = "idCardNo", defaultValue = "") String idCardNo,
                                      @ApiParam(value = "电话号码") @RequestParam(value = "phoneNumber", defaultValue = "") String phoneNumber,
                                      @ApiParam(value = "支部Id") @RequestParam(value = "partBranchId", defaultValue = "") Long partBranchId,
                                      @ApiParam(value = "党委Id") @RequestParam(value = "partyCommitteeId", defaultValue = "") Long partyCommitteeId,
                                      @ApiParam(value = "党员分类") @RequestParam(value = "partyCategoryId", defaultValue = "") Integer partyCategoryId,
                                      @ApiParam(value = "当前页") @RequestParam(value = "current", defaultValue = "1") Integer current,
                                      @ApiParam(value = "每页大小") @RequestParam(value = "size", defaultValue = "10") Integer size) {
        IPage<PartyMember> pageResult = partyMemberServiceImpl.getList(name, gender, educationId, idCardNo, phoneNumber,
            partBranchId, partyCommitteeId, partyCategoryId, current, size);
        return ResultUtil.data(pageResult);
    }

    @ApiOperation("新增")
    @PostMapping("/partymember")
    public Result<Object> add(@RequestBody @Valid PartyMemberReq partyMemberReq) {
        partyMemberServiceImpl.add(getUserId(), partyMemberReq);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    @ApiOperation("修改")
    @PostMapping("/partymember/{id}")
    public Result<Object> edit(@ApiParam(value = "Id", required = true) @PathVariable("id") Long id,
                          @RequestBody @Valid PartyMemberReq partyMemberReq) {
        partyMemberServiceImpl.edit(id, getUserId(), partyMemberReq);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    @ApiOperation("删除")
    @DeleteMapping("partymember/{id}")
    public Result<Object> delete(@ApiParam(value = "Id", required = true) @PathVariable("id") Long id) {
        partyMemberServiceImpl.deleteById(id);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

}
