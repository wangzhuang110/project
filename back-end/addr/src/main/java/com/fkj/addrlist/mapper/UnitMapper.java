package com.fkj.addrlist.mapper;

import com.fkj.addrlist.entities.Equipment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UnitMapper {

    @Select("select equipment_sn,equipment.equipment_type_id,code_name,name from equipment left OUTER JOIN s_equipment_type ON equipment.equipment_type_id = s_equipment_type.equipment_type_id ORDER BY equipment_sn")
    List<Equipment> getEquipments();

}
