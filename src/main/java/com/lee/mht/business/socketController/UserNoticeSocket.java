package com.lee.mht.business.socketController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author FucXing
 * @date 2022/02/27 00:05
 **/
@ServerEndpoint("/userNoticeSocket/{id}")
@Slf4j
@Component
public class UserNoticeSocket {

    private static int onlineCount = 0;
    private static Map<Integer, UserNoticeSocket> clients = new ConcurrentHashMap<Integer, UserNoticeSocket>();
    private Session session;
    private Integer id;

    @OnOpen
    public void onOpen(@PathParam("id") Integer id, Session session) {
        this.id = id;
        this.session = session;
        UserNoticeSocket.onlineCount++;
        clients.put(id, this);
        log.info("{}登录进来了", id);
    }

    @OnClose
    public void onClose() {
        clients.remove(id);
        log.info("{}登出了",id);
        UserNoticeSocket.onlineCount--;
    }

    @OnMessage
    public void onMessage(String message) {

    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("WebSocket发生错误：" + throwable.getMessage());
    }
    public static void sendMessageToAll(String message) {
        // 向所有连接websocket的客户端发送消息
        // 可以修改为对某个客户端发消息
        for (UserNoticeSocket item : clients.values()) {
            item.session.getAsyncRemote().sendText(message);
        }
    }

}