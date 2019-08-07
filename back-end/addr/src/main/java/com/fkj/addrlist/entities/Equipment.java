package com.fkj.addrlist.entities;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "装备信息")
public class Equipment {
    @JSONField(ordinal = 1)
    @ApiModelProperty(value = "作战单元编号", dataType = "String", example = "10205")
    private String combatUnitID;

    @JSONField(ordinal = 2)
    @ApiModelProperty(value = "装备名称", dataType = "String", example = "团指挥所")
    private String name;

    @JSONField(ordinal = 3)
    @ApiModelProperty(value = "装备编号", dataType = "String", example = "010205")
    private String equipmentSn;

    @JSONField(ordinal = 4)
    @ApiModelProperty(value = "装备类型内码", dataType = "String", example = "0102")
    private String equipmentTypeId;


    @JSONField(ordinal = 5)
    @ApiModelProperty(value = "车号", dataType = "String", example = "1")
    private String codeName;

    @JSONField(ordinal = 6)
    @ApiModelProperty(value = "IP地址", dataType = "String", example = "192.168.1.2")
    private String ip;

    @JSONField(ordinal = 7)
    @ApiModelProperty(value = "文电地址", dataType = "String", example = "192.168.1.2")
    private String telAddress;                              //文电地址

    @JSONField(ordinal = 8)
    private List<Seat> seats = new ArrayList<>();           //席位
    @JSONField(ordinal = 9)
    private List<Equipment> children = new ArrayList<>();   //下级指挥机构及装备


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
//        if (! k.equals(o)) return false;
        Equipment equipment = (Equipment) o;
        //不能比较combatUnitID,为null String比较会报false
        return name.equals(equipment.name) &&
                equipmentSn.equals(equipment.equipmentSn) &&
                equipmentTypeId.equals(equipment.equipmentTypeId);
    }


}
