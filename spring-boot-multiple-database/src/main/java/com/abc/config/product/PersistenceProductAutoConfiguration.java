package com.abc.config.product;

import java.util.HashMap;

import javax.persistence.PersistenceUnit;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.abc.repo.product", entityManagerFactoryRef = "productEntityManager", transactionManagerRef = "productTransactionManager")
public class PersistenceProductAutoConfiguration {

	@Autowired
	private Environment environment;

	@Bean
	@ConfigurationProperties(prefix = "spring.second-datasource")
	public DataSource productDataSource() {

		return DataSourceBuilder.create().build();

	}

	@Bean
	public LocalContainerEntityManagerFactoryBean productEntityManager() {

		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryBean.setDataSource(productDataSource());
		entityManagerFactoryBean.setPackagesToScan("com.abc.entity.product");

		EclipseLinkJpaVendorAdapter eclipseLinkJpaVendorAdapter = new EclipseLinkJpaVendorAdapter();
		entityManagerFactoryBean.setJpaVendorAdapter(eclipseLinkJpaVendorAdapter);
		
		entityManagerFactoryBean.setPersistenceUnitName("product");

		HashMap<String, Object> hashMap = new HashMap();
		hashMap.put("spring.jpa.show-sql", environment.getProperty("spring.jpa.show-sql"));
		hashMap.put("eclipselink.weaving", "false");
		hashMap.put("hibernate.hbm2ddl.auto", "update");

		entityManagerFactoryBean.setJpaPropertyMap(hashMap);
		return entityManagerFactoryBean;

	}

	@Bean
	public PlatformTransactionManager productTransactionManager() {

		JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
		jpaTransactionManager.setEntityManagerFactory(productEntityManager().getObject());
		return jpaTransactionManager;

	}

}
