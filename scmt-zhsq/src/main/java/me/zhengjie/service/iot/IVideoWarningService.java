package me.zhengjie.service.iot;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.entity.iot.VideoWarning;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ly
 * @since 2019-05-06
 */
public interface IVideoWarningService extends IService<VideoWarning> {

    /**
     * 添加视频预警信息
     * @param videoWarning
     */
    void add(VideoWarning videoWarning);

    /**
     * 删除视频预警信息
     * @param videoWarningId
     */
    void delete(Integer videoWarningId);

    /**
     * 修改视频预警信息
     * @param videoWarning
     */
    void modify(VideoWarning videoWarning);

    /**
     * 根据视频预警id获取单个视频预警信息
     * @param videoWarningId
     * @return
     */
    VideoWarning loadOne(Integer videoWarningId);

    /**
     * 根据查询条件获取所有视频预警信息
     * @param query
     * @return
     */
    IPage<VideoWarning> loadAllByQuery(JSONObject query);


}
