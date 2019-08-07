package com.glch.spectrum.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@ApiModel(value = "Troop",description = "任务编组信息")
public class Troop {
    @ApiModelProperty(value = "任务部队标识",dataType = "String",example = "878422575687783597824617")
    private String missionTroopID;           //任务部队标识

    @ApiModelProperty(value = "任务部队名称",dataType = "String",example = "多功能机载雷达干扰群组")
    private String missionTroopName;         //任务部队名称

    @ApiModelProperty(value = "任务部队级别",dataType = "String",example = "11")
    private String missionTroopLevel;        //任务部队级别

    @ApiModelProperty(value = "本级指挥机构代码",dataType = "String",example = "EQ000000000000000121")
    private String ownGradeCmdID;            //本级指挥机构代码

    @ApiModelProperty(value = "本级指挥机构名称",dataType = "String",example = "多功能机载雷达干扰连指挥所")
    private String ownGradeCmdName;          //本级指挥机构名称

    @ApiModelProperty(value = "上级指挥机构代码",dataType = "String",example = "EQ000000000000000121")
    private String upperCommandID;           //上级指挥机构代码

    @ApiModelProperty(value = "编组装备列表",dataType = "List<Equip>")
    private List<Command> groupEquipmentList;  //编组装备列表
}
