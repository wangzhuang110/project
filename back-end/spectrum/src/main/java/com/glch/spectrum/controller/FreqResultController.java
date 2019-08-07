package com.glch.spectrum.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.glch.spectrum.entities.*;
import com.glch.spectrum.entities.msg.Frequency;
import com.glch.spectrum.service.PowerService;
import com.glch.spectrum.util.Result;
import io.swagger.annotations.Api;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.spring.web.json.Json;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class FreqResultController {
    @Autowired
    PowerService powerService;
    @Value("${file}")
    private String path;

    @PostMapping("/result")
    public Result getFreqResult(MultipartFile plan) throws IOException {

//        plan.transferTo(new File(path, "planData.json"));
        String json = FileUtils.readFileToString(new File(path, "planData.json"), "UTF-8");
        JSONObject jsonObject = JSONObject.parseObject(json);

        //频谱计划
        FrequencyPlan frequencyPlan = new FrequencyPlan();

        //获取计划ID
        String planId = jsonObject.getString("planID");
        frequencyPlan.setPlanId(planId);

        //获取编组列表
        JSONArray groupList = (JSONArray) jsonObject.get("opMissionGroupList");
        List<Group> groups = JSONArray.parseArray(groupList.toString(), Group.class);

        //获取保护频率列表
        JSONArray protectFreqList = (JSONArray) jsonObject.get("protectFreqList");
        List<Frequency> protectFreqs = JSONArray.parseArray(protectFreqList.toString(), Frequency.class);

        //获取行动列表
        JSONArray actionList = (JSONArray) jsonObject.get("opCombatActionList");
//        for (int i = 0;i < actionList.size();i++){
//            JSONObject test = (JSONObject) actionList.get(i);
//            Action action1 = JSONObject.parseObject(test.toString(), Action.class);
//        }
        List<Action> actions = JSONArray.parseArray(actionList.toString(), Action.class);

        //获取侦察干扰频率列表
        List<Frequency> frequencyList = new ArrayList<>();

        for (Action action:actions) {
            frequencyList.addAll(action.getScoutFreqList());
            frequencyList.addAll(action.getJamFreqList());
        }


        frequencyList.addAll(protectFreqs);

        //设置计划中的频率列表
        frequencyPlan.setFrequencyList(frequencyList);

        //设置完成时间为生成文件时间
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date = new Date();
//        String resultTime = sdf.format(date);
        Date date = new Date();
        frequencyPlan.setResultTime(date);

        String s = JSON.toJSONString(frequencyPlan, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
        System.out.println(s);
        String jsonString = s.replace("\n", "\r\n");
        File file = new File(path, "frequency.json");
        if (!file.exists()) {
            file.mkdirs();
        }
        FileUtils.writeStringToFile(file, jsonString, "UTF-8", false);
//        String jsonString = JsonFormatTool.formatJson(testS);
////        Writer writer = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
////        writer.write(jsonString);
////        writer.flush();
////        writer.close();
        return Result.success(frequencyPlan);
    }
}
