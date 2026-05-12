package com.traveler.common.db.config;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@AutoConfiguration
@ConditionalOnClass(EntityManagerFactory.class)
@ConditionalOnBean(DataSource.class)
@EnableJpaAuditing
@EntityScan(basePackages = "com.traveler")
@EnableJpaRepositories(basePackages = "com.traveler")
public class JpaConfig {}
