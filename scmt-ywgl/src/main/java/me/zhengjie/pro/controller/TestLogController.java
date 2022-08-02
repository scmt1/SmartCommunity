package me.zhengjie.pro.controller;

import io.swagger.annotations.Api;
import me.zhengjie.aop.log.Log;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.utils.SecurityUtil;
import me.zhengjie.common.vo.Result;
import me.zhengjie.dao.service.ITLogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dengjie
 * @since 2020-05-21
 */
@Api(tags = "日志")
@RestController
@RequestMapping("/api/tLog")
public class TestLogController {
	@Autowired
	private ITLogService iTLogService;
	@Autowired
    private SecurityUtil securityUtil;

	@Log("获取首页热门物业表格数据")
    @GetMapping("/getHotBusinessTableData")
    public Result<Object> getHotBusinessTableData() {
		
        return new ResultUtil<>().setData(iTLogService.list());
    }


}
