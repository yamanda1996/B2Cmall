package cn.e3mall.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.sso.service.LoginService;


/**
 * 用户登录处理
 * @author ymdhi
 *
 */
@Controller
public class LoginController {
	
	@Autowired
	private LoginService loginService;
	
	@Value("${TOKEN}")
	private String tokenName;
	
	@RequestMapping("/page/login")
	public String showLoginPage(){
		return "login";
	}
	
	@RequestMapping(value="/user/login",method=RequestMethod.POST)
	@ResponseBody
	public E3Result login(String username,String password,HttpServletRequest request,HttpServletResponse response){
		/**
		 * 1、调用service层的方法
		 * 2、利用service中返回的token将token写入到cookie之中
		 */
		E3Result e3Result = loginService.userLogin(username, password);
		//判断登录是否成功
		if(e3Result.getStatus().equals(200)){
			String token = (String) e3Result.getData();
			//如果登录成功，把token写入cookie
			CookieUtils.setCookie(request, response, tokenName, token);
		}
		//返回结果
		return e3Result;
	}
}
