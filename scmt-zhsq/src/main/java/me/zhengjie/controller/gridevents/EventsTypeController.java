package me.zhengjie.controller.gridevents;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;

import io.swagger.annotations.ApiOperation;
import me.zhengjie.aop.log.Log;
import me.zhengjie.common.utils.ResultCode;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.Result;
import me.zhengjie.entity.TComponentmanagement;
import me.zhengjie.entity.gridevents.EventsType;
import me.zhengjie.service.gridevents.IEventsTypeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/events/type")
@AllArgsConstructor
public class EventsTypeController {

    private final IEventsTypeService eventsTypeService;

    /**
     * 新增事件类型
     *
     * @param eventsType
     * @return
     */
    @PostMapping("/add")
    public Result<Object> add(@RequestBody EventsType eventsType) {
        eventsTypeService.add(eventsType);

        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 修改事件类型
     *
     * @param eventsType
     * @return
     * @throws Exception
     */
    @PostMapping("/modify")
    public Result<Object> modify(@RequestBody EventsType eventsType) throws Exception {
        eventsTypeService.modify(eventsType);
        return ResultUtil.success(ResultCode.SUCCESS);
    }


    /**
     * 获取单个事件类型
     *
     * @param eventsTypeId
     * @return
     */
    @GetMapping("/loadOne")
    public Result<Object> loadOne(Integer eventsTypeId) {
        EventsType eventsType = eventsTypeService.loadOne(eventsTypeId);
        return ResultUtil.data(eventsType);
    }
    
    /**
     * 删除事件类型
     *
     * @param eventsTypeId
     * @return
     */
    @GetMapping("/delete")
    public Result<Object> delete(Integer eventsTypeId) {
        eventsTypeService.delete(eventsTypeId);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 获取所有事件类型
     *
     * @param query
     * @return
     */
    @PostMapping("/loadAllByQuery")
    public Result<Object> loadAllByQuery(@RequestBody JSONObject query) {
        IPage<EventsType> EventsTypes = eventsTypeService.loadAllByQuery(query);
        return ResultUtil.data(EventsTypes);
    }

    /**
     * 获取所有事件类型
     *
     * @param
     * @return
     */
    @GetMapping("/loadByQuery")
    public Result<Object> loadByQuery() {
        List<EventsType> EventsTypes = eventsTypeService.loadByQuery();
        return ResultUtil.data(EventsTypes);
    }

    /**
     * 分页查询事件类型（树级）
     * @return
     */
    @Log("分页查询事件类型")
    @ApiOperation("分页查询事件类型")
    @PostMapping("/queryEventsTypeTreeByPage")
    public Result<Object> queryEventsTypeTreeByPage(@RequestBody JSONObject params){
        IPage<EventsType> eventsTypeIPage = eventsTypeService.queryEventsTypeTreeByPage(params);
        return ResultUtil.data(eventsTypeIPage);
    }

    /**
     * 查询事件类型 不分页（树级）
     * @return
     */
    @Log("不分页查询事件类型")
    @ApiOperation("不分页查询事件类型")
    @PostMapping("/queryEventsTypeTreeNotPage")
    public Result<Object> queryEventsTypeTreeNotPage(@RequestBody JSONObject params){
        List<EventsType> eventsTypeIPage = eventsTypeService.queryEventsTypeTreeNotPage(params);
        return ResultUtil.data(eventsTypeIPage);
    }
}
