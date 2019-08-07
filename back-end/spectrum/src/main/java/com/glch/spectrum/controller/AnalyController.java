package com.glch.spectrum.controller;

import com.alibaba.fastjson.JSONObject;
import com.glch.spectrum.entities.FreqPlan;
import com.glch.spectrum.entities.Rectangle;
import com.glch.spectrum.entities.msg.*;
import com.glch.spectrum.service.FreqService;
import com.glch.spectrum.util.IDUtil;
import com.glch.spectrum.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Api(tags = {"新建频谱计划"})
@RestController
public class AnalyController {

    @Value("${file}")
    private String path;

    @Autowired
    FreqService freqService;

    @ApiOperation(value = "新建频谱计划", notes = "解析信息作战计划与网络规划方案")
    @PostMapping("/freq/parse")
    public Result analyFile(@Valid FreqPlan freqPlan, MultipartFile msgPlan, MultipartFile netPlan) throws IOException, ParseException {
        //1、填写频谱计划基本信息，插入到数据库
        freqPlan.setPlanId(IDUtil.getID(24));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        freqPlan.setPlanMakeTime(simpleDateFormat.format(new Date()));
        freqService.insertPlan(freqPlan);

        //2、解析信息作战方案，获取编组、侦察频率、干扰频率、保护频率，存入数据库
        File msgFile = new File(path, "msg.json");
        if (!msgFile.exists()) {
            msgFile.createNewFile();
        }
        msgPlan.transferTo(msgFile);
        analyMsgFile(msgFile, freqPlan.getPlanId());

        //3、解析网络规划方案，获取保护频率，存入数据库
        File netFile = new File(path, "net.json");
        if (!netFile.exists()) {
            netFile.createNewFile();
        }
        return Result.success(freqService.getPlan(freqPlan.getPlanId()));
    }

    private void analyMsgFile(File file, String planId) throws IOException, ParseException {

        String json = FileUtils.readFileToString(file, "UTF-8");
        MsgPlan msgPlan = JSONObject.parseObject(json, MsgPlan.class);  //获取信息作战计划对象

        //保护频率存入数据库
        for (Frequency frequency : msgPlan.getProtectFreqList()) {
            frequency.setFrequencyID(IDUtil.getID(24));   //重新给一个ID，以免需要再次导入此文件，ID重复导致导入失败
            freqService.insertProtect(frequency, planId);
        }
        //将编组转化为指挥所装备层级结构树
        Equipment equipment = groupToCommand(msgPlan.getOpMissionGroupList());

        String phaseFlag = "";  //阶段ID标识
        for (Action action : msgPlan.getOpCombatActionList()) {

            //找到行动所属的阶段ID
            for (Mission mission : msgPlan.getOpMissionList()) {
                if (mission.getOpMissionID().equals(action.getCAMissionID())) {
                    phaseFlag = mission.getOperationPhaseID();
                    break;
                }
            }
            //解析侦察频率
            if (action.getScoutFreqList() != null && action.getScoutFreqList().size() != 0) {
                for (Equipment equip : action.getActionEquipAssign()) {
                    List<Rectangle> rectangles = new ArrayList<>();
                    for (Frequency frequency : action.getScoutFreqList()) {     //侦察频率列表
                        Rectangle rectangle = new Rectangle();
                        //中心频率转装备工作频段上下限
                        getFreqScope(frequency.getCenterFrequency(), frequency.getFrequencyWidth(), rectangle);
                        //装备工作时间设置
                        getOpTime(phaseFlag, rectangle, frequency, msgPlan);
                        rectangles.add(rectangle);
                    }
                    //根据装备类型设置频率
                    setEquipFreqType(equip, rectangles, 0);
                }
            }
            //解析干扰频率
            if (action.getJamFreqList() != null && action.getJamFreqList().size() != 0) {
                for (Equipment equip : action.getActionEquipAssign()) {
                    List<Rectangle> rectangles = new ArrayList<>();
                    for (Frequency frequency : action.getJamFreqList()) {     //侦察频率列表
                        Rectangle rectangle = new Rectangle();
                        //中心频率转装备工作频段上下限
                        getFreqScope(frequency.getCenterFrequency(), frequency.getFrequencyWidth(), rectangle);
                        //装备工作时间设置
                        getOpTime(phaseFlag, rectangle, frequency, msgPlan);
                        rectangles.add(rectangle);
                    }
                    //根据装备类型设置频率
                    setEquipFreqType(equip, rectangles, 1);
                }
            }
            if (action.getScoutFreqList().size() != 0 || action.getJamFreqList().size() != 0) {
                //将行动装备配属（带频率），匹配到编组装备树中
                List<Equipment> list = new ArrayList<>();
                list.add(equipment);
                for (Equipment equip : action.getActionEquipAssign()) {
                    setFreqToCommand(list, equip);
                }
            }
        }
    }

    //将编组重构成树状图装备列表
    private Equipment groupToCommand(List<Troop> list) {
        Equipment upper = new Equipment();//指挥所群组
        for (Troop troop : list) {  //重构指挥所群组
            if (troop.getUpperCommandID().equals("1000")) {
                for (Equipment equipment : troop.getGroupEquipmentList()) {
                    if (equipment.getCombatUnitID().equals(troop.getOwnGradeCmdID())) {
                        upper = equipment;          //获取指挥所群组中的防空电子战团指挥所
                        troop.getGroupEquipmentList().remove(equipment);
                        upper.setEquipmentList(troop.getGroupEquipmentList());
                        break;
                    }
                }
                list.remove(troop);
                break;
            }
        }
        for (Troop troop : list) {  //重构非指挥所群组
            //1 ：本级指挥机构代码 == 上级指挥机构代码
            if (troop.getUpperCommandID().equals(troop.getOwnGradeCmdID())) {
                //1.1 ： 当前群组直属于防空军团指挥所
                if (troop.getUpperCommandID().equals(upper.getCombatUnitID())) {
                    upper.getEquipmentList().addAll(troop.getGroupEquipmentList());
                } else {    //1.2 : 连指挥所
                    for (Equipment command : upper.getEquipmentList()) {
                        if (command.getCombatUnitID().equals(troop.getOwnGradeCmdID())) {
                            command.setEquipmentList(troop.getGroupEquipmentList());
                            break;
                        }
                    }
                }
            //2 : 有组长站的群组，本级指挥机构代码 ！= 上级指挥机构代码
            } else {
                Equipment equipment = new Equipment();      //组长站信息
                for (Equipment equip : troop.getGroupEquipmentList()) {
                    if (equip.getCombatUnitID().equals(troop.getOwnGradeCmdID())) {
                        equipment = equip;
                        troop.getGroupEquipmentList().remove(equip);
                        equipment.setEquipmentList(troop.getGroupEquipmentList());
                        break;
                    }
                }
                //2.1 ： 组长站上级为防空军团指挥所
                if (troop.getUpperCommandID().equals(upper.getCombatUnitID())) {
                    upper.getEquipmentList().add(equipment);
                } else {  //2.2 ： 组长站上级为连指挥所（这种情况应该不存在，组长站与连指挥所平级）
                    for (Equipment cmd : upper.getEquipmentList()) {
                        if (cmd.getCombatUnitID().equals(troop.getUpperCommandID())) {
                            cmd.getEquipmentList().add(equipment);
                            break;
                        }
                    }
                }
            }
        }
        return upper;
//        Troop troop = list.get(0);  //第一个为指挥所群组
//        //将编组中的指挥所群组解析
//        Equipment equip = troop.getGroupEquipmentList().get(0);
//        if (equip.getCombatUnitID().equals(troop.getOwnGradeCmdID())) {
//            troop.getGroupEquipmentList().remove(equip);
//            equip.setEquipmentList(troop.getGroupEquipmentList());
//        }
//        //将各个编组中的装备合并入上述指挥所cmd中
//        for (int i = 1; i < list.size(); i++) {
//            Troop trp = list.get(i);
//            for (Equipment command : equip.getEquipmentList()) {
//                if (command.getCombatUnitID().equals(trp.getOwnGradeCmdID())) {
//                    command.setEquipmentList(trp.getGroupEquipmentList());
//                    break;
//                }
//            }
//        }
//        return equip;
    }

    //中心频率转上下限
    private void getFreqScope(Double center, Double width, Rectangle rectangle) {
        rectangle.setFrequencyUpperLimit(center + width + "");
        rectangle.setFrequencyLowerLimit(center - width + "");
    }

    //时间转换
    private void getOpTime(String phaseFlag, Rectangle rectangle, Frequency frequency, MsgPlan msgPlan) throws ParseException {
        for (Phase phase : msgPlan.getOperationPhaseList()) {
            if (phase.getPhaseID().equals(phaseFlag)) {
                Double timeDifference = phase.getTimeBaseT().getTimeDifference();//与全局时间基准时间差
                BigDecimal diff = new BigDecimal(timeDifference.toString());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sdf.parse(msgPlan.getPlanAttribute().getGlobalTimeBaseT().getAstronomicalTime());//天文时间字符串转对象
                BigDecimal hs = new BigDecimal(60000);
                date.setTime(date.getTime() + diff.multiply(hs).longValue());

                rectangle.setStartTime(date.getTime() + frequency.getPropFreqStartTime().multiply(hs).longValue());
                rectangle.setEndTime(date.getTime() + frequency.getPropFreqEndTime().multiply(hs).longValue());
//                System.out.println(new Date(rectangle.getStartTime()) + "" + new Date(rectangle.getEndTime()) + "");
            }
        }
    }

    //通过装备类型内码设置装备的频率类型
    private void setEquipFreqType(Equipment equipment, List<Rectangle> rectangles, int f) {
        String flag = equipment.getEquipType();
        switch (flag) {
            case "00006":
            case "00007":
            case "00008":
            case "00009":
            case "00010":
            case "00019":
            case "00020":
            case "00021":
            case "00023":
            case "00024":
            case "00025":
                if (f == 0) {
                    equipment.setRadarScoutFreq(rectangles);//雷达计划侦察频率
                }
                if (f == 1) {
                    equipment.setRadarJamFreq(rectangles);//雷达计划干扰频率
                }
                break;
            case "00013":
            case "00014":
                if (f == 0) {
                    equipment.setCommScoutFreq(rectangles);//通信计划侦察频率
                }
                if (f == 1) {
                    equipment.setCommJamFreq(rectangles);//通信计划干扰频率
                }
                break;
            default:
                System.out.println("装备类型内码错误");
                break;
        }
    }

    //将行动中的装备（加了频率）替换到编组command中
    private void setFreqToCommand(List<Equipment> list, Equipment equipment) {
        for (Equipment equip : list) {
            if (equip.getCombatUnitID().equals(equipment.getCombatUnitID())) {
                equip.setRectangle(equipment);
                break;
            }
            if (equip.getEquipmentList() != null && equip.getEquipmentList().size() != 0) {
                setFreqToCommand(equip.getEquipmentList(), equipment);
            }
        }
    }
}
