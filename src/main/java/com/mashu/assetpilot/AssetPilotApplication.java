package com.mashu.assetpilot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class})
public class AssetPilotApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssetPilotApplication.class, args);
    }

}
