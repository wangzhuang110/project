package com.glch.spectrum.entities.msg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
/*
*   全局基准时间
*/
public class GlobalTimeBaseT {
    String globaTimeBaseCode;           //全局时间基准代号
    String astronomicalTime;            //天文时间
    String combatTime;                  //作战时间
}
