package com.jjs.miaosha;

import com.jjs.miaosha.redis.RedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@SpringBootApplication
public class MiaoshaApplication {

	@Autowired
	private RedisConfig redisConfig;

	public static void main(String[] args) {
		SpringApplication.run(MiaoshaApplication.class, args);
	}

	@Bean
	public JedisPool getJedisPool() {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxIdle(redisConfig.getPoolMaxIdle());
		config.setMaxTotal(redisConfig.getPoolMaxTotal());
		config.setMaxWaitMillis(redisConfig.getPoolMaxWait());
		return new JedisPool(config, redisConfig.getHost(), redisConfig.getPort(),
				redisConfig.getTimeout() * 1000);
	}
}
