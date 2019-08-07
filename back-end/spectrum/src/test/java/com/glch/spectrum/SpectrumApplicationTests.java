package com.glch.spectrum;

import com.glch.spectrum.util.IDUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpectrumApplicationTests {

	@Test
	public void contextLoads() throws InterruptedException {

		while (true){
			Thread.sleep(1000);
			System.out.println(IDUtil.getID(24));
		}
	}

}
