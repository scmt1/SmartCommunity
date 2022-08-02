package me.zhengjie.modules.quartz.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.aop.log.Log;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.exception.BadRequestException;

import me.zhengjie.modules.quartz.domain.QuartzJob;
import me.zhengjie.modules.quartz.service.QuartzJobService;
import me.zhengjie.modules.quartz.service.dto.JobQueryCriteria;
import me.zhengjie.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author Zheng Jie
 * @date 2019-01-07
 */
@Slf4j
@RestController
@Api(tags = "系统:定时任务管理")
@RequestMapping("/api/jobs")
public class QuartzJobController {

    private static final String ENTITY_NAME = "quartzJob";

    private final QuartzJobService quartzJobService;
    @Autowired
    private LogService logService;

    public QuartzJobController(QuartzJobService quartzJobService) {
        this.quartzJobService = quartzJobService;
    }

    @Log("查询定时任务")
    @ApiOperation("查询定时任务")
    @GetMapping
    @PreAuthorize("@el.check('timing:list')")
    public ResponseEntity<Object> getJobs(JobQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(quartzJobService.queryAll(criteria,pageable), HttpStatus.OK);
    }

    @Log("查询定时任务")
    @ApiOperation(value = "查询定时任务")
    @GetMapping(value = "queryListByPage")
    @PreAuthorize("@el.check('app:list')")
    public Result<Object>  queryListByPage(JobQueryCriteria criteria, SearchVo searchVo, PageVo pageVo){
        Long time = System.currentTimeMillis();
        try {
            Sort s = new Sort(Sort.Direction.DESC, pageVo.getSort());
            if(pageVo!=null&&"asc".equals(pageVo.getOrder())){
                s = new Sort(Sort.Direction.ASC, pageVo.getSort());
            }
            if(searchVo!=null && searchVo.getEndDate()!=null && searchVo.getStartDate()!=null){
                List<Timestamp> createTime  =  new ArrayList<Timestamp>();

                Timestamp t1 = Timestamp.valueOf(searchVo.getStartDate() +" 00:00:00");
                Timestamp t2 = Timestamp.valueOf(searchVo.getEndDate()+" 00:00:00");
                createTime.add(t1);
                createTime.add(t2);
                criteria.setCreateTime(createTime);
            }
            Pageable pageable =  PageRequest.of(pageVo.getPageNumber()-1,pageVo.getPageSize(),s);
            Object res = quartzJobService.queryAll(criteria, pageable);
            return ResultUtil.data(res, "查询定时任务成功");
        } catch (Exception e) {
            logService.addErrorLog("查询定时任务异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("查询定时任务异常:" + e.getMessage());
        }
    }
    @Log("查询定时任务")
    @ApiOperation(value = "查询定时任务")
    @GetMapping(value = "getById")
    @PreAuthorize("@el.check('app:list')")
    public Result<Object> getById(Long id){
        if (id == null) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            QuartzJob res = quartzJobService.findById(id);

            return ResultUtil.data(res, "查询定时任务成功");
        } catch (Exception e) {
            logService.addErrorLog("查询定时任务异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("查询定时任务异常:" + e.getMessage());
        }
    }

    @Log("导出任务数据")
    @ApiOperation("导出任务数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('timing:list')")
    public void download(HttpServletResponse response, JobQueryCriteria criteria) throws IOException {
        quartzJobService.download(quartzJobService.queryAll(criteria), response);
    }

    @Log("导出日志数据")
    @ApiOperation("导出日志数据")
    @GetMapping(value = "/logs/download")
    @PreAuthorize("@el.check('timing:list')")
    public void downloadLog(HttpServletResponse response, JobQueryCriteria criteria) throws IOException {
        quartzJobService.downloadLog(quartzJobService.queryAllLog(criteria), response);
    }

    @ApiOperation("查询任务执行日志")
    @GetMapping(value = "/logs")
    @PreAuthorize("@el.check('timing:list')")
    public ResponseEntity<Object> getJobLogs(JobQueryCriteria criteria,  SearchVo searchVo, PageVo pageVo){

        Sort s = new Sort(Sort.Direction.DESC, pageVo.getSort());
        if(pageVo!=null&&"asc".equals(pageVo.getOrder())){
            s = new Sort(Sort.Direction.ASC, pageVo.getSort());
        }
        if(searchVo!=null && searchVo.getEndDate()!=null && searchVo.getStartDate()!=null){
            List<Timestamp> createTime  =  new ArrayList<Timestamp>();

            Timestamp t1 = Timestamp.valueOf(searchVo.getStartDate() +" 00:00:00");
            Timestamp t2 = Timestamp.valueOf(searchVo.getEndDate()+" 00:00:00");
            createTime.add(t1);
            createTime.add(t2);
            criteria.setCreateTime(createTime);
        }
        Pageable pageable =  PageRequest.of(pageVo.getPageNumber()-1,pageVo.getPageSize(),s);
        return new ResponseEntity<>(quartzJobService.queryAllLog(criteria,pageable), HttpStatus.OK);
    }

    @Log("新增定时任务")
    @ApiOperation("新增定时任务")
    @PostMapping
    @PreAuthorize("@el.check('timing:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody QuartzJob resources){
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        resources.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return new ResponseEntity<>(quartzJobService.create(resources),HttpStatus.CREATED);
    }

    @Log("修改定时任务")
    @ApiOperation("修改定时任务")
    @PutMapping
    @PreAuthorize("@el.check('timing:edit')")
    public ResponseEntity<Object> update(@Validated(QuartzJob.Update.class) @RequestBody QuartzJob resources){
        //resources.setCreateTime(new Timestamp(System.currentTimeMillis()));
        quartzJobService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("更改定时任务状态")
    @ApiOperation("更改定时任务状态")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@el.check('timing:edit')")
    public ResponseEntity<Object> updateIsPause(@PathVariable Long id){
        quartzJobService.updateIsPause(quartzJobService.findById(id));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("执行定时任务")
    @ApiOperation("执行定时任务")
    @PutMapping(value = "/exec/{id}")
    @PreAuthorize("@el.check('timing:edit')")
    public ResponseEntity<Object> execution(@PathVariable Long id){
        quartzJobService.updateIsPause(quartzJobService.findById(id));
        quartzJobService.execution(quartzJobService.findById(id));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除定时任务")
    @ApiOperation("删除定时任务")
    @DeleteMapping
    @PreAuthorize("@el.check('timing:del')")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids){
        quartzJobService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("删除定时任务")
    @ApiOperation(value = "删除应用")
    @PostMapping(value = "deleteByIds")
    @PreAuthorize("@el.check('app:del')")
    public Result<Object> deleteByIds(@RequestParam(name = "ids[]") Set<Long> ids){
        if (ids == null || ids.size() == 0) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
             quartzJobService.delete(ids);
             return ResultUtil.data("", "删除成功");

        } catch (Exception e) {
            logService.addErrorLog("删除异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("删除异常:" + e.getMessage());
        }
    }
}
