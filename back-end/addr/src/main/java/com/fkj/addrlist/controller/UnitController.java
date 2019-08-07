package com.fkj.addrlist.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.InetAddressCodec;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bfd.overseas.uop.mcs.client.holder.McsContextHolder;
import com.fkj.addrlist.entities.*;
import com.fkj.addrlist.service.FileService;
import com.fkj.addrlist.service.UnitService;
import com.fkj.addrlist.socket.MessageEventHandler;
import com.fkj.addrlist.util.LoggerUtils;
import com.fkj.addrlist.util.Result;
import com.fkj.addrlist.util.ResultCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Api(tags = {"通信录操作"})
@RestController
public class UnitController {
    @Value("${file.unitPath}")
    private String unitPath;
    @Value("${file.groupPath}")
    private String groupPath;
    @Autowired
    FileService fileService;
    @Autowired
    UnitService unitService;
    @Autowired
    SeatInfo seatInfo;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    private KafkaTemplate kafkaTemplate;

    @GetMapping("/grp")
    public void getPath(){
        System.out.println(groupPath);
    }

    //初始化通信录
    @ApiOperation(value = "初始化通信录")
    @GetMapping(value = "/init")
    public Result init() throws IOException {
        File file = new File(unitPath);
        List<Equipment> list = new ArrayList<>();
        if (!file.exists()) {
            file.mkdirs();
        }
        List<Equipment> equipments = unitService.getEquipments();//获取共享库装备列表信息
        if (equipments == null || equipments.size() == 0) {
            return new Result(5001, "共享库不存在装备信息");
        }
        File[] fileList = file.listFiles();
        if (fileList == null || fileList.length == 0) {
            list = getEquipments();//不存在旧编制，直接初始化
        } else {
            for (File f : fileList) {
                if (f.getName().equals("unit.txl")) {
                    //共享库装备信息无变化，直接返回文件中编制信息
                    List<Equipment> list1 = fileService.getUnit();
                    if (list1.size() == equipments.size() && list1.containsAll(equipments)) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                        LoggerUtils.info(simpleDateFormat.format(new Date()), McsContextHolder.userDetail().getName(), McsContextHolder.userDetail().getSeatName(), "通信录服务软件", "新建通信录");
//                        return Result.success(fileService.getUnit());
                    }
                    list = joint();
                }
            }
        }
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        LoggerUtils.info(simpleDateFormat.format(new Date()), McsContextHolder.userDetail().getName(), McsContextHolder.userDetail().getSeatName(), "通信录服务软件", "新建通信录");
        return Result.success(list);
    }

    //获取文件中的通信录
    @ApiOperation(value = "获取文件中的通信录")
    @GetMapping(value = "/addr/get")
    public Result getAddr() throws IOException {
        File file = new File(groupPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        File[] fileList = file.listFiles();
        if (fileList == null || fileList.length == 0) {
            return new Result(5001, "通信录不存在，请先上传！");
        } else {
            for (File f : fileList) {
                if (f.getName().equals("group.txl") && f.isFile()) {
                    return Result.success(fileService.getGroup());
                }
                f.delete();
            }
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        LoggerUtils.info(simpleDateFormat.format(new Date()), McsContextHolder.userDetail().getName(), McsContextHolder.userDetail().getSeatName(), "通信录服务软件", "打开当前正在使用的通信录");
        return Result.error(ResultCode.RESULE_DATA_NONE);
    }

    //拖动完成后调用此接口将修改后通信录数据写入文件
    @ApiOperation(value = "保存拖动更改")
    @PostMapping(value = "/save")
    public Result putAddr(@RequestBody String equipment) throws IOException {
        JSONArray jsonArray = JSON.parseArray(equipment);

        removeSeat(jsonArray);//修改前端数据格式，将children中的seat放到seats中，然后删除
        List<Equipment> list = JSON.parseArray(jsonArray.toString(), Equipment.class);

        String json = JSON.toJSONString(list, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat).replace("\n", "\r\n");
        FileUtils.writeStringToFile(new File(groupPath, "group.txl"), json, "UTF-8", false);

        return Result.success();
    }

    //test change front date
    @GetMapping("equip")
    public Result test1() throws IOException {
        String json = FileUtils.readFileToString(new File(groupPath, "group.txl"), "UTF-8");
        JSONArray jsonArray = JSON.parseArray(json);
        removeSeat(jsonArray);
        List<Equipment> list = JSON.parseArray(jsonArray.toString(), Equipment.class);
        return Result.success(list);
    }

    //前端传过来的json数据跟对象字段不一致，进行数据格式的修改
    private void removeSeat(JSONArray jsonArray) {
        for (Object aJsonArray : jsonArray) {
            JSONObject obj = (JSONObject) aJsonArray;
            JSONArray ary = (JSONArray) obj.get("children");//children and seats
            if (ary.size() != 0) {
                for (int j = 0; j < ary.size(); j++) {
                    JSONObject jsonObject = (JSONObject) ary.get(j);//children or seats
                    if (jsonObject.containsKey("seat")) {
                        obj.put("seats", jsonObject.get("seat"));
                        ary.remove(j);
                    }
                }
                removeSeat(ary);
            }
        }
    }

    @PostMapping(value = "/dadasd")
    public void dsadad(JSONObject jsonObject) {
        System.out.println("sdad");
    }

    //更新节点IP
    @ApiOperation(value = "更新节点的IP")
    @PostMapping(value = "/ip/put")
    public Result putIp(@RequestBody @Valid PutIp putIp) throws IOException {
        if (putIp.getNewIp().equals("192.168.1.1")) {
            return new Result(1001, "IP不能为192.168.1.1");
        }
        switch (putIp.getFlag()) {
            case 1:
                List<Equipment> init = fileService.getUnit();
                for (Equipment equip : init) {
                    if (equip.getIp().equals(putIp.getNewIp())) {
                        return new Result(1001, "IP已存在");
                    }
                }
                for (Equipment equipment : init) {
                    if (equipment.getIp().equals(putIp.getOldIp())) {
                        equipment.setIp(putIp.getNewIp());
                        equipment.setTelAddress(putIp.getNewIp());
                    }
                }
                String jsonString = JSON.toJSONString(init, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat).replace("\n", "\r\n");
                FileUtils.writeStringToFile(new File(unitPath, "unit.txl"), jsonString, "UTF-8", false);
                return Result.success();
            case 2:
                Integer flag = 0;
                List<Equipment> address = fileService.getGroup();

                if (select(address, putIp.getNewIp(), flag) == 1) {
                    return new Result(1001, "IP已存在");
                }
                changeIp(address, putIp);//修改通信录IP

                File file = new File(unitPath);
                if (file.exists()) {
                    List<Equipment> unit = fileService.getUnit();
                    for (Equipment equipment : unit) {
                        if (equipment.getIp().equals(putIp.getOldIp())) {
                            equipment.setIp(putIp.getNewIp());
                            equipment.setTelAddress(putIp.getNewIp());
                        }
                    }
                    String s = JSON.toJSONString(unit, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat).replace("\n", "\r\n");
                    FileUtils.writeStringToFile(new File(unitPath, "unit.txl"), s, "UTF-8", false);
                }

                String json = JSON.toJSONString(address, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat).replace("\n", "\r\n");
                FileUtils.writeStringToFile(new File(groupPath, "group.txl"), json, "UTF-8", false);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                LoggerUtils.info(simpleDateFormat.format(new Date()), "wz", "系统控制管理席", "通信录服务软件", "将旧IP为" + putIp.getOldIp() + "更改为新IP" + putIp.getNewIp());
                return Result.success();
        }
        return new Result(2, "未知错误");
    }

    //更新提醒,不存在要提示，失败要提示
    @ApiOperation(value = "通信录更新提醒")
    @GetMapping("/addr/remind")
    public Result remindAddr() throws IOException {
        MessageEventHandler.sendToUop("on");
        List<Equipment> txl = fileService.getGroup();
        ListenableFuture future = kafkaTemplate.send("ADDRLIST_0", JSON.toJSONString(txl));
        future.addCallback(o -> System.out.println("消息发送成功"), throwable -> System.out.println("消息发送失败"));
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        LoggerUtils.info(simpleDateFormat.format(new Date()), "wz", "系统控制管理席", "通信录服务软件", "通信录更新提醒");
        return Result.success();
    }

    //修改通信录IP
    private void changeIp(List<Equipment> list, PutIp putIp) {
        for (Equipment equipment : list) {
            if (equipment.getIp().equals(putIp.getOldIp())) {
                equipment.setIp(putIp.getNewIp());
                equipment.setTelAddress(putIp.getNewIp());
                break;
            }
            if (equipment.getChildren() != null && equipment.getChildren().size() != 0) {
                changeIp(equipment.getChildren(), putIp);
            }
        }
    }

    //判断IP是否已存在
    private Integer select(List<Equipment> list, String ip, Integer flag) {
        for (Equipment equipment : list) {
            if (equipment.getIp().equals(ip)) {
                flag = 1;
                return flag;
            }
            if (equipment.getChildren() != null && equipment.getChildren().size() != 0) {
                flag = select(equipment.getChildren(), ip, flag);
                if (flag == 1) {
                    return flag;
                }
            }
        }
        return flag;
    }

    //读取配置文件中存储的编制信息
    @ApiOperation(value = "读取文件存储的编制")
    @GetMapping(value = "/fileUnit")
    public Result fileUnitGet() throws IOException {
        File file = new File(unitPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        File[] fileList = file.listFiles();
        if (fileList == null || fileList.length == 0) {
            return Result.error(ResultCode.RESULE_DATA_NONE);
        } else {
            for (File f : fileList) {
                if (f.getName().equals("unit.txl")) {
                    return Result.success(fileService.getUnit());
                }
                f.delete();
            }
        }
        return Result.error(ResultCode.RESULE_DATA_NONE);
    }

    //初始化通信录,填写ip信息并保存成文件,席位不再分配IP
    private List<Equipment> getEquipments() throws IOException {
        List<Equipment> list = fillSeat();
        if (!list.isEmpty()) {
            Integer i = 2;
            for (Equipment equipment : list) {
                equipment.setName(equipment.getName() + equipment.getEquipmentSn().substring(3, 5));
                equipment.setIp(ipSet(i++));
                equipment.setTelAddress(equipment.getIp());
                equipment.setCombatUnitID(equipment.getEquipmentSn());
            }
            String json = JSON.toJSONString(list, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
            String jsonString = json.replace("\n", "\r\n");
            FileUtils.writeStringToFile(new File(unitPath, "unit.txl"), jsonString, "UTF-8", false);
        }
        return list;
    }

    //获取共享库编制并匹配席位信息
    private List<Equipment> fillSeat(){
        List<Equipment> list = unitService.getEquipments();//获取共享库编制信息
        if (!list.isEmpty()) {
            for (Equipment equipment : list) {//填充席位，与dev.yml匹配
                for (UnitEquipment unitEquipment : seatInfo.getEquipmentList()) {
                    if (equipment.getEquipmentTypeId().equals(unitEquipment.getEquipmentTypeId())) {
                        List<Seat> seatList = new ArrayList<>();
                        if (unitEquipment.getSeats() != null) { //new Seat防止同类型车获取同对象引用的seat导致ip不能独立
                            for (Seat seat : unitEquipment.getSeats()) {
                                Seat newSeat = new Seat();
                                BeanUtils.copyProperties(seat, newSeat);
                                seatList.add(newSeat);
                            }
                        }
                        equipment.setSeats(seatList);
                    }
                }
            }
        }
        return list;
    }

    //将原编制文件中的IP信息补充到新编制文件中
    private List<Equipment> joint() throws IOException {
        List<Equipment> oldList = fileService.getUnit();
        List<Equipment> newList = fillSeat();
        for (Equipment equipment : newList) {
            for (Equipment oldEquipment : oldList) {
                if (equipment.getEquipmentSn().equals(oldEquipment.getEquipmentSn())) {
                    equipment.setCombatUnitID(oldEquipment.getEquipmentSn());
                    equipment.setIp(oldEquipment.getIp());
                    equipment.setTelAddress(oldEquipment.getIp());
                    equipment.setSeats(oldEquipment.getSeats());
                }
            }
        }
        setIp(newList);//更新部分ip生成
        String json = JSON.toJSONString(newList, SerializerFeature.PrettyFormat, SerializerFeature.DisableCircularReferenceDetect);
        String jsonString = json.replace("\n", "\r\n");
        FileUtils.writeStringToFile(new File(unitPath, "unit.txl"), jsonString, "UTF-8", false);
//        FileUtils.writeStringToFile(new File(unitPath, "backup.txl"), jsonString, "UTF-8", false);
        return newList;
    }

    //更新部分ip生成
    private List<Equipment> setIp(List<Equipment> list) {
        for (Equipment equipment : list) {
            if (equipment.getIp() == null || equipment.getIp().equals("")) {//定位没有IP的节点
                for (int i = 2; i < 255; i++) {
                    int flag = 0;
                    for (Equipment equipment1 : list) {
                        if (equipment1.getIp() != null && !equipment1.getIp().equals("")) {
                            int index = equipment1.getIp().indexOf(".");
                            index = equipment1.getIp().indexOf(".", index + 1);
                            index = equipment1.getIp().indexOf(".", index + 1);
                            String result = equipment1.getIp().substring(index + 1);
                            if (result.equals(i + "")) {
                                flag = 1;//最后一位为i的ip已存在
                                break;
                            }
                        }
                    }
                    if (flag == 0) {
                        equipment.setIp(ipSet(i));
                        equipment.setTelAddress(equipment.getIp());
                        equipment.setCombatUnitID(equipment.getEquipmentSn());
                        break;
                    }
                }
            }
        }
        return list;
    }

    //IP生成
    private String ipSet(Integer i) {
        return "192.168.1." + i;
    }
}
