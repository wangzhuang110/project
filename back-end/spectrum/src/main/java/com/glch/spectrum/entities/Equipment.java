package com.glch.spectrum.entities;

import com.glch.fkj398.vmfclient.vmf.base.Base;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Equipment{
    private String combatUnitID;                //作战单元编号
    private String equipModel;                  //装备型号内码
    private String equipType;                   //装备类型内码
    private String equipName;                   //装备名称
    private String frequencyType;               //频率类型，通过装备型号判断是雷达还是通信
    private String frequencyUpperLimit;         //频率上限
    private String frequencyLowerLimit;         //频率下限
    private Long startTime;                     //实际开始时间
    private Long endTime;                       //实际结束时间

}
