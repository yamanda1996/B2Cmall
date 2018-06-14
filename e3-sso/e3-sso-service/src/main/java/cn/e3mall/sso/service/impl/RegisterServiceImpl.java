package cn.e3mall.sso.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;
import cn.e3mall.sso.service.RegisterService;

/**
 * 用户注册处理Service
 * @author ymdhi
 *
 */
@Service
public class RegisterServiceImpl implements RegisterService {
	
	@Autowired
	private TbUserMapper tbUserMapper;
	
	
	public E3Result checkData(String param, int type) {
		//根据不同的type生成不同的查询条件
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		
		//1:用户名		2：电话号码		3：邮箱
		if(type == 1){
			
			criteria.andUsernameEqualTo(param);
			
		}else if(type == 2){
			criteria.andPhoneEqualTo(param);
		}else if(type == 3){
			criteria.andEmailEqualTo(param);
		}else{
			return E3Result.build(400, "数据类型错误");
		}
		
		//执行查询
		List<TbUser> userList = tbUserMapper.selectByExample(example);
		//判断结果中是否包含数据
		if(userList.size()>0 && userList != null){
			return E3Result.ok(false);
		}
		//如果有数据，返回false
		//没有数据返回true
		return E3Result.ok(true);
	}


	public E3Result register(TbUser tbUser) {
		//数据有效性进行校验
		if(StringUtils.isBlank(tbUser.getUsername()) || StringUtils.isBlank(tbUser.getPassword()) || StringUtils.isBlank(tbUser.getPhone())){
			return E3Result.build(400, "用户信息不完整，注册失败");
		}
		//校验用户名
		E3Result e3ResultUsername = checkData(tbUser.getUsername(), 1);
		if(!(boolean) e3ResultUsername.getData()){
			return E3Result.build(400, "用户名已被占用，注册失败");
		}
		//校验手机号
		E3Result e3ResultPhone = checkData(tbUser.getPhone(), 2);
		if(!(boolean) e3ResultPhone.getData()){
			return E3Result.build(400, "手机号已被占用，注册失败");
		}
		//补全tbUser
		tbUser.setCreated(new Date());
		tbUser.setUpdated(new Date());
		//对password密码进行md5加密
		String passwordMd5 = DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes());
		tbUser.setPassword(passwordMd5);
		//把用户数据插入数据库中
		tbUserMapper.insert(tbUser);
		//返回添加成功
		return E3Result.ok();
	}

}
