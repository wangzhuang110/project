package com.fkj.addrlist.service.Impl;

import com.alibaba.fastjson.JSONArray;
import com.fkj.addrlist.entities.Equipment;
import com.fkj.addrlist.service.FileService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    @Value("${file.unitPath}")
    private String unitPath;
    @Value("${file.groupPath}")
    private String groupPath;

    @Override
    public List<Equipment> getBackup() throws IOException {
        String json = FileUtils.readFileToString(new File(unitPath, "backup.txl"), "UTF-8");
        return JSONArray.parseArray(json,Equipment.class);
    }

    @Override
    public List<Equipment> getUnit() throws IOException {
        String json = FileUtils.readFileToString(new File(unitPath, "unit.txl"), "UTF-8");
        return JSONArray.parseArray(json,Equipment.class);
    }

    @Override
    public List<Equipment> getGroup() throws IOException {
        String json = FileUtils.readFileToString(new File(groupPath, "group.txl"), "UTF-8");
        return JSONArray.parseArray(json, Equipment.class);
    }
}
