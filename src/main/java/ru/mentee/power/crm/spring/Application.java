package ru.mentee.power.crm.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "ru.mentee.power.crm")
@EnableFeignClients(basePackages = "ru.mentee.power.crm.spring.client")
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
