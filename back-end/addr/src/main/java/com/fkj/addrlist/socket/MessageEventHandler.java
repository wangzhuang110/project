package com.fkj.addrlist.socket;


import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.fkj.addrlist.entities.Equipment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Component
public class MessageEventHandler {
    public static SocketIOServer socketIoServer;
    static ArrayList<UUID> listClient = new ArrayList<>();
    static final int limitSeconds = 1;
    @Autowired
    public MessageEventHandler(SocketIOServer server) {
        this.socketIoServer = server;
    }

    @OnConnect
    public void onConnect(SocketIOClient client) {
        listClient.add(client.getSessionId());
        System.out.println("客户端:" + client.getSessionId() + "已连接");
        sendMsg("successful");
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        System.out.println("客户端:" + client.getSessionId() + "断开连接");
    }

//
//    @OnEvent(value = "msg")
//    public void onEvent(SocketIOClient client, AckRequest request, Object data) {
//        System.out.println("发来消息：" + data);
//        socketIoServer.getClient(client.getSessionId()).sendEvent("msg", "back data");
//    }

    public static void sendMsg(String str) {   //这里就是向客户端推消息了
//        String dateTime = new DateTime().toString("hh:mm:ss");
        for (UUID clientId : listClient) {
            if (socketIoServer.getClient(clientId) == null) continue;
            socketIoServer.getClient(clientId).sendEvent("msg", str, 1);
        }
    }
    public static void sendBuyLogEvent(List<Equipment> str) {   //这里就是向客户端推消息了
//        String dateTime = new DateTime().toString("hh:mm:ss");
        for (UUID clientId : listClient) {
            if (socketIoServer.getClient(clientId) == null) continue;
            socketIoServer.getClient(clientId).sendEvent("msg", str, 1);
        }
    }
    public static void sendToUop(String str) {   //这里就是向客户端推消息了
        for (UUID clientId : listClient) {
            if (socketIoServer.getClient(clientId) == null) continue;
            socketIoServer.getClient(clientId).sendEvent("remind", str);
        }
    }
}