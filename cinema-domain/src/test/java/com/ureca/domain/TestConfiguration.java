package com.ureca.domain;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.ureca.domain.repository")
@EntityScan(basePackages = "com.ureca.domain.entity")
public class TestConfiguration {
}