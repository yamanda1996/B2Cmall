package cn.e3mall.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;

/**
 * 首页展示
 * @author ymdhi
 *
 */
@Controller
public class IndexController {
	@Autowired
	private ContentService contentService;
	
	@Value("${CategoryId}")
	private long cid;
	
	
	@RequestMapping("/index")
	public String showIndex(Model model){
		//查询内容列表
		List<TbContent> ad1List = contentService.getContentListByCid(cid);    //大广告
		//把结果传递给页面
		model.addAttribute("ad1List",ad1List);
		
		return "index";
	}
	
}
