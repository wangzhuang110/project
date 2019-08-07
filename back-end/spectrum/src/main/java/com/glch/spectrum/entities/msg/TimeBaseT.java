package com.glch.spectrum.entities.msg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
/*
*  基准时间
*/
public class TimeBaseT {
    String timeBaseCode;                    //基准代号
    String timeBaseNote;                    //基准说明
    String timeUnit;                        //表示所描述时长的量纲
    Double timeDifference;                  //与全局基准时间差
}
