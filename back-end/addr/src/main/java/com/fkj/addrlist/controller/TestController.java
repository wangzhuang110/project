package com.fkj.addrlist.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bfd.overseas.uop.mcs.client.holder.McsContextHolder;
import com.fkj.addrlist.entities.Equipment;
import com.fkj.addrlist.service.FileService;
import com.fkj.addrlist.util.LoggerUtils;
import com.fkj.addrlist.util.Result;
import com.glch.fkj398.msgservice.entity.TransferMsg;
import com.glch.fkj398.msgservice.handler.Message.MessengerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Api(tags = {"通信录下发，引入实时依赖"})
@RestController
public class TestController {
    @Value("${send}")
    private String sendIp;

    @Autowired
    FileService fileService;

    @GetMapping("/ww")
    public String testasd(){
        return sendIp;
    }
    @ApiOperation("通信录下发")
    @PostMapping("/addr/send")
    public Result Test() throws InterruptedException, IOException {

        long startTime = System.currentTimeMillis();

        List<Equipment> list = fileService.getGroup();
        String byteStr = JSON.toJSONString(list, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
        byte[] bytes = byteStr.getBytes();

        send(sendIp, bytes);

        long endTime = System.currentTimeMillis();
        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        LoggerUtils.info(simpleDateFormat.format(new Date()), McsContextHolder.userDetail().getName(), McsContextHolder.userDetail().getSeatName(), "通信录服务软件", "通信录下发");
        return Result.success();
    }

    //遍历通信录，发送通信录
    private void cycle(List<Equipment> list, byte[] bytes) throws InterruptedException {
        for (Equipment equipment : list) {
            send(equipment.getIp(), bytes);
            if (equipment.getChildren() != null && equipment.getChildren().size() != 0) {
                cycle(equipment.getChildren(), bytes);
            }
        }
    }

    //发送实时消息接口
    private String send(String ip, byte[] bytes) throws InterruptedException {
        TransferMsg msg = new TransferMsg();
        msg.setIp(ip);
        msg.setPort(8309);
        msg.setAck(1);
        msg.setSubsectionsize(2048);
        msg.setRetrySendTimes(3);
        msg.setTimeOutMS(5000);
        msg.setData(bytes);

        MessengerService ms = new MessengerService();
        return ms.SendUDP(msg);//发送成功返回：发送成功！，发送失败返回：发送超时！;
    }
}
