package com.glch.spectrum.entities;

import com.alibaba.fastjson.annotation.JSONField;
import com.glch.spectrum.entities.msg.Frequency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FrequencyPlan {
    @JSONField(ordinal = 1)
    private String planId;

    @JSONField(ordinal = 2, format = "yyyy-MM-dd HH:mm:ss")
    private Date resultTime;

    @JSONField(ordinal = 3)
    private List<Frequency> frequencyList;
}
