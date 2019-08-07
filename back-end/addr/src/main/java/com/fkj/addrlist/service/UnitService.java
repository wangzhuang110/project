package com.fkj.addrlist.service;

import com.fkj.addrlist.entities.Equipment;

import java.util.List;

public interface UnitService {
    List<Equipment> getEquipments();        //获取编制中的装备列表并补充席位、IP、文电地址
}
