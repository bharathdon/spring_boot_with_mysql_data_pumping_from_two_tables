package com.abc.config.user;

import java.util.HashMap;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
//@PropertySource(value =  "classpath:persistence-multiple-db-boot.properties" )
@EnableJpaRepositories(basePackages = "com.abc.repo.user", entityManagerFactoryRef = "userEntityManager", transactionManagerRef = "userTransactionManager")
public class PersistenceUserAutoConfiguration {

	@Autowired
	private Environment environment;

	@Primary
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource1")
	public DataSource userDataSource() {

		return DataSourceBuilder.create().build();
	}

	@Primary
	@Bean
	public LocalContainerEntityManagerFactoryBean userEntityManager() {

		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryBean.setDataSource(userDataSource());
		entityManagerFactoryBean.setPackagesToScan(new String[]{"com.abc.entity.user"});

		entityManagerFactoryBean.setPersistenceUnitName("user");
		
		EclipseLinkJpaVendorAdapter jpaVendorAdapter = new EclipseLinkJpaVendorAdapter();
		entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);

		HashMap<String, Object> hashMap = new HashMap();
		hashMap.put("spring.jpa.show-sql", "true");
		hashMap.put("eclipselink.weaving", "false");
		hashMap.put("hibernate.hbm2ddl.auto", "update");

		entityManagerFactoryBean.setJpaPropertyMap(hashMap);

		return entityManagerFactoryBean;

	}

	@Primary
	@Bean
	public PlatformTransactionManager userTransactionManager() {
		

		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(userEntityManager().getObject());

		return transactionManager;

	}

}
