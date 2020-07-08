package com.example.shopping;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

@SpringBootApplication

/*
 * Chú ý: Spring Boot mặc định sẽ tự động cấu hình JPA, và tạo ra các Spring
 * BEAN liên quan tới JPA, các tự động cấu hình này của Spring Boot bao gồm:
 * DataSourceAutoConfiguration 
 * DataSourceTransactionManagerAutoConfiguration
 * HibernateJpaAutoConfiguration
 *  Mục đích trong ứng dụng này chúng ta sẽ sử dụng
 * Hibernate, vì vậy chúng ta cần vô hiệu hóa các cấu hình tự động nói trên của
 * Spring Boot.
 */

@EnableAutoConfiguration(exclude = { //  
        DataSourceAutoConfiguration.class, //
        DataSourceTransactionManagerAutoConfiguration.class, //
        HibernateJpaAutoConfiguration.class })
public class ShoppingApplication {
	
	@Autowired
    private Environment env;
	
	public static void main(String[] args) {
		SpringApplication.run(ShoppingApplication.class, args);
	}
		
	
	//cấu hình các Spring BEAN cần thiết cho Hibernate
	
		@Bean(name = "dataSource")
	    public DataSource getDataSource() {
	        DriverManagerDataSource dataSource = new DriverManagerDataSource();
	 
	        // See: application.properties
	        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
	        dataSource.setUrl(env.getProperty("spring.datasource.url"));
	        dataSource.setUsername(env.getProperty("spring.datasource.username"));
	        dataSource.setPassword(env.getProperty("spring.datasource.password"));
	 
	        System.out.println("## getDataSource: " + dataSource);
	 
	        return dataSource;
	    }
	 
	    @Autowired
	    @Bean(name = "sessionFactory")
	    public SessionFactory getSessionFactory(DataSource dataSource) throws Exception {
	        Properties properties = new Properties();
	 
	        // See: application.properties  
	        properties.put("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
	        properties.put("hibernate.show_sql", env.getProperty("spring.jpa.show-sql"));
	        properties.put("current_session_context_class", //
	                env.getProperty("spring.jpa.properties.hibernate.current_session_context_class"));
	 
	        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
	 
	        // Package contain entity classes
	        factoryBean.setPackagesToScan(new String[] { "" });
	        factoryBean.setDataSource(dataSource);
	        factoryBean.setHibernateProperties(properties);
	        factoryBean.afterPropertiesSet();
	        //
	        SessionFactory sf = factoryBean.getObject();
	        System.out.println("## getSessionFactory: " + sf);
	        return sf;
	    }
	 
	    @Autowired
	    @Bean(name = "transactionManager")
	    public HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory) {
	        HibernateTransactionManager transactionManager = new HibernateTransactionManager(sessionFactory);
	 
	        return transactionManager;
	    }
	
}
