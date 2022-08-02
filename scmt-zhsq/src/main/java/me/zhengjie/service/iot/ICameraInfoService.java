package me.zhengjie.service.iot;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.entity.iot.CameraInfo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ly
 * @since 2019-05-06
 */
public interface ICameraInfoService extends IService<CameraInfo> {

    /**
     * 添加摄像头信息
     * @param cameraInfo
     */
    void add(CameraInfo cameraInfo);

    /**
     * 删除摄像头信息
     * @param cameraInfoId
     */
    void delete(Integer cameraInfoId);

    /**
     * 修改摄像头信息
     * @param cameraInfo
     */
    void modify(CameraInfo cameraInfo);

    /**
     * 根据摄像头id获取单个摄像头信息
     * @param cameraInfoId
     * @return
     */
    CameraInfo loadOne(Integer cameraInfoId);

    /**
     * 根据查询条件获取所有摄像头信息
     * @param query
     * @return
     */
    IPage<CameraInfo> loadAllByQuery(JSONObject query);


}
