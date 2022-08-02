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
import me.zhengjie.dto.PartyThreeLessonsDto;
import me.zhengjie.entity.party.PartyThreeLessons;
import me.zhengjie.req.PartyThreeLessonsSeq;
import me.zhengjie.service.party.IPartyThreeLessonsService;

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
@Api(value = "三会一课", tags = "三会一课")
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/party")
public class PartyThreeLessonsController extends AbstractController {

    private final IPartyThreeLessonsService partyThreeLessonsService;

    @ApiOperation("列表")
    @GetMapping("/threelessons")
    public Result<Object> list(@ApiParam(value = "议题") @RequestParam(value = "topic", defaultValue = "") String topic,
                                            @ApiParam(value = "支部Id") @RequestParam(value = "partBranchId", defaultValue = "") Long partBranchId,
                                            @ApiParam(value = "党委Id") @RequestParam(value = "partyCommitteeId", defaultValue = "") Long partyCommitteeId,
                                            @ApiParam(value = "分类") @RequestParam(value = "partyCategoryId", defaultValue = "") Integer partyCategoryId,
                                            @ApiParam(value = "当前页") @RequestParam(value = "current", defaultValue = "1") Integer current,
                                            @ApiParam(value = "每页大小") @RequestParam(value = "size", defaultValue = "10") Integer size) {
        IPage<PartyThreeLessons> pageResult = partyThreeLessonsService.getList(topic, partBranchId, partyCommitteeId,
            partyCategoryId, current, size);
        return ResultUtil.data(pageResult);
    }

    @ApiOperation("新增")
    @PostMapping("/threelessons")
    public Result<Object> addPartyThreeLessons(@RequestBody @Valid PartyThreeLessonsSeq PartyThreeLessonsReq) {
        partyThreeLessonsService.add(getUserId(), PartyThreeLessonsReq);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    @ApiOperation("详情")
    @GetMapping("/threelessons/detail/{id}")
    public Result<Object> partyThreeLessonsDetail(@ApiParam(value = "id", required = true) @PathVariable("id") Long id) {
        PartyThreeLessonsDto dto = partyThreeLessonsService.detail(id);
        return ResultUtil.data(dto);
    }

    @ApiOperation("修改")
    @PostMapping("/threelessons/{id}")
    public Result<Object> updatePartyThreeLessons(@ApiParam(value = "id", required = true) @PathVariable("id") Long id,
                                             @RequestBody @Valid PartyThreeLessonsSeq PartyThreeLessonsReq) {
        partyThreeLessonsService.edit(getUserId(), id, PartyThreeLessonsReq);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    @ApiOperation("删除")
    @DeleteMapping("threelessons/{id}")
    public Result<Object> delete(@ApiParam(value = "id", required = true) @PathVariable("id") Long id) {
        partyThreeLessonsService.removeById(id);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

}
