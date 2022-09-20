package com.example.crysto.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.JedisPooled;

@Configuration
@ConfigurationProperties(prefix = "redis")
public class RedisConfiguration {

	private String host;
	private Integer port;

	public RedisConfiguration() {
		super();
	}
	
	public RedisConfiguration(String host, Integer port) {
		super();
		this.host = host;
		this.port = port;
	}



	@Bean
	JedisPooled jedisPooled() {
		return new JedisPooled(host, port);
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	@Override
	public String toString() {
		return "RedisConfiguration [host=" + host + ", port=" + port + "]";
	}
}
