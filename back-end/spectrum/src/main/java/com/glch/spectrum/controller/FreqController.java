package com.glch.spectrum.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glch.spectrum.entities.*;
import com.glch.spectrum.entities.msg.Frequency;
import com.glch.spectrum.service.FreqService;
import com.glch.spectrum.service.PowerService;
import com.glch.spectrum.util.IDUtil;
import com.glch.spectrum.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class FreqController {

    private static JSONArray phaseList = null;  //阶段列表
    private static JSONArray opMissionList = null;  //任务列表
    private static JSONArray actionList = null; //行动列表

    @Value("${file}")
    private String path;

    @Autowired
    PowerService powerService;

    @Autowired
    FreqService freqService;


    @GetMapping("/test")
    public String test() {
        return "wz is ok!";
    }

    @ApiOperation(value = "制作频谱计划", notes = "解析信息作战计划与网络规划方案")
    @PostMapping("/freq")
    public Result makePlan(@Valid FreqPlan freqPlan, MultipartFile msgPlan, MultipartFile netPlan) throws IOException, ParseException {
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
        File netFile = new File(path,"net.json");
        if (!netFile.exists()) {
            netFile.createNewFile();
        }
        netPlan.transferTo(netFile);
        analyNetFile(netFile, freqPlan.getPlanId());  //网络规划方案中的保护频率列表

        return Result.success();
    }

    //解析网络规划方案
    private void analyNetFile(File file, String planId) throws IOException {

        String json = FileUtils.readFileToString(file, "UTF-8");
        JSONObject jsonObject = JSONObject.parseObject(json);

        JSONArray actions = (JSONArray) jsonObject.get("opCombatActionList");


        List<Frequency> freqs = new ArrayList<>();      //存放生成好的保护频率列表

        for (Object object : actions) {
            List<String> list = new ArrayList<>();
            JSONObject action = (JSONObject) object;
            JSONArray equips = (JSONArray) action.get("actionEquipAssign");
            String actionID = (String) action.get("actionID");
            String timeDifference = getTime(actionID);//获取与全局基准时间时间差
            for (Object obj : equips) {
                JSONObject equip = (JSONObject) obj;
                JSONArray connectList = (JSONArray) equip.get("connectList");
                for (Object o : connectList) {
                    JSONObject connect = (JSONObject) o;
                    list.add((String) connect.get("tx"));
                    list.add((String) connect.get("rx"));
                }
            }
            Set<String> set = new HashSet<>(list);  //去掉list中重复的频率
            for (String string : set) {
                Frequency frequency = new Frequency();  //将String作为频率上下限生成保护频率
                frequency.setFrequencyUpperLimit(Double.valueOf(string));
                frequency.setFrequencyLowerLimit(Double.valueOf(string));
                assert timeDifference != null;
                frequency.setPropFreqStartTime(frequency.getPropFreqStartTime().add(new BigDecimal(timeDifference)));
                frequency.setPropFreqEndTime(frequency.getPropFreqEndTime().add(new BigDecimal(timeDifference)));
                freqs.add(frequency);
                freqService.insertProtect(frequency, planId);  //将保护频率存入数据库
            }
        }
    }
    //获取行动基于全局基准时间的时间（开始为基于基准时间的）
    private String getTime(String actionID) {
        for(Object o : actionList){
            JSONObject action = (JSONObject) o;
            if (action.get("actionID").equals(actionID)){
                for (Object object : opMissionList) {
                    JSONObject mission = (JSONObject) object;
                    if (mission.get("opMissionID").equals(action.get("cAMissionID"))) {
                        for (Object obj : phaseList) {
                            JSONObject phase = (JSONObject) obj;
                            if (mission.get("operationPhaseID").equals(phase.get("phaseID"))) {
                                JSONObject jsonObject = (JSONObject) phase.get("timeBaseT");
                                return (String) jsonObject.get("timeDifference");//获取当前行动与全局基准时间差
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    //解析信息作战方案
    private void analyMsgFile(File file, String planId) throws IOException, ParseException {

        String json = FileUtils.readFileToString(file, "UTF-8");
        JSONObject jsonObject = JSONObject.parseObject(json);

        //获取编组列表
        JSONArray groupList = (JSONArray) jsonObject.get("opMissionGroupList");
        List<Group> groups = JSONArray.parseArray(groupList.toString(), Group.class);
        Command command = groupToCommand(groups);       //将编组转化为指挥所装备层级结构树

        //获取保护频率列表，要对单位进行处理成Mhz
        JSONArray protectFreqList = (JSONArray) jsonObject.get("protectFreqList");
        List<Frequency> protectFreqs = JSONArray.parseArray(protectFreqList.toString(), Frequency.class);

        for (Frequency frequency : protectFreqs) {
            frequency.setFrequencyID(IDUtil.getID(24));   //重新给一个ID，以免需要再次导入此文件，ID重复导致导入失败
            freqService.insertProtect(frequency, planId);
        }
//        plan.setProtectFreqList(protectFreqs);

        //获取全局基准时间
        JSONObject planAttribute = (JSONObject) jsonObject.get("planAttribute");
        JSONObject globalTimeBaseT = (JSONObject) planAttribute.get("globalTimeBaseT");//全局基准时间
        String astronomicalTime = (String) globalTimeBaseT.get("astronomicalTime");//天文时间

        //获取作战阶段列表
        JSONArray phaseList = (JSONArray) jsonObject.get("operationPhaseList");
        FreqController.phaseList = phaseList;

        //获取作战任务列表
        JSONArray opMissionList = (JSONArray) jsonObject.get("opMissionList");
        FreqController.opMissionList = opMissionList;

        //获取行动列表
        JSONArray actionList = (JSONArray) jsonObject.get("opCombatActionList");
        FreqController.actionList = actionList;
        List<Action> actions = JSONArray.parseArray(actionList.toString(), Action.class);

        for (Action action : actions) {

            List<Command> commands = new ArrayList<>();
            String phaseFlag = "";  //阶段ID标识
            String groupFlag = "";  //编组标识ID

            if (action.getActionEquipAssign() != null && !action.getActionEquipAssign().isEmpty()) {

                commands = action.getActionEquipAssign();//该行动有装备配属

                //获取任务对应的阶段ID
                for (Object object : opMissionList) {
                    JSONObject mission = (JSONObject) object;
                    if (mission.get("opMissionID").equals(action.getCAMissionID())) {
                        phaseFlag = (String) mission.get("operationPhaseID");
                        break;
                    }
                }
                //解析侦察频率
                if (action.getScoutFreqList() != null && action.getScoutFreqList().size() != 0) {
                    for (Command cmd : commands) {
                        List<Rectangle> rectangles = new ArrayList<>();
                        for (Frequency frequency : action.getScoutFreqList()) {     //侦察频率列表
                            Rectangle rectangle = new Rectangle();
                            //中心频率转装备工作频段上下限
                            getFreqScope(frequency.getCenterFrequency(), frequency.getFrequencyWidth(), rectangle, cmd);
                            //装备工作时间设置
                            getOpTime(phaseList, phaseFlag, rectangle, frequency, astronomicalTime);
                            rectangles.add(rectangle);
                        }
                        //根据装备类型设置频率
                        setEquipFreqType(cmd, rectangles, 0);
                    }
                }
                //解析干扰频率
                if (action.getJamFreqList() != null && action.getJamFreqList().size() != 0) {
                    for (Command cmd : commands) {
                        List<Rectangle> rectangles = new ArrayList<>();
                        for (Frequency frequency : action.getJamFreqList()) {     //侦察频率列表
                            Rectangle rectangle = new Rectangle();
                            //中心频率转装备工作频段上下限
                            getFreqScope(frequency.getCenterFrequency(), frequency.getFrequencyWidth(), rectangle, cmd);
                            //装备工作时间设置
                            getOpTime(phaseList, phaseFlag, rectangle, frequency, astronomicalTime);
                            rectangles.add(rectangle);
                        }
                        //根据装备类型设置频率
                        setEquipFreqType(cmd, rectangles, 1);
                    }
                }
            }
            setFreqToCommand(command, commands);    //编组装备结构树，带有侦察干扰频率
        }

    }

    //将编组转化为指挥所装备层级结构树
    private Command groupToCommand(List<Group> groups) {
        Group group = groups.get(0);
        //将编组中的指挥所群组解析
        Command cmd = group.getGroupEquipmentList().get(0);
        if (cmd.getCombatUnitID().equals(group.getOwnGradeCmdID())) {
            group.getGroupEquipmentList().remove(cmd);
            cmd.setEquipmentList(group.getGroupEquipmentList());
        }
        //将各个编组中的装备合并入上述指挥所cmd中
        for (int i = 1; i < groups.size(); i++) {
            Group grp = groups.get(i);
            for (Command command : cmd.getEquipmentList()) {
                if (command.getCombatUnitID().equals(grp.getOwnGradeCmdID())) {
                    command.setEquipmentList(grp.getGroupEquipmentList());
                    break;
                }
            }
        }
        return cmd;
    }

    //将行动中的装备（加了频率）替换到编组command中
    private void setFreqToCommand(Command command, List<Command> commands) {
        for (Command cmd : commands) {
            for (Command c : command.getEquipmentList()) {
                if (c.getCombatUnitID().equals(cmd.getCombatUnitID())) {
                    BeanUtils.copyProperties(cmd, c);
                    break;
                }
                if (c.getEquipmentList() != null && c.getEquipmentList().size() != 0) {
                    for (Command command1 : c.getEquipmentList()) {
                        if (command1.getCombatUnitID().equals(cmd.getCombatUnitID())) {
                            BeanUtils.copyProperties(cmd, command1);
                        }
                        break;
                    }
                }
            }
        }
    }

    //设置装备工作频率类型
    private void setEquipFreqType(Command equipment, List<Rectangle> rectangles, int f) {
        String flag = equipment.getEquipType().substring(0, 2);
        switch (flag) {
            case "02":
            case "05":
            case "06":
                if (f == 0) {
                    equipment.setRadarScoutFreq(rectangles);//雷达计划侦察频率
                }
                if (f == 1) {
                    equipment.setRadarJamFreq(rectangles);//雷达计划干扰频率
                }
                break;
            case "03":
                if (f == 0) {
                    equipment.setCommScoutFreq(rectangles);//通信计划侦察频率
                }
                if (f == 1) {
                    equipment.setCommJamFreq(rectangles);//通信计划干扰频率
                }
                break;
            default:
                System.out.println("装备类型错误");
                break;
        }
    }

    //获取装备工作频段上下限
    private void getFreqScope(Double center, Double width, Rectangle rectangle, Command equip) {

        //中心频率转上下限
        rectangle.setFrequencyUpperLimit(center + width + "");
        rectangle.setFrequencyLowerLimit(center - width + "");
        //还要与实际装备的性能做对比getWorkFreq
        Map<String, String> map = powerService.getPower(equip.getEquipType());
        if (map != null) {
            if (Double.parseDouble(map.get("frequencyLowerLimit")) > Double.parseDouble(rectangle.getFrequencyLowerLimit())) {
                rectangle.setFrequencyLowerLimit(map.get("frequencyLowerLimit"));
            }
            if (Double.parseDouble(map.get("frequencyUpperLimit")) < Double.parseDouble(rectangle.getFrequencyUpperLimit())) {
                rectangle.setFrequencyUpperLimit(map.get("frequencyUpperLimit"));
            }
        }
    }

    //时间转换
    private void getOpTime(JSONArray phaseList, String phaseFlag, Rectangle rectangle, Frequency frequency, String astronomicalTime) throws ParseException {
        for (Object o : phaseList) {
            JSONObject phase = (JSONObject) o;
            if (phase.get("phaseID").equals(phaseFlag)) {
                JSONObject timeBaseT = (JSONObject) phase.get("timeBaseT");//时间基准
                BigDecimal timeDifference = (BigDecimal) timeBaseT.get("timeDifference");//与全局时间基准时间差

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sdf.parse(astronomicalTime);//天文时间字符串转对象
                BigDecimal a = new BigDecimal(60000);
                date.setTime(date.getTime() + timeDifference.multiply(a).longValue());
                String phaseTime = sdf.format(date); //阶段实际基准时间
                //将装备相对时间转化为实际时间戳
                rectangle.setStartTime(date.getTime() + frequency.getPropFreqStartTime().multiply(a).longValue());
                rectangle.setEndTime(date.getTime() + frequency.getPropFreqEndTime().multiply(a).longValue());
            }
        }
    }
}
