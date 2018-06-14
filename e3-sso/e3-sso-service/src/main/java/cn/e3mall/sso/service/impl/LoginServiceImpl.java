package cn.e3mall.sso.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;
import cn.e3mall.sso.service.LoginService;


/**
 * 用户登录处理
 * @author ymdhi
 *
 */
@Service
public class LoginServiceImpl implements LoginService {
	@Autowired
	private TbUserMapper tbUserMapper;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${REDIS_PRE}")
	private String REDIS_PRE;
	
	@Value("${EXPIRE_TIME}")
	private Integer EXPIRE_TIME;
	
	/**
	 * 业务逻辑
	 * 1、判断用户名和密码是否正确
	 * 2、如果不正确，返回登陆失败
	 * 3、正确，生成token
	 * 4、把用户信息写入redis,key是token,value是用户信息
	 * 5、设置session的过期时间
	 * 6、返回token，以便让表现层把token写入到cookie中，返回值是E3Result，其中包含token信息
	 * @param username
	 * @param password
	 * @return
	 */
	public E3Result userLogin(String username, String password) {
//		1、判断用户名和密码是否正确
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> userList = tbUserMapper.selectByExample(example);
		if(userList == null||userList.size() == 0){
			//返回登录失败
			return E3Result.build(400, "用户名或密码错误");
		}
		//取用户信息
		TbUser user = userList.get(0);
		
		//判断密码是否正确
		String passwordMd5 = DigestUtils.md5DigestAsHex(password.getBytes());
		if(!passwordMd5.equals(user.getPassword())){
//		2、如果不正确，返回登陆失败
			return E3Result.build(400, "用户名或密码错误");
		}
//		3、正确，生成token
		String token = UUID.randomUUID().toString();
//		4、把用户信息写入redis,key是token,value是用户信息
		user.setPassword(null);   //密码就不要保存在缓存中了
		
		jedisClient.set(REDIS_PRE+token, JsonUtils.objectToJson(user));
//		5、设置session的过期时间
		jedisClient.expire(REDIS_PRE+token, EXPIRE_TIME);
//		6、返回token，以便让表现层把token写入到cookie中，返回值是E3Result，其中包含token信息
		
		
		
		return E3Result.ok(token);
		
		
	}

}
