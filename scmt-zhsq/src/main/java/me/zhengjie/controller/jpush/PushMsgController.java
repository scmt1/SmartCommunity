package me.zhengjie.controller.jpush;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;


import com.baomidou.mybatisplus.extension.api.R;
import me.zhengjie.common.utils.ResultCode;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.Result;
import me.zhengjie.entity.jpush.PushMsg;
import me.zhengjie.service.jpush.IPushMsgService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/push")
@AllArgsConstructor
@Slf4j
public class PushMsgController {

    private final IPushMsgService pushMsgService;

    /**
     * 新增消息推送
     * @param pushMsg
     * @return
     */
    @PostMapping("/add")
    public Result<Object> add(@RequestBody PushMsg pushMsg) throws IllegalAccessException {
        Long add = pushMsgService.add(pushMsg);
        return ResultUtil.data(add);
    }

    /**
     * 获取未读数量
     * @param param
     * @return
     */
    @PostMapping("/loadUnreadCount")
    public Result<Object> loadUnreadCount(@RequestBody JSONObject param){
        Integer results = pushMsgService.loadUnreadCount(param);
        return ResultUtil.data(results);
    }

    /**
     * 设置所有已读
     * @param param
     * @return
     */
    @PostMapping("/setReadStatus")
    public Result<Object> setReadStatus(@RequestBody JSONObject param){
        pushMsgService.setReadStatus(param);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 获取所有消息列表
     * @param param
     * @return
     */
    @PostMapping("/loadPushMsg")
    public Result<Object> loadAllByLevel(@RequestBody JSONObject param){
        List<PushMsg> results = pushMsgService.loadPushMsg(param);
        return ResultUtil.data(results);
    }

    /**
     * 获取后台消息
     * @param param
     * @return
     */
    @PostMapping("/loadMsgInWeb")
    public Result<Object> loadMsgInWeb(@RequestBody JSONObject param){
        List<Map<String,Object>> results = pushMsgService.loadMsgInWeb(param);
        return ResultUtil.data(results);
    }

    /**
     * 门口机呼叫
     * @param param
     * @return
     */
    @PostMapping("/sendMsgInCall")
    public Result<Object> sendMsgInCall(@RequestBody JSONObject param){
        pushMsgService.sendMsgInCall(param);
        return ResultUtil.success(ResultCode.SUCCESS);
    }


}
