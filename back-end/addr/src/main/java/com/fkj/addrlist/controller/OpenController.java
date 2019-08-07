package com.fkj.addrlist.controller;

import com.fkj.addrlist.entities.Equipment;
import com.fkj.addrlist.service.FileService;
import com.fkj.addrlist.util.LoggerUtils;
import com.fkj.addrlist.util.Result;
import com.fkj.addrlist.util.ResultCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Api(tags = {"通信录接口调用"})
@RestController
public class OpenController {
    @Value("${file.groupPath}")
    private String groupPath;

    @Autowired
    FileService fileService;

    @ApiOperation(value = "根据装备编号获取ip")
    @GetMapping("/ip/{equipmentSn}")
    public Result getIPByEquipId(@PathVariable("equipmentSn") String equipmentSn) throws IOException {
        if (StringUtils.isEmpty(equipmentSn)) {
            return Result.error(ResultCode.PARAM_IS_BLANK);
        }
        String ip = "";
        ip = getIpByEquipId(fileService.getGroup(), equipmentSn, ip);
        return ip.equals("") ? Result.error(ResultCode.PARAM_IS_INVALID) : Result.success(ip);
    }

    @ApiOperation(value = "获取ip列表")
    @PostMapping("/ip/list")
    public Result getIpList(@RequestBody List<String> list) throws IOException {
        if(list == null || list.size() == 0 ){
            return Result.error(ResultCode.PARAM_IS_BLANK);
        }
        List<String> ipList = new ArrayList<>();
        String ip = "";
        for (String sn : list) {
            ip = getIpByEquipId(fileService.getGroup(), sn, ip);
            ipList.add(ip);
            ip = "";
        }
        return Result.success(ipList);
    }

    @ApiOperation(value = "根据通信地址编码获取节点信息")
    @GetMapping("/node/{equipmentSn}")
    public Result getNodeByEquipmentSn(@PathVariable("equipmentSn") String equipmentSn) throws IOException {
        List<Equipment> list = fileService.getGroup();
        Equipment node = getNodeByEquipId(list, equipmentSn, new Equipment());
        node.setChildren(new ArrayList<>());
        if (StringUtils.isEmpty(node.getIp())) {
            return Result.error(ResultCode.PARAM_IS_INVALID);
        }
        return Result.success(node);
    }

    @ApiOperation(value = "根据通信地址编码获取ip")
    @GetMapping("/ip/{cmd}")
    public Result getIP(@PathVariable("cmd") String cmd) throws IOException {
        Equipment node = getNode(cmd);
        if (StringUtils.isEmpty(node.getIp())) {
            return Result.error(ResultCode.PARAM_IS_INVALID);
        }
        List<Equipment> equipment = fileService.getGroup();
        String ip = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        LoggerUtils.info(simpleDateFormat.format(new Date()), "wz", "指挥席", "da", "dads");
        return Result.success(getIpByCmd(equipment, cmd, ip));
//        return ips.equals("")?"作战单元编号不存在":ips;
    }

    @ApiOperation(value = "根据通信地址编码获取节点及其上下级信息")
    @GetMapping("/nodes/{cmd}")
    public Result getNodeAndUpper(@PathVariable("cmd") String cmd) throws IOException {
        Equipment equipment = getUpper(cmd);
        Equipment node = getNode(cmd);
        if (StringUtils.isEmpty(node.getIp())) {
            return new Result(5002, "通信地址编码错误");
        }
        if (!node.getChildren().isEmpty() && node.getChildren() != null) {
            for (Equipment equip : node.getChildren()) {
                equip.setChildren(new ArrayList<>());
            }
        }
        if (StringUtils.isEmpty(equipment.getIp())) {
            return Result.success(node);
        }
        List<Equipment> list = new ArrayList<>();
        list.add(node);
        equipment.setChildren(list);
        return Result.success(equipment);
    }

    @ApiOperation(value = "根据通信地址编码获取节点信息")
    @GetMapping("/node/{cmd}")
    public Result getNodeMsg(@PathVariable("cmd") String cmd) throws IOException {
        Equipment node = getNode(cmd);
        node.setChildren(new ArrayList<>());
        if (StringUtils.isEmpty(node.getIp())) {
            return new Result(5002, "通信地址编码错误");
        }
        return Result.success(node);
    }

    private Equipment getUpper(String cmd) throws IOException {
        List<Equipment> equipment = fileService.getGroup();
        return getUpperByCmd(equipment, cmd, new Equipment());
    }

    private Equipment getUpperByCmd(List<Equipment> list, String cmd, Equipment node) {
        for (Equipment equipment : list) {
            if (!equipment.getChildren().isEmpty() && equipment.getChildren() != null) {
                for (Equipment equip : equipment.getChildren()) {
                    if (equip.getCombatUnitID().equals(cmd)) {
                        node = equipment;
                        break;
                    }
                }
                if (StringUtils.isEmpty(node.getIp())) {
                    node = getUpperByCmd(equipment.getChildren(), cmd, node);
                }
            }
        }
        return node;
    }

    //根据通信地址编码获取节点
    private Equipment getNode(String cmd) throws IOException {
        List<Equipment> list = fileService.getGroup();
        return getNodeByCmd(list, cmd, new Equipment());
    }

    //根据通信地址编码获取节点
    private Equipment getNodeByCmd(List<Equipment> list, String cmd, Equipment node) {
        for (Equipment equipment : list) {
            if (equipment.getCombatUnitID().equals(cmd)) {
                node = equipment;
                break;
            }
            if (StringUtils.isEmpty(node.getIp())) {
                if (!equipment.getChildren().isEmpty() && equipment.getChildren() != null) {
                    node = getNodeByCmd(equipment.getChildren(), cmd, node);
                }
            }
        }
        return node;
    }

    //根据通信地址编码获取节点
    private Equipment getNodeByEquipId(List<Equipment> list, String equipmentSn, Equipment node) {
        for (Equipment equipment : list) {
            if (equipment.getEquipmentSn().equals(equipmentSn)) {
                node = equipment;
                break;
            }
            if (StringUtils.isEmpty(node.getIp())) {
                if (!equipment.getChildren().isEmpty() && equipment.getChildren() != null) {
                    node = getNodeByEquipId(equipment.getChildren(), equipmentSn, node);
                }
            }
        }
        return node;
    }

    private String getIpByCmd(List<Equipment> list, String cmd, String ip) {
        for (Equipment equipment : list) {
            if (equipment.getCombatUnitID().equals(cmd)) {
                ip = equipment.getIp();
                break;
            }
            if (ip.equals("")) {
                if (!equipment.getChildren().isEmpty() && equipment.getChildren() != null) {
                    ip = getIpByCmd(equipment.getChildren(), cmd, ip);
                }
            }
        }
        return ip;
    }

    private String getIpByEquipId(List<Equipment> list, String equipmentSn, String ip) {
        for (Equipment equipment : list) {
            if (equipment.getEquipmentSn().equals(equipmentSn)) {
                ip = equipment.getIp();
                break;
            }
            if (ip.equals("")) {
                if (!equipment.getChildren().isEmpty() && equipment.getChildren() != null) {
                    ip = getIpByEquipId(equipment.getChildren(), equipmentSn, ip);
                }
            }
        }
        return ip;
    }
}
