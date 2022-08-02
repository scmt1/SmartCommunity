package me.zhengjie.controller.gridevents;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;

import me.zhengjie.common.utils.ResultCode;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.Result;
import me.zhengjie.entity.gridevents.GridHandle;
import me.zhengjie.service.gridevents.IGridHandleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/handle")
@AllArgsConstructor
@Slf4j
public class GridHandleController {

    private final IGridHandleService gridHandleService;

    /**
     * 新增事件处理
     *
     * @param gridHandle
     * @return
     */
    @PostMapping("/add")
    public Result<Object> add(@RequestBody GridHandle gridHandle) {
        gridHandleService.add(gridHandle);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 修改事件处理
     *
     * @param gridHandle
     * @return
     * @throws Exception
     */
    @PostMapping("/modify")
    public Result<Object> modify(GridHandle gridHandle) throws Exception {
        gridHandleService.modify(gridHandle);
        return ResultUtil.success(ResultCode.SUCCESS);
    }


    /**
     * 获取单个事件处理
     *
     * @param gridHandleId
     * @return
     */
    @GetMapping("/loadOne")
    public Result<Object> loadOne(Integer gridHandleId) {
        GridHandle gridHandle = gridHandleService.loadOne(gridHandleId);
        return ResultUtil.data(gridHandle);
    }

    /**
     * 删除事件处理
     *
     * @param GridHandleDetailsId
     * @return
     */
    @GetMapping("/delete")
    public Result<Object> delete(Integer GridHandleDetailsId) {
        gridHandleService.delete(GridHandleDetailsId);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 获取所有任务详情
     *
     * @param query
     * @return
     */
    @PostMapping("/loadAllByQuery")
    public Result<Object> loadAllByQuery(@RequestBody JSONObject query) {
        IPage<Map<String, Object>> GridHandles = gridHandleService.loadAllByQuery(query);
        return ResultUtil.data(GridHandles);
    }

    /**
     * 事件属于判断
     *
     * @param query
     * @return
     */
    @PostMapping("/isAuthenticity")
    public Result<Object> isAuthenticity(@RequestBody JSONObject query) {
        gridHandleService.isAuthenticity(query);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 不属实审核
     *
     * @param query
     * @return
     */
    @PostMapping("/beVerified")
    public Result<Object> beVerified(@RequestBody JSONObject query) {
        gridHandleService.beVerified(query);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 处理操作
     *
     * @param query
     * @return
     */
    @PostMapping("/processing")
    public Result<Object> processing(@RequestBody JSONObject query) {
        gridHandleService.processing(query);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 管理员派单接口
     *
     * @param query
     * @return
     */
    @PostMapping("/distribute")
    public Result<Object> distribute(@RequestBody JSONObject query) {
        gridHandleService.distribute(query);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 转单审核
     *
     * @param query
     * @return
     */
    @PostMapping("/transferExamine")
    public Result<Object> transferExamine(@RequestBody JSONObject query) {
        gridHandleService.transferExamine(query);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 分阶上报
     *
     * @param query
     * @return
     */
    @PostMapping("/hierarchical")
    public Result<Object> hierarchical(@RequestBody JSONObject query) {
        gridHandleService.hierarchical(query);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 审核分阶上报
     *
     * @param query
     * @return
     */
    @PostMapping("/examineHierarchical")
    public Result<Object> examineHierarchical(@RequestBody JSONObject query) {
        gridHandleService.examineHierarchical(query);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 正常审核
     *
     * @param query
     * @return
     */
    @PostMapping("/examine")
    public Result<Object> examine(@RequestBody JSONObject query) {
        gridHandleService.examine(query);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 居民审核
     *
     * @param query
     * @return
     */
    @PostMapping("/residentAudit")
    public Result<Object> residentAudit(@RequestBody JSONObject query) {
        gridHandleService.residentAudit(query);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 异议审核
     *
     * @param query
     * @return
     */
    @PostMapping("/objectionReview")
    public Result<Object> objectionReview(@RequestBody JSONObject query) {
        gridHandleService.objectionReview(query);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 修改事件分类
     *
     * @param query
     * @return
     */
    @PostMapping("/changeEventsType")
    public Result<Object> changeEventsType(@RequestBody JSONObject query) {
        gridHandleService.changeEventsType(query);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 结束事件
     *
     * @param query
     * @return
     */
    @PostMapping("/endEvents")
    public Result<Object> endEvents(@RequestBody JSONObject query) {
        gridHandleService.endEvents(query);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 获取用户网格
     *
     * @param
     * @return
     */
    @GetMapping("/getUserGrid")
    public Result<Object> getUserGrid() {
        List<Map<String, Object>> list = gridHandleService.getUserGrid();
        return ResultUtil.data(list);
    }
}
