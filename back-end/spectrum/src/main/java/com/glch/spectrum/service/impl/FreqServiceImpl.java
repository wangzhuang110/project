package com.glch.spectrum.service.impl;

import com.glch.spectrum.entities.FreqPlan;
import com.glch.spectrum.entities.msg.Frequency;
import com.glch.spectrum.mapper.spectrum.FreqMapper;
import com.glch.spectrum.service.FreqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FreqServiceImpl implements FreqService {

    @Autowired
    FreqMapper freqMapper;

    @Override
    public boolean insertPlan(FreqPlan freqPlan) {
        return freqMapper.insertPlan(freqPlan);
    }

    @Override
    public boolean insertProtect(Frequency frequency, String planId) {
        return freqMapper.insertProtect(frequency, planId);
    }

    @Override
    public FreqPlan getPlan(String planId) {
        return freqMapper.getPlanById(planId);
    }
}
