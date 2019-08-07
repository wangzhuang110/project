package com.fkj.addrlist.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fkj.addrlist.entities.Equipment;
import com.fkj.addrlist.service.FileService;
import com.glch.fkj398.vmfclient.vmf.base.Base;
import com.glch.fkj398.vmfclient.vmf.pack.command.K0213;
import com.glch.fkj398.vmfclient.vmf.pack.info.K022;
import com.glch.fkj398.vmfclient.vmf.pack.other.K0212;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@Api(tags = {"通信录下发，调用实时接口"})
@RestController
public class SendController {
    @Value("${send}")
    private String sendIp;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    FileService fileService;

    @PostMapping("/send")
    public void send() throws IOException {
        List<Byte> byteList = new ArrayList<>();
        byte low = (byte) (Integer.toUnsignedLong(200) & 0xff);

        for (int i = 0; i < 13; i++) {
            byteList.add(low);
        }
        byteList.add((byte) 0);
        List<Equipment> equipmentList = fileService.getGroup();
        String byteStr = JSON.toJSONString(equipmentList, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
        byte[] bytes = byteStr.getBytes();
        int length = bytes.length;
        Byte[] b = new Byte[length];
        for (int i = 0; i < length; i++) {
            b[i] = bytes[i];
        }
        byteList.addAll(Arrays.asList(b));

        //转化为字节数组，不能直接用list.toArray，会变成Object[]
            byte[] bytes1 =  new byte[byteList.size()];

        for(int i = 0; i < byteList.size(); ++i) {
            bytes1[i] = byteList.get(i);
        }
        byte[] bytes2 = Base64.encodeBase64(bytes1);
//        不能传输用Byte[] 和 Object[] 只能用byte[] 不然接收到的数据会错
        Byte[] bytes3 = byteList.toArray(new Byte[byteList.size()]);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ack", 1);
        jsonObject.put("data", bytes1);
        jsonObject.put("ip", "192.168.1.103");

//        restTemplate.postForObject("http://192.168.1.221:8000/send",jsonObject1,String.class);

        String s = restTemplate.postForObject("http://192.168.1.221:8000/send", jsonObject, String.class);
        System.out.println(s);
    }
    //接收实时发送过来的通信录数据信息
    @PostMapping(value = "/freq")
    public void receive(@RequestBody Object data) {
        //转成byte[]
        System.out.println(data.getClass());
        LinkedHashMap buf = (LinkedHashMap) data;
        ObjectMapper mapper = new ObjectMapper();
        byte[] bytes = mapper.convertValue(buf.get("data"), byte[].class);

        //解包
        Base base = Base.unPack(bytes);
        System.out.println(base.getMessageId());
    }

}



