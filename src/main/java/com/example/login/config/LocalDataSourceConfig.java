package com.example.login.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
        import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
    basePackages = "com.example.login.repository",
    entityManagerFactoryRef = "entityManagerFactory",
            transactionManagerRef = "transactionManager"
)
public class LocalDataSourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "app.datasource.local")
    public DataSourceProperties localDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "dataSource")
    @Primary
    public DataSource localDataSource() {
        return localDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean(name = "entityManagerFactory")
@Primary
public LocalContainerEntityManagerFactoryBean localEntityManagerFactory() {

    LocalContainerEntityManagerFactoryBean factoryBean =
            new LocalContainerEntityManagerFactoryBean();

    factoryBean.setDataSource(localDataSource());

    factoryBean.setPackagesToScan(
            "com.example.login.entity"
    );

    factoryBean.setJpaVendorAdapter(
            new HibernateJpaVendorAdapter()
    );

    factoryBean.setPersistenceUnitName("local");

    Map<String, Object> properties = new HashMap<>();

    properties.put(
            "hibernate.dialect",
            "org.hibernate.dialect.Oracle10gDialect"
    );

    properties.put(
            "hibernate.hbm2ddl.auto",
            "none"
    );

    properties.put(
            "hibernate.show_sql",
            true
    );

    factoryBean.setJpaPropertyMap(properties);

    return factoryBean;
}
    @Bean(name = "transactionManager")
    @Primary
    public PlatformTransactionManager localTransactionManager() {
        return new JpaTransactionManager(
                localEntityManagerFactory().getObject()
        );
    }
}