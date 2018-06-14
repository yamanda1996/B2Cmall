package cn.e3mall.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;


/**
 * 用户登录处理拦截器
 * @author ymdhi
 *
 */
public class LoginInterceptor implements HandlerInterceptor {
	
	@Autowired
	private TokenService TokenService;
	

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception)
			throws Exception {
		//完成处理，在返回modelAndView之后
		//可以在此处理异常

	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {
		//handler执行之后，返回modelAndView之前，对modelAndView进行处理

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//前处理,执行handler之前执行此方法
		//返回true，放行，返回false,拦截
		
		//1、从cookie中获取token，
		String token = CookieUtils.getCookieValue(request, "token");
		//2、没有token，肯定是未登录，直接放行
		if(StringUtils.isBlank(token)){
			return true;
		}
		//3、取到token，调用sso系统的服务，根据token获取用户信息
		E3Result e3Result = TokenService.getUserByToken(token);
		//4、没有取到用户信息，登录过期，直接放行
		if(e3Result.getStatus() != 200){
			return true;
		}
		//5、取到用户信息，用户是登录状态
		TbUser user = (TbUser) e3Result.getData();
		//6、把用户信息放到request中，只需要在controller中判断request中是否有用户信息即可
		request.setAttribute("user", user);
		//7、放行
		return true;
	}

}
