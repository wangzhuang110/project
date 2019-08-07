package com.fkj.addrlist;


import com.bfd.overseas.uop.acm.client.EnableACMClient;
import com.bfd.overseas.uop.mcs.client.EnableMCSClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import com.glch.fkj398.msgservice.handler.netty.StartupEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableMCSClient
@EnableACMClient
@SpringBootApplication
@EnableSwagger2
@EnableEurekaClient
public class AddrlistApplication {

	public static void main(String[] args) {
		SpringApplication.run(AddrlistApplication.class, args);
		new StartupEvent().receiveUdpdata(8309);//实时
	}
	//实时
//	@Component
//	public class ReceiveRunner implements CommandLineRunner {
//		@Override
//		public void run(String... args) {
//			Thread receiveThread = new Thread(new ReceiveThread(), "ReceiveThread");
//			receiveThread.start();
//		}
//	}
//
//	public class ReceiveThread implements Runnable {
//		public void run() {
//			while (true) {
//				try {
//					while (MsgCallBackImpl.vectorBuffer.size() > 0) {
//						//接收消息
//						byte[] bytes = (byte[]) MsgCallBackImpl.vectorBuffer.remove(0);
//						System.out.println("接收到数据：" + new String(bytes, "UTF-8"));
//					}
//				} catch (Exception e) {
//					e.getMessage();
//				}
//			}
//		}
//	}
	//socket io
	@Bean
	public SocketIOServer socketIOServer() {
		com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
		config.setPort(5499);
		final SocketIOServer server = new SocketIOServer(config);
		return server;
	}

	@Bean
	public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
		return new SpringAnnotationScanner(socketServer);
	}
}
