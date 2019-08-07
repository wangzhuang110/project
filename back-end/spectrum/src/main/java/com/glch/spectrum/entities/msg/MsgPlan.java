package com.glch.spectrum.entities.msg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
/*
*   信息作战计划
*/
public class MsgPlan {
    String planID;                          //计划ID
    PlanAttribute planAttribute;            //计划属性
    List<Phase> operationPhaseList;         //作战阶段
    List<Mission> opMissionList;            //作战任务
    List<Troop> opMissionGroupList;         //任务编组
    List<Action> opCombatActionList;        //作战行动
    List<Frequency> protectFreqList;        //保护频率
}
