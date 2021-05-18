package com.itsm.dranswer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class DranswerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DranswerApplication.class, args);
    }

}
