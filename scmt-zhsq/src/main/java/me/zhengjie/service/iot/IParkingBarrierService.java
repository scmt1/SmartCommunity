package me.zhengjie.service.iot;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.entity.iot.ParkingBarrier;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ly
 * @since 2019-05-06
 */
public interface IParkingBarrierService extends IService<ParkingBarrier> {

    /**
     * 添加停车道闸信息
     * @param parkingBarrier
     */
    void add(ParkingBarrier parkingBarrier);

    /**
     * 删除停车道闸信息
     * @param parkingBarrierId
     */
    void delete(Integer parkingBarrierId);

    /**
     * 修改停车道闸信息
     * @param parkingBarrier
     */
    void modify(ParkingBarrier parkingBarrier);

    /**
     * 根据停车道闸id获取单个停车道闸信息
     * @param parkingBarrierId
     * @return
     */
    ParkingBarrier loadOne(Integer parkingBarrierId);

    /**
     * 根据查询条件获取所有停车道闸信息
     * @param query
     * @return
     */
    IPage<Map<String, Object>> loadAllByQuery(JSONObject query);


}
