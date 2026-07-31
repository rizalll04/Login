package com.example.login.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.login.adm.repository",
        entityManagerFactoryRef = "admEntityManagerFactory",
        transactionManagerRef = "admTransactionManager"
)
public class AdmDataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "adm.datasource")
    public DataSourceProperties admDataSourceProperties() {
        return new DataSourceProperties();
    }

    @ConfigurationProperties(prefix = "adm.datasource")
    @Bean(name = "admDataSource")
    public DataSource admDataSource() {
        return admDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean(name = "admEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean admEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(admDataSource());
        factoryBean.setPackagesToScan("com.example.login.adm.entity");
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factoryBean.setPersistenceUnitName("adm");
        return factoryBean;
    }

    @Bean(name = "admTransactionManager")
    public PlatformTransactionManager admTransactionManager() {
        return new JpaTransactionManager(admEntityManagerFactory().getObject());
    }

}