package me.zhengjie.controller.party.app;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.extension.api.R;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.entity.RelaPersonRelatives;
import me.zhengjie.entity.party.PartyWork;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import me.zhengjie.controller.party.AbstractController;
import me.zhengjie.dto.PartyWorkListDto;
import me.zhengjie.dto.UserInfomation;
import me.zhengjie.entity.party.PartyMember;
import me.zhengjie.service.party.impl.PartyMemberServiceImpl;
import me.zhengjie.service.party.impl.PartyWorkServiceImpl;

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
 * 党务公开 前端控制器
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-12
 */
@Api(value = "党务公开", tags = "党务公开")
@RestController
@AllArgsConstructor
@RequestMapping("/app/party")
public class AppPartyWorkController extends AbstractController {

    private final PartyWorkServiceImpl   partyWorkServiceImpl;

    private final PartyMemberServiceImpl partyMemberServiceImpl;

    @ApiOperation("党务公开列表")
    @GetMapping("/partywork")
    public Result<Object> list(@ApiParam(value = "当前页") @RequestParam(value = "current", defaultValue = "1") Integer current,
                               @ApiParam(value = "每页大小") @RequestParam(value = "size", defaultValue = "10") Integer size) {
        UserInfomation userInfomation = getUserInfomation();
        String idCardNo = userInfomation.getIdNumber();
        PartyMember partyMember = partyMemberServiceImpl.getPartyMemberByIdCardNo(idCardNo);
        if (partyMember == null) {
            IPage<PartyWorkListDto> page = new Page<>(current, size);
            return ResultUtil.data(page);
        }
        Long branchId = partyMember.getPartyBranchId();
        Long partyCommitteeId = partyMember.getPartyCommitteeId();
        Integer partyCategoryId = partyMember.getPartyCategoryId();
        IPage<PartyWorkListDto> pageResult = partyWorkServiceImpl.getList(partyCommitteeId, branchId, partyCategoryId,
            current, size);
        return ResultUtil.data(pageResult);
    }

    @ApiOperation("党务公开详情")
    @GetMapping("/partywork/{id}")
    public void detail(HttpServletResponse response,
                       @ApiParam(value = "Id", required = true) @PathVariable("id") Long id) throws TemplateNotFoundException,
                                                                                             MalformedTemplateNameException,
                                                                                             ParseException,
                                                                                             IOException,
                                                                                             TemplateException {
        partyWorkServiceImpl.getPartyWorkDetai(response, id);
    }

    @ApiOperation("app端党务列表")
    @GetMapping("/getPartyWork")
    public Result<Object> getPartyList(@ApiParam(value = "当前页") @RequestParam(value = "current", defaultValue = "1") Integer current,
                                       @ApiParam(value = "每页大小") @RequestParam(value = "size", defaultValue = "10") Integer size){
        IPage<PartyWork> pageResult = partyWorkServiceImpl.getPartyList(current, size);
        return ResultUtil.data(pageResult);
    }

    @ApiOperation("app端党务详情")
    @GetMapping("/partyDetails")
    public Result<Object> getPartyDetails(@ApiParam(value = "id") @RequestParam(value = "id") Long id){
        PartyWork partyDetails = partyWorkServiceImpl.getById(id);
        return ResultUtil.data(partyDetails);
    }
}
