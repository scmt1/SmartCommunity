package me.zhengjie.modules.system.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletResponse;

import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.aop.log.Log;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.config.DataScope;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.quartz.service.QuartzJobService;
import me.zhengjie.system.domain.Job;
import me.zhengjie.system.service.JobService;
import me.zhengjie.system.service.dto.JobQueryCriteria;
import me.zhengjie.utils.ThrowableUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
* @author Zheng Jie
* @date 2019-03-29
*/
@Api(tags = "系统：岗位管理")
@RestController
@RequestMapping("/api/job")
public class JobController {

    @Autowired
    private QuartzJobService quartzJobService;

    private final JobService jobService;

    private final DataScope dataScope;

    private static final String ENTITY_NAME = "job";

    public JobController(JobService jobService, DataScope dataScope) {
        this.jobService = jobService;
        this.dataScope = dataScope;
    }

    @Log("导出岗位数据")
    @ApiOperation("导出岗位数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('job:list')")
    public void download(HttpServletResponse response, JobQueryCriteria criteria) throws IOException {
        jobService.download(jobService.queryAll(criteria), response);
    }

    @Log("查询岗位")
    @ApiOperation("查询岗位")
    @GetMapping
//    @PreAuthorize("@el.check('job:list','user:list')")
    @AnonymousAccess
    public ResponseEntity<Object> getJobs(JobQueryCriteria criteria, Pageable pageable){
        // 数据权限
        criteria.setDeptIds(dataScope.getDeptIds());
        return new ResponseEntity<>(jobService.queryAll(criteria, pageable),HttpStatus.OK);
    }

    @Log("查询岗位")
    @ApiOperation("查询岗位")
    @GetMapping(value = "getJobById")
    @PreAuthorize("@el.check('job:list','user:list')")
    @AnonymousAccess
    public ResponseEntity<Object> getJobById(Long id){
        return new ResponseEntity<>(jobService.findById(id),HttpStatus.OK);
    }

    @Log("查询岗位")
    @ApiOperation("查询岗位")
    @GetMapping(value = "queryJobList")
    public ResponseEntity<Object> queryJobList(JobQueryCriteria criteria, SearchVo searchVo, PageVo pageVo){

        Sort s = new Sort(Sort.Direction.DESC, "id");
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
        // 数据权限
        criteria.setDeptIds(dataScope.getDeptIds());
        return new ResponseEntity<>(jobService.queryAll(criteria, pageable),HttpStatus.OK);
    }


    @Log("新增岗位")
    @ApiOperation("新增岗位")
    @PostMapping
    @PreAuthorize("@el.check('job:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Job resources){
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        resources.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return new ResponseEntity<>(jobService.create(resources),HttpStatus.CREATED);
    }

    @Log("修改岗位")
    @ApiOperation("修改岗位")
    @PutMapping
    @PreAuthorize("@el.check('job:edit')")
    public ResponseEntity<Object> update(@Validated(Job.Update.class) @RequestBody Job resources){
        jobService.update(resources);
        return new ResponseEntity<>(resources,HttpStatus.NO_CONTENT);
    }

    @Log("删除岗位")
    @ApiOperation("删除岗位")
    @DeleteMapping
    @PreAuthorize("@el.check('job:del')")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids){
        try {
            jobService.delete(ids);
        }catch (Throwable e){
            ThrowableUtil.throwForeignKeyException(e, "所选岗位存在用户关联，请取消关联后再试");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @Log("删除岗位")
    @ApiOperation("删除岗位")
    @PostMapping(value = "deleteByIds")
    @PreAuthorize("@el.check('job:del')")
    public ResponseEntity<Object> deleteByIds(@RequestParam(name = "ids[]") Set<Long> ids){
        try {
            jobService.delete(ids);
        }catch (Throwable e){
            ThrowableUtil.throwForeignKeyException(e, "所选岗位存在用户关联，请取消关联后再试");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
    /**
     * 查询可用银行
     */
    @GetMapping(value = "/bank")
    public List<Map<String, Object>> getBank(){
        return quartzJobService.getBank();
    }
}
