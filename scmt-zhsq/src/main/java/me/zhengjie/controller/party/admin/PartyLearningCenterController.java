package me.zhengjie.controller.party.admin;

import javax.validation.Valid;

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
import me.zhengjie.entity.party.PartyLearnCenterEnroll;
import me.zhengjie.entity.party.PartyLearningCenter;
import me.zhengjie.req.PartyLearningCenterSeq;
import me.zhengjie.service.party.IPartyLearningCenterService;

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
@Api(value = "学习中心", tags = "学习中心")
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/party")
public class PartyLearningCenterController extends AbstractController {

    private final IPartyLearningCenterService partyLearningCenterService;

    @ApiOperation("列表")
    @GetMapping("/learningcenter")
    public Result<Object> list(@ApiParam(value = "标题") @RequestParam(value = "title", defaultValue = "") String title,
                                              @ApiParam(value = "当前页") @RequestParam(value = "current", defaultValue = "1") Integer current,
                                              @ApiParam(value = "每页大小") @RequestParam(value = "size", defaultValue = "10") Integer size) {
        IPage<PartyLearningCenter> pageResult = partyLearningCenterService.getList(title, current, size);
        return ResultUtil.data(pageResult);
    }

    @ApiOperation("新增")
    @PostMapping("/learningcenter")
    public Result<Object> addPartyThreeLessons(@RequestBody @Valid PartyLearningCenterSeq partyLearningCenterSeq) {
        partyLearningCenterService.add(getUserId(), partyLearningCenterSeq);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    @ApiOperation("修改")
    @PostMapping("/learningcenter/{id}")
    public Result<Object> updatePartyThreeLessons(@ApiParam(value = "id", required = true) @PathVariable("id") Long id,
                                             @RequestBody @Valid PartyLearningCenterSeq partyLearningCenterSeq) {
        partyLearningCenterService.edit(getUserId(), id, partyLearningCenterSeq);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    @ApiOperation("删除")
    @DeleteMapping("learningcenter/{id}")
    public Result<Object> delete(@ApiParam(value = "id", required = true) @PathVariable("id") Long id) {
        partyLearningCenterService.removeById(id);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    @ApiOperation("完成学习的人员列表")
    @GetMapping("completelearning/{id}")
    public Result<Object> completeLearning(@ApiParam(value = "id", required = true) @PathVariable("id") Long id,
                                                             @ApiParam(value = "当前页") @RequestParam(value = "current", defaultValue = "1") Integer current,
                                                             @ApiParam(value = "每页大小") @RequestParam(value = "size", defaultValue = "10") Integer size) {
        IPage<PartyLearnCenterEnroll> pageResult = partyLearningCenterService.completeLearningMember(id, current, size);

        return ResultUtil.data(pageResult);
    }

}
