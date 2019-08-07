package com.glch.spectrum.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glch.fkj398.vmfclient.vmf.base.Base;
import com.glch.fkj398.vmfclient.vmf.group.EquipmentState;
import com.glch.fkj398.vmfclient.vmf.pack.combat.status.K0217;
import com.glch.spectrum.entities.msg.Equipment;
import com.glch.spectrum.service.FeignAddrService;
import com.glch.spectrum.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;


@RestController
@Api(tags = {"频谱监控"})
public class MonitorController {

    @Autowired
    FeignAddrService feignAddrService;

    @ApiOperation(value = "接收推送消息", notes = "接收推送消息")
    @ApiImplicitParam(name = "data", value = "数据内容UTF-8转码后的byte数组", required = true, dataType = "Object")
    @PostMapping(value = "/monitor")
    public void receive(@RequestBody Object data) {
        //转成byte[]
        LinkedHashMap buf = (LinkedHashMap) data;
        ObjectMapper mapper = new ObjectMapper();
        byte[] s = ((String) buf.get("data")).getBytes();
//        System.out.println(buf.get("data").getClass());
//        Byte[] bytes1 = (Byte[]) JSON.parseArray((String) buf.get("data"), Byte.class).toArray();
        byte[] s1 = Base64.decodeBase64(s);
        byte[] bytes = mapper.convertValue(buf.get("data"), byte[].class);

        //解包
        Base base = Base.unPack(bytes);
        System.out.println(base.getMessageId());
        switch (base.getMessageId()) {
            case 17:
                K0217 k0217 = (K0217) base;
                for (EquipmentState sta : k0217.getEquipmentStateList()) {
                    //1、通过通信地址编码找到对应装备
                    Result result = feignAddrService.getEquipBySn(String.valueOf(sta.getAddressNo()));
                    Equipment equipment = (Equipment) result.getData();
                    //2、通过工作状态判断是侦察还是干扰
                    switch (sta.getWorkState()) {
                        case 15:
                            System.out.println("干扰");
                        case 18:
                            System.out.println("侦察");
                        default:
                            break;
                    }
                }
                break;
            default:
                System.out.println("接收报文错误");
                break;
        }
    }
    @GetMapping("/equip")
    public void test(){
        Result result = feignAddrService.getEquipBySn(String.valueOf(1111111));
        Equipment equipment = (Equipment) result.getData();
        System.out.println(equipment);
    }
}
