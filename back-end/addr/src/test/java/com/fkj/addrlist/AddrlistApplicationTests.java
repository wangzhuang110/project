package com.fkj.addrlist;

import com.fkj.addrlist.entities.Equipment;
import com.fkj.addrlist.entities.SeatInfo;
import com.fkj.addrlist.service.UnitService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AddrlistApplicationTests {

    @Autowired
    DataSource dataSource;

    @Autowired
    UnitService unitService;

    @Test
    public void contextLoads() throws SQLException {
        System.out.println(dataSource.getClass());

        Connection collection = dataSource.getConnection();

        System.out.println(collection);
        collection.close();
    }

    @Test
    public void unitService() {
        List<Equipment> list = unitService.getEquipments();
        System.out.println(list);
    }
}
