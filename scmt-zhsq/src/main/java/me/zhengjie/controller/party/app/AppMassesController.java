package me.zhengjie.controller.party.app;

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
import me.zhengjie.dto.AppPartyMassessListDto;
import me.zhengjie.dto.PartyMassessDetail;
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
@Api(value = "党群活动", tags = "党群活动")
@RestController
@AllArgsConstructor
@RequestMapping("/app/party")
public class AppMassesController extends AbstractController {

    private final IPartyMassesService        partyMassesServiceImpl;

    private final IPartyMassessEnrollService partyMassessEnrollService;

    @ApiOperation("党群活动列表")
    @GetMapping("/partymasses")
    public Result<Object> list(@ApiParam(value = "当前页") @RequestParam(value = "current", defaultValue = "1") Integer current,
                               @ApiParam(value = "每页大小") @RequestParam(value = "size", defaultValue = "10") Integer size) {

        IPage<AppPartyMassessListDto> list = partyMassesServiceImpl.getPartyMassessList(getUserInfomation(), current,
            size);
        return ResultUtil.data(list);
    }

    @ApiOperation("党群活动詳情")
    @GetMapping("/partymasses/{id}")
    public void list(HttpServletResponse response,
                     @ApiParam(value = "Id", required = true) @PathVariable("id") Long id) throws Exception {
        partyMassesServiceImpl.getPartyMassessDetailById(response, id);

    }

    @ApiOperation("当前活动-报名参加")
    @PostMapping("/partymasses/enroll/{id}")
    public Result<Object> enroll(@ApiParam(value = "Id", required = true) @PathVariable("id") Long id) {
        partyMassessEnrollService.enroll(id, getUserInfomation());
        return ResultUtil.success(ResultCode.SUCCESS);
    }
}
