package io.example.app.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ConfigurationProperties("spring.datasource")
public class Config {
	
	Integer corePoolSize = 500;
	Integer maxThreadPool = 1000;
	long threadKeepAliveTime = 1;

	
	@Autowired
	DataSource dataSource;

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public ExecutorService executorService() {
		ExecutorService executorService = new ThreadPoolExecutor(corePoolSize,
				maxThreadPool, threadKeepAliveTime, TimeUnit.MINUTES,
				new LinkedBlockingQueue<Runnable>());
		return executorService;
	}

}
