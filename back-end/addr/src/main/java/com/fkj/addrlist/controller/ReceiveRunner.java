package com.fkj.addrlist.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fkj.addrlist.entities.Equipment;
import com.fkj.addrlist.socket.MessageEventHandler;
import com.glch.fkj398.msgservice.handler.Message.MsgCallBackImpl;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.concurrent.locks.Lock;

@Component
public class ReceiveRunner implements CommandLineRunner {
    @Value("${file.groupPath}")
    private String groupPath;

    @Override
    public void run(String... args) {
        Thread receiveThread = new Thread(new ReceiveThread(), "ReceiveThread");
        receiveThread.start();
    }

    public class ReceiveThread implements Runnable {
        public void run() {
            while (true) {
                try {
                    while (MsgCallBackImpl.vectorBuffer.size() > 0) {
                        //接收消息
                        byte[] bytes = (byte[]) MsgCallBackImpl.vectorBuffer.remove(0);
                        //写入group.txl
                        String jsonString = new String(bytes, "UTF-8");
                        List<Equipment> list = JSON.parseArray(jsonString, Equipment.class);
                        String s = JSON.toJSONString(list, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat).replace("\n", "\r\n");
                        FileUtils.writeStringToFile(new File(groupPath, "group.txl"), s, "UTF-8", false);
                        //socket.io推送前端
                        MessageEventHandler.sendBuyLogEvent(list);
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        }
    }

}