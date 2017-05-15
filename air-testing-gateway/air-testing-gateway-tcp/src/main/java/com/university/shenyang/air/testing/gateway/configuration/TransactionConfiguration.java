package com.university.shenyang.air.testing.gateway.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * 开启spring boot的事务管理功能
 * @author Administrator
 *
 */
@EnableTransactionManagement
public class TransactionConfiguration {
	
	@Bean
	public DataSourceTransactionManager transactionManager(DataSource dataSource) throws Exception {
		return new DataSourceTransactionManager(dataSource);
	}
}
