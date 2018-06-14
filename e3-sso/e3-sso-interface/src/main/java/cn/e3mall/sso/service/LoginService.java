package cn.e3mall.sso.service;

import cn.e3mall.common.utils.E3Result;

public interface LoginService {
	
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
	E3Result userLogin(String username,String password);
}
