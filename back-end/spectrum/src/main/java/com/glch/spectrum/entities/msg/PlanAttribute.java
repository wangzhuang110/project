package com.glch.spectrum.entities.msg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
/*
*   计划属性
*/
public class PlanAttribute {
    String planName;                        //计划名称
    GlobalTimeBaseT globalTimeBaseT;        //全局基准时间
}
