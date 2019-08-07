package com.glch.spectrum.entities.msg;

import com.alibaba.fastjson.annotation.JSONField;
import com.glch.spectrum.util.IDUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
/*
*   频率
*/
public class Frequency {
    private String frequencyID = IDUtil.getID(24);                            //频率标识
    private String frequencyName = "保护频率" + IDUtil.getHashcode();       //频率名称
    private String frequencyType = "1";                                     //频率类型

    @JSONField(format = "0.00")
    private Double frequencyUpperLimit;                                     //频率上限
    private Double frequencyLowerLimit;                                     //频率下限
    private Double centerFrequency;                                         //中心频率
    private Double frequencyWidth;                                          //带宽
    private String frequencyUnit = "3";                                     //频率单位
    private BigDecimal propFreqStartTime;                                   //相对开始时间
    private BigDecimal propFreqEndTime;                                     //相对结束时间
    private String comment;

}
