package com.smh.InteractiveArticlePlatformWebService.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableJpaAuditing
@Configuration
public class PersistenceConfiguration {

}

