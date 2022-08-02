package me.zhengjie.modules.mnt.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.aop.log.Log;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.modules.mnt.domain.DeployHistory;
import me.zhengjie.modules.mnt.service.DeployHistoryService;
import me.zhengjie.modules.mnt.service.dto.DeployHistoryDto;
import me.zhengjie.modules.mnt.service.dto.DeployHistoryQueryCriteria;
import me.zhengjie.system.domain.Menu;
import me.zhengjie.utils.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author zhanghouying
 * @date 2019-08-24
 */
@Api(tags = "部署历史管理")
@RestController
@RequestMapping("/api/deployHistory")
public class DeployHistoryController {

    private final DeployHistoryService deployhistoryService;

    public DeployHistoryController(DeployHistoryService deployhistoryService) {
        this.deployhistoryService = deployhistoryService;
    }

    @Log("导出部署历史数据")
    @ApiOperation("导出部署历史数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('deployHistory:list')")
    public void download(HttpServletResponse response, DeployHistoryQueryCriteria criteria) throws IOException {
        deployhistoryService.download(deployhistoryService.queryAll(criteria), response);
    }

    @Log("查询部署历史")
    @ApiOperation(value = "查询部署历史")
    @GetMapping("/getDeployHistorys")
    @PreAuthorize("@el.check('deployHistory:list')")
    public Result<Object> getDeployHistorys(DeployHistoryQueryCriteria criteria, SearchVo vo, Pageable pageable) {
        if (StringUtils.isNotEmpty(vo.getStartDate()) && StringUtils.isNotEmpty(vo.getEndDate())) {
            List<Timestamp> objects = new ArrayList<>();
            Timestamp timestamp1 = Timestamp.valueOf(vo.getStartDate() + " 00:00:00");
            Timestamp timestamp2 = Timestamp.valueOf(vo.getEndDate() + " 00:00:00");
            objects.add(timestamp1);
            objects.add(timestamp2);
            criteria.setDeployDate(objects);
        }
        return new ResultUtil<>().setData(deployhistoryService.queryAll(criteria, pageable));
    }

    @Log("删除DeployHistory")
    @ApiOperation("删除部署历史")
    @PostMapping("/deleteDeployHistory")
    @PreAuthorize("@el.check('deployHistory:del')")
    public Result<Object> delete(@RequestParam(name = "ids[]") String[] ids) {
        Set<String> set = new HashSet<>();
        for (String id : ids) {
            set.add(id);
        }
        deployhistoryService.delete(set);
        return new ResultUtil<>().setSuccessMsg("删除成功！");
    }
}
