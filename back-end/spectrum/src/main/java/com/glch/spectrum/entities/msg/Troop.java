package com.glch.spectrum.entities.msg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
/*
*   任务编组
*/
public class Troop {
    String missionTroopID;              //任务部队标识
    String ownGradeCmdID;               //本级指挥机构代码
    String ownGradeCmdName;             //本级指挥机构名称
    String upperCommandID;              //上级指挥机构标识
    List<Equipment> groupEquipmentList; //编组装备列表
}
