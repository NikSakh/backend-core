package ru.mentee.power.crm.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "ru.mentee.power.crm")
@EnableJpaRepositories(basePackages = "ru.mentee.power.crm.jparepository")
@EntityScan(basePackages = "ru.mentee.power.crm.domain.jpa")
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
