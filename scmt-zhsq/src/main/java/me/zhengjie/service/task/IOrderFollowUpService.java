package me.zhengjie.service.task;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.entity.task.OrderFollowUp;


import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ljj
 * @since 2019-05-06
 */
public interface IOrderFollowUpService extends IService<OrderFollowUp> {

    /**
     * 添加工单跟进信息
     * @param orderFollowUp
     */
    void add(OrderFollowUp orderFollowUp);

    /**
     * 删除工单跟进信息
     * @param orderFollowUpId
     */
    void delete(Integer orderFollowUpId);

    /**
     * 修改工单跟进信息
     * @param orderFollowUp
     */
    void modify(OrderFollowUp orderFollowUp);

    /**
     * 根据工单跟进id获取单个工单跟进信息
     * @param orderFollowUpId
     * @return
     */
    OrderFollowUp loadOne(Integer orderFollowUpId);

    /**
     * 根据查询条件获取所有工单跟进信息
     * @param query
     * @return
     */
    List<OrderFollowUp> loadByQuery(JSONObject query);

}
