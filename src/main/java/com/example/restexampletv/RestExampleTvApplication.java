package com.example.restexampletv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication
@EnableConfigurationProperties({
        FileStorageProperties.class
})
public class RestExampleTvApplication  {

    public static void main(String[] args) {
        SpringApplication.run(RestExampleTvApplication.class, args);
    }


}
