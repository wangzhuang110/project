package com.glch.spectrum.entities.msg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
/*
*   作战行动
*/
public class Action {
    String cAMissionID;                 //行动所属任务ID
    String actionID;                    //行动ID
    String actionStartTime;             //行动发起时间
    String actionEndTime;               //行动结束时间
    List<Frequency> scoutFreqList;      //侦察频率
    List<Frequency> jamFreqList;        //干扰频率
    List<Equipment> actionEquipAssign;  //装备配属
}
