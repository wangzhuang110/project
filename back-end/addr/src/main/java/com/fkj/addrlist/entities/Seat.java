package com.fkj.addrlist.entities;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Seat {
    private String name;                //名称
    private String seatCode;            //席位编码
}
