package com.fkj.addrlist.service;

import com.fkj.addrlist.entities.Equipment;

import java.util.List;

public interface GroupService {
    List<String> getPlanList();                     //获取编组表中的计划标识list
    String getPlanName(String planId);              //根据计划标识获取计划名称
    String getPlanId(String planName);              //根据计划名称获取计划标识
    String getTopCode(String planId);               //获取最高级指挥所的代码
    Equipment getCommand(String ownCmdCode);          //获取指挥所信息
    List<String> getCodeList(String upperCmdCode);  //获取某个指挥所的下属指挥机构代码列表
    List<Equipment> getEquipments(String groupId,String ownCmdCode);    //获取指挥所装备列表
    Equipment getAddr(String planName);                                   //获取通信录
}
