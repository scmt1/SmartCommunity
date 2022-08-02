package me.zhengjie.util;

import me.zhengjie.utils.StringUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;


/**
 * @author yzy
 */

@Component
@ServerEndpoint(value = "/kungfupeng/websocket")
public class CustomWebSocket {
    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static int onlineCount = 0;
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的CumWebSocket对象。
     */
    private static CopyOnWriteArraySet<Map<String, CustomWebSocket>> webSocketSet = new CopyOnWriteArraySet();
    private static Map<String, CustomWebSocket> clients = new ConcurrentHashMap();
    private static JSONObject online=new JSONObject();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    private String shopId;


    /**
     * 连接建立成功调用的方法
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        String queryString = session.getQueryString();
        if(StringUtils.isNotEmpty(queryString)) {
            String[] split = queryString.split("=");
            this.session = session;
            this.shopId = split[1];
            //加入set中
            clients.put(shopId, this);
            online.put(shopId,"");
            webSocketSet.add(clients);
            //添加在线人数
            addOnlineCount();
            System.out.println("新连接接入。当前在线人数为：" + getOnlineCount());
            sendAll(online.toString());
        }
    }
    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        String queryString = session.getQueryString();
        if(StringUtils.isNotEmpty(queryString)) {
            String[] split = queryString.split("=");
            this.shopId = split[1];
            online.remove(shopId);
            sendAll(online.toString());
        }
        //从set中删除
        webSocketSet.remove(this);
        //在线数减1
        subOnlineCount();
        System.out.println("有连接关闭。当前在线人数为：" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用
     *
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        //转换成为JSONObject对象
        JSONObject jsonObj = new JSONObject(message);
        System.out.println("客户端发送的消息：" + message);
        String to = jsonObj.getString("name"); //message.split(",")[0];
        //System.out.println("给客户端发送id：" + to);
        for (CustomWebSocket item : clients.values()) {
            if (item.shopId.equals(to)) {
                item.session.getAsyncRemote().sendText(message);
            }
        }
    }

    /**
     * 暴露给外部的群发
     *
     * @param message
     * @throws IOException
     */
    public static void sendInfo(String message, String to) throws IOException {
        for (CustomWebSocket item : clients.values()) {
            if (item.shopId.equals(to)) {
                item.session.getAsyncRemote().sendText(message);
            }
        }
    }

    /**
     * 群发
     *
     * @param message
     * @throws IOException
     */
    private static void sendAll(String message){
        for (CustomWebSocket item : clients.values()) {
            try {
                item.session.getAsyncRemote().sendText(message);
            }catch (Exception e) {
                //打印输出异常
                e.printStackTrace();
            }

        }
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("----websocket-------有异常啦");
        error.printStackTrace();
    }

    /**
     * 减少在线人数
     */
    private void subOnlineCount() {
        CustomWebSocket.onlineCount--;
    }

    /**
     * 添加在线人数
     */
    private void addOnlineCount() {
        CustomWebSocket.onlineCount++;

    }

    /**
     * 当前在线人数
     *
     * @return
     */
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    /**
     * 发送信息
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        //获取session远程基本连接发送文本消息
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }
}
