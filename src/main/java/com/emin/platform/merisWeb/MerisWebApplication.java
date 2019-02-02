package com.emin.platform.merisWeb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

import com.emin.platform.merisWeb.config.MerisWebMvcConfig;


@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class MerisWebApplication {
	public static void main(String[] args) {
		SpringApplication.run(new Object[]{MerisWebApplication.class,MerisWebMvcConfig.class}, args);
	}
}
