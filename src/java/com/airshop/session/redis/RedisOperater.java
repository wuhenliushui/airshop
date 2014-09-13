package com.airshop.session.redis;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import com.airshop.utils.ObjectUtils;

public class RedisOperater<T>{

	private  Log logger = LogFactory.getLog(this.getClass());
	
	private final static String REDIS_SESSION_PREFIX = "";

	@Autowired
	private RedisTemplate<String, HttpSession> redisTemplate;
	
	public void set(final T session, final String key) {
		logger.info("invoke save , key :  "+ key);
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.set(
						redisTemplate.getStringSerializer().serialize(REDIS_SESSION_PREFIX+key)
						, ObjectUtils.serialize(session));
				return null;
			}		
		});
	}
	
	public T get(final String key) {
		logger.info("invoke get , key :  "+ key);
		return redisTemplate.execute(new RedisCallback<T>() {
			@Override
			public T doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[] bytekey = redisTemplate.getStringSerializer().serialize(  
						REDIS_SESSION_PREFIX+key);
				 if (connection.exists(bytekey)) {  
		               byte[] value = connection.get(bytekey);  
		               return ObjectUtils.unserialize(value);  
		            }
				return null;
			}			
		});
	}
	
	public void delete(final String key) {
		logger.info("invoke delete , key  :  "+ key);
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection) {
				return connection.del(
						redisTemplate.getStringSerializer().serialize(REDIS_SESSION_PREFIX+key));
			}
		});
	}
	
}
