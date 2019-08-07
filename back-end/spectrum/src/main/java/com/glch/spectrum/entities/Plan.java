package com.glch.spectrum.entities;

import com.glch.spectrum.entities.msg.Frequency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Plan {
    List<Command> commandList;              //频谱编组
    List<Frequency> protectFreqList;        //保护频率
}
