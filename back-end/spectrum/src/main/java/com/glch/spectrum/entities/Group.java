package com.glch.spectrum.entities;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Troop",description = "任务编组信息")
public class Group {
    private String  missionTroopID;              //任务部队标识
    private String  missionTroopName;            //任务部队名称
    private String  missionTroopLevel;           //任务部队级别
    private String  ownGradeCmdID;               //本级指挥机构代码
    private String  ownGradeCmdName;             //本级指挥机构名称
    private String  upperCommandID;              //上级指挥机构代码
    private List<Command> groupEquipmentList;    //编组装备列表
}
