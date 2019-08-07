package com.glch.spectrum.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glch.fkj398.vmfclient.vmf.base.Base;
import com.glch.fkj398.vmfclient.vmf.group.*;
import com.glch.fkj398.vmfclient.vmf.pack.combat.status.K0216;
import com.glch.fkj398.vmfclient.vmf.pack.combat.status.K0217;
import com.glch.fkj398.vmfclient.vmf.pack.command.K0210;
import com.glch.fkj398.vmfclient.vmf.pack.command.K0213;
import com.glch.fkj398.vmfclient.vmf.pack.command.K0214;
import com.glch.fkj398.vmfclient.vmf.pack.command.K0215;
import com.glch.fkj398.vmfclient.vmf.pack.info.K022;
import com.glch.fkj398.vmfclient.vmf.pack.info.K0271;
import com.glch.fkj398.vmfclient.vmf.pack.other.K02100;
import com.glch.fkj398.vmfclient.vmf.pack.other.K0212;
import com.glch.spectrum.util.IDUtil;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@Api(tags = {"接收报文接口"})
public class TestTime {
//    @Autowired
//    RestTemplate restTemplate;
//
//    //k0212 4.17暂有问题
//    @GetMapping("/test9")
//    public void test4() throws Exception {
//        K02100 k02100 = new K02100();
//        FreeText freeText = new FreeText();
//        freeText.setFreeText("123");
//        k02100.getFreeTextList().add(freeText);
//
//        byte[] bytes = k02100.packDatagram();
//        K0210 k0210 = new K0210();
//        k0210.setWorkStatus(1);
//        DateTime dateTime = new DateTime();
//        dateTime.setYear(2018);
//        dateTime.setDay(1);
//        dateTime.setHour(1);
//        dateTime.setMinute(1);
//        byte[] bytes1 = k0210.pack();
//        Base base = Base.unPack(bytes);
//        K02100 k02100a = (K02100) base;
//
//    }
//
//    @GetMapping("/test3")
//    public void test3() throws Exception {
//        K0271 k0271 = new K0271();
//        //通讯地址编号
//        k0271.setAddressNo(10101);
//        ArrayList<LaserSourceReconnaissance> laserSourceReconnaissanceList = new ArrayList<>();
//        LaserSourceReconnaissance laserSourceReconnaissance = new LaserSourceReconnaissance();
//        //目标编号
//        laserSourceReconnaissance.setTargetNo1((long) 12);
//        //截获时间
//        laserSourceReconnaissance.setInterceptTime(new DateTime(2019, 04, 24, 18, 23, 24));
//        //方位
//        laserSourceReconnaissance.setDirection((double) 45);
//
//        //激光编码位数
//        laserSourceReconnaissance.setLaserCodeDigit(1);
//        //激光编码间隔集合
//        ArrayList<LaserCodingInterval> laserCodingIntervalList = new ArrayList<>();
//        LaserCodingInterval laserCodingInterval = new LaserCodingInterval();
//        //激光编码间隔
//        laserCodingInterval.setLaserCodingInterval((long) 500000);
//        laserCodingIntervalList.add(laserCodingInterval);
//        laserSourceReconnaissance.setLaserCodingIntervalList(laserCodingIntervalList);
//        //激光信息类//光电目标类型：1，目标属性：4，威胁等级：4 识别置信度：100，制导方式：1
//        PhotoelectricTarget photoelectricTarget = new PhotoelectricTarget();
//        //光电目标类型
//        photoelectricTarget.setPhotoelectricTargetType(1);
//        //目标属性
//        photoelectricTarget.setTargetAttribute(4);
//        //威胁等级
//        photoelectricTarget.setThreatLevel(4);
//        //识别置信度
//        photoelectricTarget.setRecognitionConfidence(100);
//        //制导方式
//        photoelectricTarget.setGuidanceSystem(1);
//        laserSourceReconnaissance.setPhotoelectricTarget(photoelectricTarget);
//        laserSourceReconnaissanceList.add(laserSourceReconnaissance);
//        k0271.setLaserSourceReconnaissanceList(laserSourceReconnaissanceList);
//
//        //打包
//        byte[] bytes = k0271.packDatagram();
//        //解包
//        Base base = Base.unPack(bytes);
//        K0271 k02711 = (K0271) base;
//        String json = JSON.toJSONString(k02711, SerializerFeature.PrettyFormat);
//        System.out.println(json);
//    }
//
//    @GetMapping("/test2")
//    public void test2() throws IOException {
//        File file = new File("D:\\data.txt");
//        System.out.println(file.length());
//        FileInputStream fin = new FileInputStream(file);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        IOUtils.copy(fin, baos);
//        byte[] data = baos.toByteArray();
//        Base base = Base.unPack(data);
//        System.out.println(base);
//    }
//
//    @GetMapping("/test1")
//    public K022 test1() throws Exception {
//        File file = new File("D:\\data.json");
//        System.out.println(file.length());
//        FileInputStream fin = new FileInputStream(file);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        IOUtils.copy(fin, baos);
//        byte[] data = baos.toByteArray();
//        String jsonStr = new String(data, "UTF-8");
//        System.out.println(jsonStr);
//        K022 k022 = new Gson().fromJson(jsonStr, K022.class);
//
//        //打包
//        byte[] bytes = k022.packDatagram();
//        System.out.println(bytes.length);
//        //解包
//        Base base = Base.unPack(bytes);
//
//        System.out.println(new Gson().toJson(base));
//        return (K022) base;
//    }
//
//    @GetMapping("/test")
//    public K022 test() throws Exception {
//        File file = new File("D:\\data.json");
//        String json = FileUtils.readFileToString(file, "UTF-8");
//        K022 k022 = JSONObject.parseObject(json, K022.class);
//        //打包
//        byte[] bytes = k022.packDatagram();
//        StringBuilder result = new StringBuilder();
//        for (byte aByte : bytes) {
//            result.append(Long.toString(aByte & 0xff, 2)).append(",");
//        }
//        System.out.println(result.toString().substring(0, result.length() - 1));
//
//        OutputStream out = new FileOutputStream("D:\\data.txt");
//        InputStream is = new ByteArrayInputStream(bytes);
//        byte[] buff = new byte[1024];
//        int len = 0;
//        while ((len = is.read(buff)) != -1) {
//            out.write(buff, 0, len);
//        }
//        is.close();
//        out.close();
//
//        //解包
//        Base base = Base.unPack(bytes);
//        System.out.println(base.getMessageId());
//        return (K022) base;
//    }
//
//    @PostMapping("/send")
//    public void send() throws Exception {
//        K0213 k0213 = new K0213();
//        k0213.setContent("大叔大婶");
//        k0213.setActionId(1);
//        k0213.setActionMode((byte) 1);
//        k0213.setActionType((byte) 2);
//        k0213.setFrequencyMode((short) 1);
//        k0213.setStartDateTime(new DateTime(2018, 2, 13, 5, 16, 3));
//        k0213.setEndDateTime(new DateTime(null, 3, 12, 7, 39, 55));
//
//        ArrayList<FrequencyRange> frequencyList = new ArrayList<>();
//        frequencyList.add(new FrequencyRange(123, 324));
//        frequencyList.add(new FrequencyRange(223, 556));
//        k0213.setFrequencyList(frequencyList);
//
//        byte[] bytes = k0213.packDatagram();
//
//        K0214 k0214 = new K0214();
//
//        UseFrequencyRange useFrequencyRange = new UseFrequencyRange();
//        useFrequencyRange.setCenterFrequency(12);
//        useFrequencyRange.setEndDatetime(new DateTime(null, 3, 12, 7, 39, 55));
//        useFrequencyRange.setPulseWidth(10);
//        useFrequencyRange.setStartDatetime(new DateTime(2018, 2, 13, 5, 16, 3));
//        AreaAttributeR areaAttributeR1 = new AreaAttributeR(1, 2);
//        AreaAttributeR areaAttributeR2 = new AreaAttributeR(3, 4);
//        Rectangle rectangle = new Rectangle(areaAttributeR1, areaAttributeR2, 9);
//        useFrequencyRange.setRectangle(rectangle);
//        DirectionRange directionRange = new DirectionRange(1, 2);
//        Sector sector = new Sector(areaAttributeR1, directionRange, 5, 6);
//        useFrequencyRange.setSector(sector);
//        ArrayList<StationPositionGRI> stationPositionGRI = new ArrayList<>();
//        stationPositionGRI.add(new StationPositionGRI(3, 5, 6));
//        useFrequencyRange.setStationPositionGRIList(stationPositionGRI);
//        List list = new ArrayList();
//        list.add(useFrequencyRange);
//        k0214.setUseFrequencyRangeList(list);
//
//
//        byte[] bytes14 = k0214.packDatagram();
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("ack", 1);
//        jsonObject.put("data", bytes14);
//        jsonObject.put("ip", "192.168.1.221");
//        restTemplate.postForObject("http://192.168.1.221:8200/test", jsonObject,String.class);
//        JSONObject jsonObject1 = new JSONObject();
//        jsonObject1.put("ack", 1);
//        jsonObject1.put("data", bytes);
//        jsonObject1.put("ip", "192.168.1.221");
//
////        restTemplate.postForObject("http://192.168.1.221:8000/send",jsonObject1,String.class);
//        String s = restTemplate.postForObject("http://192.168.1.221:8000/send", jsonObject1, String.class);
//
//
////        System.out.println(s);
//    }
//
//    @ApiOperation(value = "接收推送消息", notes = "接收推送消息")
//    @ApiImplicitParam(name = "data", value = "数据内容UTF-8转码后的byte数组", required = true, dataType = "Object")
//    @PostMapping(value = "/freqEQ")
//    public void receive(@RequestBody Object data) {
//        System.out.println(IDUtil.getID());
//        //转成byte[]
//        LinkedHashMap buf = (LinkedHashMap) data;
//        ObjectMapper mapper = new ObjectMapper();
//        byte[] s = ((String) buf.get("data")).getBytes();
////        System.out.println(buf.get("data").getClass());
////        Byte[] bytes1 = (Byte[]) JSON.parseArray((String) buf.get("data"), Byte.class).toArray();
//        byte[] s1 = Base64.decodeBase64(s);
//        byte[] bytes = mapper.convertValue(buf.get("data"), byte[].class);
//
//        //解包
//        Base base = Base.unPack(bytes);
//        System.out.println(base.getMessageId());
//        switch (base.getMessageId()) {
//            case 12:
//                K0212 k0212 = (K0212) base;
//                System.out.println(k0212.getTimeInterval());
//                System.out.println("12");
//                break;
//            case 13:
//                K0213 k0213 = (K0213) base;      //向下转型成具体报文类对象
//                System.out.println("13");
//                break;
//            case 14:
//
//                System.out.println("14");
//                break;
//            case 10:
//                System.out.println("10");
//                break;
//            case 19:
//                System.out.println("19");
//                break;
//            case 20:
//                System.out.println("20");
//                break;
//            case 22:
//                System.out.println("22");
//                K022 k022 = (K022) base;
//                break;
//            default:
//                System.out.println("接收报文错误");
//                break;
//        }
//    }
//
//    @ApiOperation(value = "测试")
//    @GetMapping("/vmf213")
//    public K0213 testVmf() throws Exception {
//        K0213 k0213 = new K0213();
//        k0213.setContent("大叔大婶");
//        k0213.setActionId(1);
//        k0213.setActionMode((byte) 1);
//        k0213.setActionType((byte) 2);
//        k0213.setFrequencyMode((short) 1);
//        k0213.setStartDateTime(new DateTime(2018, 2, 13, 5, 16, 3));
//        k0213.setEndDateTime(new DateTime(null, 3, 12, 7, 39, 55));
//
//        ArrayList<FrequencyRange> frequencyList = new ArrayList<>();
//        frequencyList.add(new FrequencyRange(123, 324));
//        frequencyList.add(new FrequencyRange(223, 556));
//        k0213.setFrequencyList(frequencyList);
//
//        byte[] bytes = k0213.packDatagram();
//        System.out.println(bytes);
//        StringBuilder buf = new StringBuilder(bytes.length * 2);
//        for (Byte b : bytes) {
//            buf.append(String.format("%02x", new Integer(b & 0xff)));
////            String ss = Integer.toHexString(b);
////            System.out.println(Integer.toBinaryString(b));
//        }
//        System.out.println(buf.toString());
////        String buf = new String(bytes, StandardCharsets.UTF_8);
////        System.out.println(buf);
////        File file = new File("E:\\1.txt");
////        FileOutputStream fos = new FileOutputStream(file);
////        fos.write(bytes);
////        System.out.println("写入成功");
////        fos.close();
//        // 解包
////        K0213 k0213unpack = new K0213().unPackDatagram(bytes);
//        Base base = Base.unPack(bytes);
//        System.out.println(base.getMessageId());
//        K0213 unpack = (K0213) base;
//        System.out.println(unpack.getContent());
//        // 获得对应的值
//        return unpack;
//    }
//    @GetMapping("/vmf214")
//    public K0214 testVmf214() throws Exception {
//        K0214 K0214 = new K0214();
//        K0214.setUpdateMode(true);
//        //打包
//
//
//        // 解包
//        K0213 k0213unpack = new K0213().unPackDatagram(bytes);
//        Base base = Base.unPack(bytes);
//        System.out.println(base.getMessageId());
//        K0213 unpack = (K0213) base;
//        // 获得对应的值
//
//    }
}
