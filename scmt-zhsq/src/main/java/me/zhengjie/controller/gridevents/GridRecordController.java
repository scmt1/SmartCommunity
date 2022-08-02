package me.zhengjie.controller.gridevents;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;

import me.zhengjie.aop.annotation.InitBaseInfo;
import me.zhengjie.aop.type.InitBaseType;
import me.zhengjie.common.utils.ResultCode;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.Result;
import me.zhengjie.entity.gridevents.GridRecord;
import me.zhengjie.service.gridevents.IGridRecordService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/record")
@Slf4j
@AllArgsConstructor
public class GridRecordController {

    private final IGridRecordService gridRecordService;

    /**
     * 新增事件记录
     *
     * @param gridRecord
     * @return
     */
    @PostMapping("/add")
    @InitBaseInfo(type = InitBaseType.ADD)
    public Result<Object> add(@RequestBody GridRecord gridRecord) {
        gridRecordService.add(gridRecord);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 修改事件记录
     *
     * @param gridRecord
     * @return
     * @throws Exception
     */
    @PostMapping("/modify")
    @InitBaseInfo(type = InitBaseType.UPDATE)
    public Result<Object> modify(GridRecord gridRecord) throws Exception {
        gridRecordService.modify(gridRecord);
        return ResultUtil.success(ResultCode.SUCCESS);
    }


    /**
     * 获取单个事件记录
     *
     * @param gridRecordId
     * @return
     */
    @GetMapping("/loadOne")
    public Result<Object> loadOne(Integer gridRecordId) {
        GridRecord gridRecord = gridRecordService.loadOne(gridRecordId);
        return ResultUtil.data(gridRecord);
    }
    
    /**
     * 删除事件记录
     *
     * @param GridRecordDetailsId
     * @return
     */
    @GetMapping("/delete")
    public Result<Object> delete(Integer GridRecordDetailsId) {
        gridRecordService.delete(GridRecordDetailsId);
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
        IPage<Map<String, Object>> GridRecords = gridRecordService.loadAllByQuery(query);
        return ResultUtil.data(GridRecords);
    }
}
