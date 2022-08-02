package me.zhengjie.service.jpush;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.entity.jpush.PushMsg;


import java.util.List;
import java.util.Map;

public interface IPushMsgService extends IService<PushMsg> {

    /**
     * 获取所有消息列表
     * @param param
     * @return
     */
    List<PushMsg> loadPushMsg(JSONObject param);

    /**
     * 根据条件获取所有未读数量
     * @param param
     * @return
     */
    Integer loadUnreadCount(JSONObject param);

    /**
     * 设置已读状态
     * @param param
     */
    void setReadStatus(JSONObject param);

    /**
     * 获取后台消息列表
     * @param param
     */
    List<Map<String,Object>> loadMsgInWeb(JSONObject param);

    /**
     * 新增消息
     * @param pushMsg
     */
    Long add(PushMsg pushMsg) throws IllegalAccessException;

    /**
     * 门口机呼叫
     * @param param
     */
    void sendMsgInCall(JSONObject param);
}
