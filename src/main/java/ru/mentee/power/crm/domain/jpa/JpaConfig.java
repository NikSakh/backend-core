package ru.mentee.power.crm.jpa;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "ru.mentee.power.crm.jparepository")
@EntityScan(basePackages = "ru.mentee.power.crm.domain.jpa")
public class JpaConfig {
}
