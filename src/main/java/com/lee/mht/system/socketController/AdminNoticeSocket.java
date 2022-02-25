package com.lee.mht.system.socketController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/adminNoticeSocket/{username}")
@Slf4j
@Component
public class AdminNoticeSocket {

    private static int onlineCount = 0;
    private static Map<String, AdminNoticeSocket> clients = new ConcurrentHashMap<String, AdminNoticeSocket>();
    private Session session;
    private String username;

    @OnOpen
    public void onOpen(@PathParam("username") String username, Session session) {
        this.username = username;
        this.session = session;
        AdminNoticeSocket.onlineCount++;
        clients.put(username, this);
        log.info("{}登录进来了", username);
    }

    @OnClose
    public void onClose() {
        clients.remove(username);
        log.info("{}登出了",username);
        AdminNoticeSocket.onlineCount--;
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
        for (AdminNoticeSocket item : clients.values()) {
            item.session.getAsyncRemote().sendText(message);
        }
    }

}