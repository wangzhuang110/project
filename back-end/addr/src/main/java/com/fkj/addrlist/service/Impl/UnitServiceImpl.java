package com.fkj.addrlist.service.Impl;

import com.fkj.addrlist.entities.Equipment;
import com.fkj.addrlist.entities.Seat;
import com.fkj.addrlist.entities.SeatInfo;
import com.fkj.addrlist.entities.UnitEquipment;
import com.fkj.addrlist.mapper.UnitMapper;
import com.fkj.addrlist.service.UnitService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UnitServiceImpl implements UnitService {

    @Autowired
    UnitMapper unitMapper;

    @Autowired
    SeatInfo seatInfo;

    @Override
    public List<Equipment> getEquipments() {

        List<Equipment> list = unitMapper.getEquipments();

        if (list != null && list.size() != 0) {
            for (Equipment equip : list) {
                equip.setIp(setIP(equip.getCodeName()));        //用车号生成IP
                equip.setTelAddress(equip.getIp());             //文电地址 == IP
                equip.setCombatUnitID(equip.getEquipmentSn());  //作战单元编号 == 装备编号
                fillSeat(equip);                                //补充装备席位信息
            }
        }
        return list;
    }

    //  根据装备类型内码补充装备席位信息
    private void fillSeat(Equipment equip) {
        for (UnitEquipment equipment : seatInfo.getEquipmentList()) {
            if (equipment.getEquipmentTypeId().equals(equip.getEquipmentTypeId())) {
                equip.setSeats(equipment.getSeats());
            }
        }
    }

    //  IP生成规则
    private String setIP(String code) {
        return "10.20." + code + ".2";
    }
}
