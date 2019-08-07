package com.glch.spectrum.mapper.power;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface PowerMapper {
    @Select("select frequencyUpperLimit,frequencyLowerLimit from freq where equipType = #{equipType}")
    Map<String, String> getPower(String equipType);
}
