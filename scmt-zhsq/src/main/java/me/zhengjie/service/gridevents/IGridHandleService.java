package me.zhengjie.service.gridevents;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.entity.gridevents.GridHandle;

import java.util.List;
import java.util.Map;

public interface IGridHandleService extends IService<GridHandle> {

    /**
     * 新增事件处理
     * @param gridHandle
     */
    void add(GridHandle gridHandle);

    /**
     * 修改事件处理
     * @param gridHandle
     */
    void modify(GridHandle gridHandle);

    /**
     * 查询事件处理
     * @param gridHandleId
     * @return
     */
    GridHandle loadOne(Integer gridHandleId);

    /**
     * 删除事件处理
     * @param gridHandleDetailsId
     */
    void delete(Integer gridHandleDetailsId);

    /**
     * 根据条件查找事件处理
     * @param query
     * @return
     */
    IPage<Map<String, Object>> loadAllByQuery(JSONObject query);

    /**
     * 网格事件属实性判断
     * @param query
     * @return
     */
    void isAuthenticity(JSONObject query);

    /**
     * 网格事件处理操作
     * @param query
     * @return
     */
    void processing(JSONObject query);

    /**
     * 转单审核
     * @param query
     * @return
     */
    void transferExamine(JSONObject query);

    /**
     * 正常审核
     * @param query
     * @return
     */
    void examine(JSONObject query);

    /**
     * 居民审核
     * @param query
     * @return
     */
    void residentAudit(JSONObject query);

    /**
     * 异议审核
     * @param query
     * @return
     */
    void objectionReview(JSONObject query);

    /**
     * 修改事件分类
     * @param query
     * @return
     */
    void changeEventsType(JSONObject query);

    /**
     * 网格长派单
     * @param query
     * @return
     */
    void distribute(JSONObject query);

    /**
     * 不属实审核
     * @param query
     * @return
     */
    void beVerified(JSONObject query);

    /**
     * 结束事件
     * @param query
     */
    void endEvents(JSONObject query);

    /**
     * 分阶上报
     * @param query
     */
    void hierarchical(JSONObject query);

    /**
     * 审核分阶上报
     * @param query
     */
    void examineHierarchical(JSONObject query);

    /**
     * 获取用户的网格
     * @param
     */
    List<Map<String,Object>> getUserGrid();
}
