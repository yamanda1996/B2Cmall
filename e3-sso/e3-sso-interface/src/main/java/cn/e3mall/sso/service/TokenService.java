package cn.e3mall.sso.service;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbUser;

/**
 * 根据token查询用户信息
 * @author ymdhi
 *
 */
public interface TokenService {
	
	E3Result getUserByToken(String token);

}
