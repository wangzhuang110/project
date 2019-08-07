package com.glch.spectrum.entities;

import com.glch.spectrum.entities.msg.Frequency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Action {                                               //行动
    private String cAMissionID;                                     //任务标识
    private String actionID;                                        //行动标识
    private double actionStartTime;                                 //行动发起时间
    private double actionEndTime;                                   //行动结束时间
    private List<Frequency> scoutFreqList = new ArrayList<>();      //侦察频率
    private List<Frequency> jamFreqList = new ArrayList<>();        //干扰频率
    private List<Command> actionEquipAssign;                        //装备配属
}
