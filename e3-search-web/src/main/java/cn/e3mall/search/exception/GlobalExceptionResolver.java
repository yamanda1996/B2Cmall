package cn.e3mall.search.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;


/**
 * 全局异常处理器
 * @author ymdhi
 *
 */
public class GlobalExceptionResolver implements HandlerExceptionResolver{
	
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionResolver.class);
	
	
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception exception) {
		//打印控制台
		exception.printStackTrace();
		//写日志  日志四个级别，严重程度从底到高：debug	info	warn	error
		logger.debug("测试。。。。。。。。。。。。");
		logger.info("系统发生异常了。。。。。。。。。。。");
		logger.error("系统异常,哈哈哈哈哈哈哈啊哈哈哈哈",exception);
		//发邮件，发短信
		//使用jmail工具包发送邮件
		//发短信使用第三方的webService
		//显示错误页面
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("error/exception");
		return modelAndView;
	}

}
