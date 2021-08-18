package com.netty;

import com.netty.echo.EchoClient;
import com.netty.echo.EchoServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.apache.tomcat.jni.Time.sleep;

@Slf4j
@SpringBootApplication
public class NettyApplication {

	public static void main(String[] args) {
		log.info("hi");

		SpringApplication.run(NettyApplication.class, args);
	}
}
