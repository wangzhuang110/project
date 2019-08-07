package com.glch.spectrum.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Command {

    private String combatUnitID;              //作战单元编号
    private String equipName;                 //装备名称
    private String equipmentSn;               //装备编号
    private String equipType;                 //装备类型内码

    private List<Rectangle> radarScoutFreq;   //雷达侦察频率
    private List<Rectangle> radarJamFreq;     //雷达干扰频率
    private List<Rectangle> commScoutFreq;    //通信侦察频率
    private List<Rectangle> commJamFreq;      //通信干扰频率
    private List<Command> EquipmentList;      //子节点
}
