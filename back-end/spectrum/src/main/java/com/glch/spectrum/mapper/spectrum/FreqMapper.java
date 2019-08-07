package com.glch.spectrum.mapper.spectrum;

import com.glch.spectrum.entities.FreqPlan;
import com.glch.spectrum.entities.msg.Frequency;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FreqMapper {
    @Insert("insert into freq_plan (plan_id, plan_name, plan_maker, plan_make_time) values(#{planId},#{planName},#{planMaker},#{planMakeTime})")
    Boolean insertPlan(FreqPlan freqPlan);

    @Insert("insert into content (frequency_id, plan_id, frequency_upper_limit, frequency_lower_limit, prop_freq_start_time, prop_freq_end_time) " +
            "values(#{freq.frequencyID},#{planId},#{freq.frequencyUpperLimit},#{freq.frequencyLowerLimit},#{freq.propFreqStartTime},#{freq.propFreqEndTime})")
    Boolean insertProtect(@Param("freq") Frequency frequency, @Param("planId") String planId);

    @Select("select * from content where plan_id = #{planId}")
    List<Frequency> getFrequency(String planId);

    @Select("select * from freq_plan where plan_id = #{planId}")
    FreqPlan getPlanById(String planId);
}
