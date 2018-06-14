package cn.e3mall.sso.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;


/**
 * 根据token查询用户信息controller
 * @author ymdhi
 *
 */
@Controller
public class TokenController {
	
	@Autowired
	private TokenService tokenService;
	
	@RequestMapping(value="/user/token/{token}",produces="application/json;charset=utf-8")
	@ResponseBody
	public String getUserByToken(@PathVariable String token,String callback){    //如果请求参数中有callback，则说明这是一个jsonp请求
		E3Result e3Result = tokenService.getUserByToken(token);
		//响应结果之前判断是否为jsonp请求
		if(StringUtils.isNotBlank(callback)){
			//把结果封装成一个js语句响应
			return callback + "(" +JsonUtils.objectToJson(e3Result) + ");";
		}
		return JsonUtils.objectToJson(e3Result);
	}
	
	
	
	//第二种方法使用jsonp
//	@RequestMapping(value="/user/token/{token}")
//	@ResponseBody
//	public Object getUserByToken(@PathVariable String token,String callback){    //如果请求参数中有callback，则说明这是一个jsonp请求
//		E3Result e3Result = tokenService.getUserByToken(token);
//		//响应结果之前判断是否为jsonp请求
//		if(StringUtils.isNotBlank(callback)){
//			//把结果封装成一个js语句响应
//			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(e3Result);
//			mappingJacksonValue.setJsonpFunction(callback);
//			return mappingJacksonValue;
//			
//		}
//		return e3Result;
//	}
}
