package me.zhengjie.service.gridevents;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.entity.TComponentmanagement;
import me.zhengjie.entity.gridevents.EventsType;

import java.util.List;
import java.util.Map;

public interface IEventsTypeService extends IService<EventsType> {

    /**
     * 新增事件类型
     * @param eventsType
     */
    void add(EventsType eventsType);

    /**
     * 修改事件类型
     * @param eventsType
     */
    void modify(EventsType eventsType);

    /**
     * 查询事件类型
     * @param eventsTypeId
     * @return
     */
    EventsType loadOne(Integer eventsTypeId);

    /**
     * 删除事件类型
     * @param eventsTypeId
     */
    void delete(Integer eventsTypeId);

    /**
     * 根据条件查找事件类型
     * @param query
     * @return
     */
    IPage<EventsType> loadAllByQuery(JSONObject query);

    /**
     * 根据条件查找事件类型
     * @param
     * @return
     */
    List<EventsType> loadByQuery();

    /**
     * 分页查询树级数据
     * @return
     */
    IPage<EventsType> queryEventsTypeTreeByPage(JSONObject query);

    /**
     * 查询全部树级数据
     * @return
     */
    List<EventsType> queryEventsTypeTreeNotPage(JSONObject query);
}
