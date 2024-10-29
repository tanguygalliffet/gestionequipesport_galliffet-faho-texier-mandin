package com.example.web_galliffetmandintexierfaho;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.example.web_galliffetmandintexierfaho.entity") // Update if necessary
@EnableJpaRepositories(basePackages = "com.example.web_galliffetmandintexierfaho.repository") // Update if necessary
public class WebGalliffetMandinTexierFahoApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebGalliffetMandinTexierFahoApplication.class, args);
    }
}
