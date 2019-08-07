package com.fkj.addrlist.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnitEquipment {
    private String name;
    private String equipmentTypeId;
    private List<Seat> seats = new ArrayList<>();
}
