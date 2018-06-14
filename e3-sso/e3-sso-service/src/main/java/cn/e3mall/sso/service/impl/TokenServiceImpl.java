package cn.e3mall.sso.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.json.JSON;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;

/**
 *根据token取用户信息 
 * @author ymdhi
 *
 */

@Service
public class TokenServiceImpl implements TokenService {
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${REDIS_PRE}")
	private String REDIS_PRE;
	
	@Value("${EXPIRE_TIME}")
	private Integer EXPIRE_TIME;
	
	public E3Result getUserByToken(String token) {
		//根据token从redis中查询用户信息
		String userInfo = jedisClient.get(REDIS_PRE+token);
		if(StringUtils.isBlank(userInfo)){
			//如果取不到用户信息，说明登录过期，返回登录过期
			return E3Result.build(400, "登录已过期，请重新登录");
			
		}
		
		//如果取到用户信息，更新token的过期时间
		jedisClient.expire(REDIS_PRE+token, EXPIRE_TIME);
		//将字符串转化为用户对象
		TbUser tbUser = JsonUtils.jsonToPojo(userInfo, TbUser.class);
		//返回
		return E3Result.ok(tbUser);
	}

	
}
