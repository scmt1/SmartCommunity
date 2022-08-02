package me.zhengjie.controller.party.app;

import java.util.List;

import com.baomidou.mybatisplus.extension.api.R;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import me.zhengjie.entity.party.PartyBanner;
import me.zhengjie.service.party.IPartyBannerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-13
 */
@Api(value = "党务公开-轮播图", tags = "党务公开-轮播图")
@RestController
@AllArgsConstructor
@RequestMapping("/app/party")
public class AppBannerController {

    private final IPartyBannerService partyBannerServiceImpl;

    @ApiOperation("列表")
    @GetMapping("/banner")
    public Result<Object> list() {
        List<PartyBanner> banners = partyBannerServiceImpl.list();
        return ResultUtil.data(banners);
    }

}
