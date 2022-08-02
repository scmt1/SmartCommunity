package me.zhengjie.modules.mnt.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.aop.log.Log;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.modules.mnt.domain.App;
import me.zhengjie.modules.mnt.service.AppService;
import me.zhengjie.modules.mnt.service.dto.AppQueryCriteria;
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
import java.util.List;
import java.util.Set;

/**
* @author zhanghouying
* @date 2019-08-24
*/
@Api(tags = "应用管理")
@RestController
@RequestMapping("/api/app")
public class AppController {

    private final AppService appService;

    public AppController(AppService appService){
        this.appService = appService;
    }

    @Log("导出应用数据")
    @ApiOperation("导出应用数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('app:list')")
    public void download(HttpServletResponse response, AppQueryCriteria criteria) throws IOException {
        appService.download(appService.queryAll(criteria), response);
    }

    @Log("查询应用")
    @ApiOperation(value = "查询应用")
    @GetMapping(value = "queryAppList")
	@PreAuthorize("@el.check('app:list')")
    public ResponseEntity<Object> queryAppList(AppQueryCriteria criteria, SearchVo searchVo, PageVo pageVo){
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
        return new ResponseEntity<>(appService.queryAll(criteria,pageable),HttpStatus.OK);
    }
    @Log("查询应用")
    @ApiOperation(value = "查询应用")
    @GetMapping(value = "getAppById")
    @PreAuthorize("@el.check('app:list')")
    public ResponseEntity<Object> getAppById(Long id){

        return new ResponseEntity<>(appService.findById(id),HttpStatus.OK);
    }

    @Log("新增应用")
    @ApiOperation(value = "新增应用")
    @PostMapping
	@PreAuthorize("@el.check('app:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody App resources){
        resources.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return new ResponseEntity<>(appService.create(resources),HttpStatus.CREATED);
    }

    @Log("修改应用")
    @ApiOperation(value = "修改应用")
    @PutMapping
	@PreAuthorize("@el.check('app:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody App resources){
        appService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除应用")
    @ApiOperation(value = "删除应用")
	@DeleteMapping
	@PreAuthorize("@el.check('app:del')")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids){
        appService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Log("删除应用")
    @ApiOperation(value = "删除应用")
    @PostMapping(value = "deleteByIds")
    @PreAuthorize("@el.check('app:del')")
    public ResponseEntity<Object> deleteByIds(@RequestParam(name = "ids[]") Set<Long> ids){
        appService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
