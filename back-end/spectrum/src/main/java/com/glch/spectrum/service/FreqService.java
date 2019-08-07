package com.glch.spectrum.service;

import com.glch.spectrum.entities.FreqPlan;
import com.glch.spectrum.entities.msg.Frequency;

public interface FreqService {
    boolean insertPlan(FreqPlan freqPlan);
    boolean insertProtect(Frequency frequency, String planId);
    FreqPlan getPlan(String planId);
}
