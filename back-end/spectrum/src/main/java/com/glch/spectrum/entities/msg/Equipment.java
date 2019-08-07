package com.glch.spectrum.entities.msg;

import com.glch.spectrum.entities.Rectangle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
/*
 *   装备
 * */
public class Equipment {
    String combatUnitID;                                 //作战单元编号
    String equipType;                                    //装备类型内码
    String equipName;                                    //装备名称

    List<Rectangle> radarScoutFreq = new ArrayList<>();   //雷达侦察频率
    List<Rectangle> radarJamFreq = new ArrayList<>();     //雷达干扰频率
    List<Rectangle> commScoutFreq = new ArrayList<>();    //通信侦察频率
    List<Rectangle> commJamFreq = new ArrayList<>();      //通信干扰频率
    List<Equipment> EquipmentList = new ArrayList<>();    //子节点

    public void setRectangle(Equipment equipment){
        radarJamFreq = equipment.radarJamFreq;
        radarScoutFreq = equipment.radarScoutFreq;
        commScoutFreq = equipment.commScoutFreq;
        commJamFreq = equipment.commJamFreq;
    }
}
