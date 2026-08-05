package com.example.login.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
		basePackages = "com.example.login.it.repository",
		entityManagerFactoryRef = "itEntityManagerFactory",
		transactionManagerRef = "itTransactionManager"
)
public class ItDataSourceConfig {

	@Bean
	@ConfigurationProperties(prefix = "it.datasource")
	public DataSourceProperties itDataSourceProperties() {
		return new DataSourceProperties();
	}

	@ConfigurationProperties(prefix = "it.datasource")
	@Bean(name = "itDataSource")
	public DataSource itDataSource() {
		return itDataSourceProperties().initializeDataSourceBuilder().build();
	}

	@Bean(name = "itEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean itEntityManagerFactory() {
		LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
		factoryBean.setDataSource(itDataSource());
		factoryBean.setPackagesToScan("com.example.login.it.entity");
		factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		factoryBean.setPersistenceUnitName("it");
		return factoryBean;
	}

	@Bean(name = "itTransactionManager")
	public PlatformTransactionManager itTransactionManager() {
		return new JpaTransactionManager(itEntityManagerFactory().getObject());
	}

}
