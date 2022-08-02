package me.zhengjie.service.gridevents;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.entity.gridevents.GridRecord;

import java.util.Map;

public interface IGridRecordService extends IService<GridRecord> {

    /**
     * 新增事件记录
     * @param gridRecord
     */
    void add(GridRecord gridRecord);

    /**
     * 修改事件记录
     * @param gridRecord
     */
    void modify(GridRecord gridRecord);

    /**
     * 查询事件记录
     * @param gridRecordId
     * @return
     */
    GridRecord loadOne(Integer gridRecordId);

    /**
     * 删除事件记录
     * @param gridRecordDetailsId
     */
    void delete(Integer gridRecordDetailsId);

    /**
     * 根据条件查找事件记录
     * @param query
     * @return
     */
    IPage<Map<String, Object>> loadAllByQuery(JSONObject query);
}
