package com.glch.spectrum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableFeignClients
public class SpectrumApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpectrumApplication.class, args);
	}

}
