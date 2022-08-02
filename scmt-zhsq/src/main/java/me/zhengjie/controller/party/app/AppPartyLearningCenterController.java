package me.zhengjie.controller.party.app;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.extension.api.R;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;

import me.zhengjie.common.utils.ResultCode;
import me.zhengjie.controller.party.AbstractController;
import me.zhengjie.dto.LearningMaterialsDto;
import me.zhengjie.dto.PartyCategoryDto;
import me.zhengjie.dto.PartyLearningCenterDto;
import me.zhengjie.dto.PartyThreeLessonsDto;
import me.zhengjie.dto.PartyThreeLessonsListDto;
import me.zhengjie.enums.PartyThreeLessonsCategoryEnum;
import me.zhengjie.service.party.IPartyLearningCenterService;
import me.zhengjie.service.party.IPartyLearningMaterialsService;
import me.zhengjie.service.party.IPartyThreeLessonsService;

import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
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
@RequestMapping("/app/party")
public class AppPartyLearningCenterController extends AbstractController {

    private final IPartyLearningCenterService    partyLearningCenterService;

    private final IPartyThreeLessonsService      partyThreeLessonsService;

    private final IPartyLearningMaterialsService partyLearningMaterialsService;

    @ApiOperation("三学一做分类列表")
    @GetMapping("/threelessonscategory")
    public Result<Object>  threeLessonsCategory() {
        List<PartyCategoryDto> list = PartyThreeLessonsCategoryEnum.getPartCateoryList();
        return ResultUtil.data(list);
    }

    @ApiOperation("三会一课列表")
    @GetMapping("/threelessons/{partyCategoryId}")
    public Result<Object>  list(@ApiParam(value = "partyCategoryId", required = true) @PathVariable("partyCategoryId") Integer partyCategoryId,
                                                   @ApiParam(value = "当前页") @RequestParam(value = "current", defaultValue = "1") Integer current,
                                                   @ApiParam(value = "每页大小") @RequestParam(value = "size", defaultValue = "10") Integer size) {

        IPage<PartyThreeLessonsListDto> pageResult = partyThreeLessonsService.getListByCategory(partyCategoryId,
            current, size);
        return ResultUtil.data(pageResult);
    }

    @ApiOperation("三会一课详情")
    @GetMapping("/threelessons/detail/{id}")
    public Result<Object>  partyThreeLessonsDetail(@ApiParam(value = "id", required = true) @PathVariable("id") Long id) {
        PartyThreeLessonsDto dto = partyThreeLessonsService.detail(id);
        return ResultUtil.data(dto);
    }

    @ApiOperation("学习中心列表")
    @GetMapping("/learningcenter")
    public Result<Object>  learningCenterlist(@ApiParam(value = "当前页") @RequestParam(value = "current", defaultValue = "1") Integer current,
                                                               @ApiParam(value = "每页大小") @RequestParam(value = "size", defaultValue = "10") Integer size) {
        IPage<PartyLearningCenterDto> pageResult = partyLearningCenterService.getLearnCenterList(getUserId(), current,
            size);

        return ResultUtil.data(pageResult);
    }

    @ApiOperation("学习中心详情")
    @GetMapping("/learningcenter/detail/{id}")
    public void partyLearningDetail(HttpServletResponse response,
                                    @ApiParam(value = "id", required = true) @PathVariable("id") Long id) throws TemplateNotFoundException,
                                                                                                          MalformedTemplateNameException,
                                                                                                          ParseException,
                                                                                                          IOException,
                                                                                                          TemplateException {
        partyLearningCenterService.getLearningDetail(response, id);

    }

    @ApiOperation("完成学习")
    @PostMapping("/learningcenter/complete/{id}")
    public Result<Object> completeLearning(@ApiParam(value = "id", required = true) @PathVariable("id") Long id) {
        partyLearningCenterService.complete(getUserInfomation(), id);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    @ApiOperation("两学一做")
    @PostMapping("/learningcenter/lxyz")
    public Result<Object>  learningLxyz() {
        LearningMaterialsDto dto = partyLearningMaterialsService.learningLxyzInfo();
        return ResultUtil.data(dto);
    }

    @ApiOperation("模范宣塑")
    @PostMapping("/learningcenter/mfxs")
    public Result<Object>  learningMfxs() {
        LearningMaterialsDto dto = partyLearningMaterialsService.learningMfxsInfo();
        return ResultUtil.data(dto);
    }
}
