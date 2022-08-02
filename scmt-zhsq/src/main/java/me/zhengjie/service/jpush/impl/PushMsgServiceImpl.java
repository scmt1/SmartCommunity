package me.zhengjie.service.jpush.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import me.zhengjie.entity.jpush.PushMsg;
import me.zhengjie.mapper.jpush.PushMsgMapper;

import lombok.AllArgsConstructor;
import me.zhengjie.service.jpush.IPushMsgService;
import me.zhengjie.util.BusinessErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@AllArgsConstructor
public class PushMsgServiceImpl extends ServiceImpl<PushMsgMapper, PushMsg> implements IPushMsgService {

    private final PushMsgMapper pushMsgMapper;

//    private final JpushService jpushService;


    @Override
    public List<PushMsg> loadPushMsg(JSONObject param) {
        List<PushMsg> pushMsgs = pushMsgMapper.loadPushMsg(param);
        return pushMsgs;
    }

    @Override
    public Integer loadUnreadCount(JSONObject param) {
        return pushMsgMapper.loadUnreadCount(param);
    }

    @Override
    public void setReadStatus(JSONObject param) {
        pushMsgMapper.setReadStatus(param);
    }

    @Override
    public List<Map<String, Object>> loadMsgInWeb(JSONObject param) {
        List<Map<String, Object>> result = pushMsgMapper.loadMsgInWeb(param);
        return result;
    }

    @Override
    public Long add(PushMsg pushMsg) throws IllegalAccessException {
        pushMsg.setCreateDate(new Date());
        pushMsg.setIsRead(1);
        validator(pushMsg);
        pushMsgMapper.insert(pushMsg);
        return pushMsg.getId();
    }

    private void validator(PushMsg pushMsg) {
    }

    @Override
    public void sendMsgInCall(JSONObject param) {
        if (StringUtils.isEmpty(param.getString("personUuid")) || StringUtils.isEmpty(param.getString("housingUuid"))) {
            throw new BusinessErrorException("参数异常");
        }

        String housingInfo = pushMsgMapper.getBasicHousingInfo(param.getString("housingUuid"));
        Map<String, Object> personUuid = pushMsgMapper.getUserPhone(param.getString("personUuid"));
        if (!personUuid.isEmpty()) {
            Map<String, String> map = new HashMap<>();
            map.put("code", "");
            map.put("messageType", "0");
//            jpushService.sendCustomPush(1, "门口机呼叫", "您所在的" + housingInfo + "有远程呼叫，请打开App接收视频呼叫", map, personUuid.get("phone").toString());
            PushMsg pushMsg = new PushMsg();
            pushMsg.setCreateDate(new Date());
            pushMsg.setAppType(1);
            pushMsg.setUserIds(personUuid.get("id").toString());
            pushMsg.setContent("您所在的" + housingInfo + "有远程呼叫，请打开App接收视频呼叫");
            pushMsg.setMessageType(0);        //门口机呼叫
            pushMsg.setTitle("门口机呼叫");
            pushMsg.setIsRead(1);
            pushMsgMapper.insert(pushMsg);
        }
    }
}
