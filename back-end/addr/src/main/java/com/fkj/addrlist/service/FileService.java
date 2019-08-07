package com.fkj.addrlist.service;

import com.fkj.addrlist.entities.Equipment;

import java.io.IOException;
import java.util.List;

public interface FileService {
     List<Equipment> getBackup() throws IOException;
     List<Equipment> getUnit() throws IOException;
     List<Equipment> getGroup() throws IOException;
}
