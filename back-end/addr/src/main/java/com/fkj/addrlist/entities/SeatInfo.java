package com.fkj.addrlist.entities;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "seatInfo")
public class SeatInfo {
    private List<UnitEquipment> equipmentList = new ArrayList<>();
}
