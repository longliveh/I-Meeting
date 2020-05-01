package com.example.auth.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
@MapperScan("com.example.auth.demo.mapper")
public class DemoApplication {

	public static void main(String[] args) {
		//System.setProperty("jna.library.path","D:\\Download\\ArcSoft_ArcFace_Java_Windows_x64_V2.0\\libs\\WIN64");
		SpringApplication.run(DemoApplication.class, args);
	}
}
