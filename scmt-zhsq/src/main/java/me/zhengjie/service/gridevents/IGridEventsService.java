package me.zhengjie.service.gridevents;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.GridStatistics;
import me.zhengjie.entity.gridevents.GridEvents;

import java.util.List;
import java.util.Map;

public interface IGridEventsService extends IService<GridEvents> {

    /**
     * 新增网格事件
     * @param gridEvents
     */
    void add(GridEvents gridEvents) throws  Exception;

    /**
     * 修改网格事件
     * @param gridEvents
     */
    void modify(GridEvents gridEvents);

    /**
     * 查询网格事件
     * @param gridEventsId
     * @return
     */
    GridEvents loadOne(Integer gridEventsId);

    /**
     * 查询网格事件 (含事件类型实体)
     * @param gridEventsId
     * @return
     */
    GridEvents getOne(Integer gridEventsId);

    /**
     * 删除网格事件
     * @param gridEventsId
     */
    void delete(Integer gridEventsId);

    /**
     * 根据条件查找网格事件
     * @param query
     * @return
     */
    IPage<GridEvents> loadAllByQuery(JSONObject query);

    /**
     * 获取所有的状态
     * @param query
     * @return
     */
    List<Map<String, Object>> getAllStatus(JSONObject query);

    /**
     * 获取网格事件的详情
     * @param gridEventsId
     * @return
     */
    GridEvents getOrderDetails(Long gridEventsId);

    /**
     * 统计
     * @param
     * @return
     */
    List<Map<String, Object>> getStatistics(JSONObject query);

    /**
     * 统计事件 全部 待处理 已办结 已超时
     * @return
     */
    GridEvents statisticsEvent(SearchVo searchVo,JSONObject query);

    /**
     * 根据条件(事件类型) 统计对应的事件数量
     * @return
     */
    List<GridEvents> echartEvent(SearchVo searchVo,JSONObject query);

    /**
     * 网格辖区概况 统计
     * @return
     */
    GridStatistics gridStatistics(SearchVo searchVo,JSONObject query);

    /**
     * 获取事件数据
     * @param
     * @return
     */
    List<GridEvents> loadByQuery(JSONObject query);

    /**
     * 数据大屏展示
     * @param
     * @return
     */
    Map<String, Object> getBigData(JSONObject query);
    /**
     * 统计
     * @param
     * @return
     */
    List<Map<String, Object>> getStatisticsPage();
}
