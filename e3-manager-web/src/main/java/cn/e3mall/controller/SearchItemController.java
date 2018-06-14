package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.search.service.SearchItemService;

/**
 * 索引库管理
 * 
 * @author ymdhi
 *
 */
@Controller
public class SearchItemController {
	@Autowired
	private SearchItemService searchItemService;

	@RequestMapping(value = "/index/item/import")
	@ResponseBody
	public E3Result importAllList() {

		E3Result e3Result = searchItemService.importAllItems();
		return e3Result;

	}
}
