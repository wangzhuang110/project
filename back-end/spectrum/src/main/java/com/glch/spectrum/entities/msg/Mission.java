package com.glch.spectrum.entities.msg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
/*
*   作战任务
*/
public class Mission {
    String operationPhaseID;                //任务所属阶段ID
    String opMissionID;                     //任务ID
}
