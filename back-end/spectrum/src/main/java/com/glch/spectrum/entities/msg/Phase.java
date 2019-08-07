package com.glch.spectrum.entities.msg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
/*
*     计划阶段
*/
public class Phase {
    String phaseID;                     //阶段ID
    String phaseName;                   //阶段名称
    TimeBaseT timeBaseT;                //基准时间
}
