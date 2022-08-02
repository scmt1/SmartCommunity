package me.zhengjie.service.iot;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.entity.iot.BuildingIntercom;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ly
 * @since 2019-05-06
 */
public interface IBuildingIntercomService extends IService<BuildingIntercom> {

    /**
     * 添加楼宇对讲信息
     * @param buildingIntercom
     */
    void add(BuildingIntercom buildingIntercom);

    /**
     * 删除楼宇对讲信息
     * @param buildingIntercomId
     */
    void delete(Integer buildingIntercomId);

    /**
     * 修改楼宇对讲信息
     * @param buildingIntercom
     */
    void modify(BuildingIntercom buildingIntercom);

    /**
     * 根据楼宇对讲id获取单个楼宇对讲信息
     * @param buildingIntercomId
     * @return
     */
    BuildingIntercom loadOne(Integer buildingIntercomId);

    /**
     * 根据查询条件获取所有楼宇对讲信息
     * @param query
     * @return
     */
    IPage<Map<String, Object>> loadAllByQuery(JSONObject query);


}
