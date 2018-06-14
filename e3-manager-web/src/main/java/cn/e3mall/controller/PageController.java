package cn.e3mall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面跳转
 * @author ymdhi
 *
 */
@Controller
public class PageController {

	@RequestMapping(value="/")
	public String showIndex(){
		return "index";
	}
	
	@RequestMapping("/{page}")      //从url中取参数
	public String showPage(@PathVariable String page){
		return page;
	}
}
