package cn.e3mall.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.service.SearchService;

/**
 * 搜索的表现层
 * @author ymdhi
 *
 */
@Controller
public class SearchController {
	@Autowired
	private SearchService searchService;
	
	@Value("${SEARCH_RESULT_ROWS}")
	private Integer rows;
	
	@RequestMapping("/search")
	public String searchItemList(String keyword,@RequestParam(defaultValue="1") Integer page,Model model) throws Exception{
		
		//对keyword进行转码
		keyword = new String(keyword.getBytes("iso-8859-1"),"utf8");   //对keyword进行转码处理
		//查询商品列表
		SearchResult searchResult = searchService.search(keyword, page, rows);
		model.addAttribute("itemList", searchResult.getItemList());
		model.addAttribute("query", keyword);
		model.addAttribute("totalPages", searchResult.getTotalPages());
		model.addAttribute("page", page);
		model.addAttribute("recourdCount", searchResult.getRecourdCount());
		
		//异常测试
		//int a = 1/0;
		
		return "search";
		
		
	}
}
