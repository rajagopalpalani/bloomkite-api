package com.sowisetech.advisor.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class DBConfig {

	@Autowired
	private Environment env;

	
	@Primary
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
		dataSource.setUrl(env.getProperty("spring.datasource.url"));
		dataSource.setUsername(env.getProperty("spring.datasource.username"));
		dataSource.setPassword(env.getProperty("spring.datasource.password"));
		return dataSource;

	}

	@Bean
	public DataSource authDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("spring.auth.datasource.driver-class-name"));
		dataSource.setUrl(env.getProperty("spring.auth.datasource.url"));
		dataSource.setUsername(env.getProperty("spring.auth.datasource.username"));
		dataSource.setPassword(env.getProperty("spring.auth.datasource.password"));
		return dataSource;
	}
	
	@Bean
    @Qualifier("sowiseJdbcTemplate")
	public JdbcTemplate jdbcTemplate(@Qualifier("dataSource") DataSource ds) {
		return new JdbcTemplate(ds);
	}
	
	@Bean
    @Qualifier("authJdbcTemplate")
	public JdbcTemplate jdbcTemplateAuth(@Qualifier("authDataSource") DataSource ds) {
		return new JdbcTemplate(ds);
	}
}
