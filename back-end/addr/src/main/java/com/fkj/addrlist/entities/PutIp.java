package com.fkj.addrlist.entities;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class PutIp {
    @NotBlank
    @Pattern(regexp = "((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))",message = "oleIp格式不对")
    private String oldIp;              //IP

    @NotBlank
    @Pattern(regexp = "((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))",message = "newIp格式不对")
    private String newIp;              //IP

    @Max(value = 2,message = "flag为1或者2")
    @Min(value = 1,message = "flag为1或者2")
    @NotNull(message = "flag不能为null")
    private Integer flag;           //标志位
}
