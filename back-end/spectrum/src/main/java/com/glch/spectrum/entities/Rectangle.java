package com.glch.spectrum.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rectangle {                        //频率点或者频率方形
    private String frequencyUpperLimit;         //频率上限
    private String frequencyLowerLimit;         //频率下限
    private Long startTime;                     //实际开始时间
    private Long endTime;                       //实际结束时间
}
