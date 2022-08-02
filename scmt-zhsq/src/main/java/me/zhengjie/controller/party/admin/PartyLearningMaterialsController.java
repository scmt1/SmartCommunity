package me.zhengjie.controller.party.admin;

import javax.validation.Valid;

import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.zhengjie.common.utils.ResultCode;
import me.zhengjie.controller.party.AbstractController;
import me.zhengjie.dto.LearningMaterialsDto;
import me.zhengjie.enums.PartyLearningMaterialsSourceEnum;
import me.zhengjie.req.PartyLearningMaterialsSeq;
import me.zhengjie.service.party.IPartyLearningMaterialsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;

/**
 * <p>
 * 党建学习资料（模范宣塑,两学一做) 前端控制器
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-13
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/party")
@Api(value = "党建学习资料（模范宣塑,两学一做)", tags = "党建学习资料（模范宣塑,两学一做)")
public class PartyLearningMaterialsController extends AbstractController {

    private final IPartyLearningMaterialsService partyLearningMaterialsService;

    @ApiOperation("新增（模范宣塑,两学一做）")
    @PostMapping("/learningmaterials")
    public Result<Object> add(@RequestBody @Valid PartyLearningMaterialsSeq partyLearningMaterialsSeq) {
        partyLearningMaterialsService.add(getUserId(), partyLearningMaterialsSeq);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    @ApiOperation("查询（模范宣塑,两学一做）")
    @GetMapping("/learningmaterials/{sourceType}")
    public Result<Object> getByType(@ApiParam(value = "sourceType 1:两学一做，2：模范宣塑:", required = true) @PathVariable("sourceType") Integer sourceType) {
        LearningMaterialsDto LearningMaterialsDto = partyLearningMaterialsService.getPartyLearningMaterials(
            sourceType == 1 ? PartyLearningMaterialsSourceEnum.LXYZ : PartyLearningMaterialsSourceEnum.MFXS);
        return ResultUtil.data(LearningMaterialsDto);
    }
}
