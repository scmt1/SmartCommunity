package me.zhengjie.modules.system.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.*;

import me.zhengjie.aop.log.Log;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.Result;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.system.domain.DictDetail;
import me.zhengjie.system.domain.Menu;
import me.zhengjie.system.service.DictDetailService;
import me.zhengjie.system.service.dto.DictDetailDto;
import me.zhengjie.system.service.dto.DictDetailQueryCriteria;

import me.zhengjie.utils.StringUtils;
import me.zhengjie.utils.ThrowableUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
* @author Zheng Jie
* @date 2019-04-10
*/
@RestController
@Api(tags = "系统：字典详情管理")
@RequestMapping("/api/dictDetail")
public class DictDetailController {

    private final DictDetailService dictDetailService;

    private static final String ENTITY_NAME = "dictDetail";

    public DictDetailController(DictDetailService dictDetailService) {
        this.dictDetailService = dictDetailService;
    }

    @Log("查询字典详情")
    @ApiOperation("查询字典详情")
    @GetMapping(value="/all")
    public ResponseEntity<Object> getDictDetails(DictDetailQueryCriteria criteria,  @PageableDefault(sort = {"sort"}, direction = Sort.Direction.ASC) Pageable pageable){
        return new ResponseEntity<>(dictDetailService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @Log("查询字典详情")
    @ApiOperation("查询字典详情")
    @GetMapping(value = "/{type}")
    public Result<List<DictDetailDto>> getDictDetailsByType(@PathVariable String type){
    	DictDetailQueryCriteria criteria=new DictDetailQueryCriteria();
    	criteria.setDictName(type);
    	List<DictDetailDto> list=dictDetailService.queryAll(criteria);
    	return new ResultUtil<List<DictDetailDto>>().setData(list);
    	//return new ResponseEntity<>(dictDetailService.queryAll(criteria),HttpStatus.OK);
    }

    @Log("查询多个字典详情")
    @ApiOperation("查询多个字典详情")
    @GetMapping(value = "/map")
    public ResponseEntity<Object> getDictDetailMaps(DictDetailQueryCriteria criteria, @PageableDefault(sort = {"sort"}, direction = Sort.Direction.ASC) Pageable pageable){
        String[] names = criteria.getDictName().split(",");
        Map<String,Object> map = new HashMap<>(names.length);
        for (String name : names) {
            criteria.setDictName(name);
            map.put(name,dictDetailService.queryAll(criteria,pageable).get("content"));
        }
        return new ResponseEntity<>(map,HttpStatus.OK);
    }

    @Log("新增字典详情")
    @ApiOperation("新增字典详情")
    @PostMapping
    @PreAuthorize("@el.check('dict:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody DictDetail resources){
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        return new ResponseEntity<>(dictDetailService.create(resources),HttpStatus.CREATED);
    }

    @Log("修改字典详情")
    @ApiOperation("修改字典详情")
    @PutMapping
    @PreAuthorize("@el.check('dict:edit')")
    public ResponseEntity<Object> update(@Validated(DictDetail.Update.class) @RequestBody DictDetail resources){
        dictDetailService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除字典详情")
    @ApiOperation("删除字典详情")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('dict:del')")
    public ResponseEntity<Object> delete(@PathVariable Long id){
        dictDetailService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Log("批量删除字典详情")
    @ApiOperation("批量删除字典详情")
    @PostMapping("/delete")
    @PreAuthorize("@el.check('dict:del')")
    public Result<Object> delete(@RequestParam(name = "ids[]")Long[] ids){
        if(ids != null && ids.length>0){
            try {
                for(Long id :ids){
                    dictDetailService.delete(id);
                }
            }catch (Throwable e){
                ThrowableUtil.throwForeignKeyException(e, "删除操作失败！");
            }
            return new ResultUtil<>().setSuccessMsg("删除成功！");
        }else{
            return new ResultUtil<>().setSuccessMsg("删除失败！");
        }
    }


    @Log("查询字典详情")
    @ApiOperation("查询字典详情")
    @GetMapping(value = "/loadByType")
    public Result<Object> loadByType(@RequestParam(name = "fieldName") String fieldName){
        DictDetailQueryCriteria criteria=new DictDetailQueryCriteria();
        criteria.setDictName(fieldName);
        List<DictDetailDto> list=dictDetailService.queryAll(criteria);
        List<Map<String,Object>> reslist = new ArrayList<>();
        for(DictDetailDto dto:list){
            if(dto != null){
                Map<String,Object> map =new HashMap<>();
                map.put("id",dto.getId());
                map.put("name",dto.getLabel());
                map.put("number",dto.getValue());
                map.put("sort",dto.getSort());
                map.put("createTime",dto.getCreateTime());
                reslist.add(map);
            }
        }
        return new ResultUtil<>().setData(reslist);
        //return new ResponseEntity<>(dictDetailService.queryAll(criteria),HttpStatus.OK);
    }
}
