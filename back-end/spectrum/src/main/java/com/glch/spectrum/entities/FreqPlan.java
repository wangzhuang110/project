package com.glch.spectrum.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "计划基本信息")
public class FreqPlan {

    @ApiModelProperty(value = "频谱计划标识", dataType = "String", example = "前端不用填写")
    private String planId;

    @NotBlank
    @ApiModelProperty(value = "计划名称", dataType = "String", example = "频谱使用计划-01")
    private String planName;

    @NotBlank
    @ApiModelProperty(value = "计划制作人", dataType = "String", example = "李一")
    private String planMaker;

    @ApiModelProperty(value = "计划制作时间", dataType = "String", example = "前端不用填写")
    private String planMakeTime;
}
