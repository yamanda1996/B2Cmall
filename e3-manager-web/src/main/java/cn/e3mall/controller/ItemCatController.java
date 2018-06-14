package cn.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.TreeNode;
import cn.e3mall.service.ItemCatService;
/**
 * 商品分类管理
 * @author ymdhi
 *
 */
@Controller
public class ItemCatController {
	@Autowired
	private ItemCatService itemCatService;
	
	@RequestMapping("/item/cat/list")
	@ResponseBody
	public List<TreeNode> getItemCatList(@RequestParam(name="id",defaultValue="0")long parentId){   
		//从页面中获得的参数值和形参不一致，可以用@RequestParam，设置默认值用defaultValue
		//调用服务
		List<TreeNode> list = itemCatService.getItemCatList(parentId);
		return list;
	}
}
