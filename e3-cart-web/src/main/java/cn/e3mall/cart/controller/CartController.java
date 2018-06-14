package cn.e3mall.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.service.ItemService;

/**
 * 购物车处理controller
 * 
 * @author ymdhi
 *
 */
@Controller
public class CartController {

	@Autowired
	private ItemService itemService;

	@Value("${EXPIRE_TIME}")
	private Integer EXPIRE_TIME;

	@Autowired
	private CartService cartService;

	@RequestMapping("/cart/add/{itemId}")
	public String addCart(@PathVariable Long itemId, @RequestParam(defaultValue = "1") Integer num,
			HttpServletRequest request, HttpServletResponse response) {
		// 判断用户是否登录
		TbUser user = (TbUser) request.getAttribute("user");
		if (user != null) {
			// 如果登录，把购物车写入redis
			// 保存到服务端
			cartService.addCart(itemId, user.getId(), num);
			// 返回逻辑视图
			return "cartSuccess";
		}
		// 未登录，使用cookie
		// 从cookie中取购物车列表
		List<TbItem> cartList = getCartListFromCookie(request);
		boolean flag = false;
		// 判断商品在商品列表中是否存在
		for (TbItem tbItem : cartList) {
			if (tbItem.getId().equals(itemId)) {
				// 找到商品数量相加
				tbItem.setNum(tbItem.getNum() + num);
				flag = true;
				// 跳出循环
				break;
			}
		}
		if (!flag) { // 没有找到对应商品
			// 如果不存在，根据商品id查询商品信息，得到tbItem对象
			TbItem tbItem = itemService.getItemById(itemId);
			// 设置商品数量
			tbItem.setNum(num);
			// 取一张图片即可
			String imageStr = tbItem.getImage();
			if (StringUtils.isNotBlank(imageStr)) { // 妹的，空指针啊空指针
				tbItem.setImage(imageStr.split(",")[0]);
			}
			// 把商品添加到商品列表
			cartList.add(tbItem);

		}
		// 写入cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), EXPIRE_TIME, true);

		// 如果存在，数量相加
		// 返回添加成功页面
		return "cartSuccess";
	}

	/**
	 * 显示购物车列表
	 */
	@RequestMapping("/cart/cart")
	public String showCartPage(HttpServletRequest request) {
		// 如果是登录状态下
		TbUser user = (TbUser) request.getAttribute("user");
		if (user != null) {
			// 从cookie中取购物车列表

			// 如果不为空，把cookie中的购物车商品和redis中的商品合并
			// 把cookie中的购物车删除
			// 从服务端取购物车列表，进行展示

		}
		// 未登录状态
		// 从cookie中取出商品列表
		List<TbItem> itemList = getCartListFromCookie(request);
		if (itemList != null && itemList.size() > 0) {

			// 把列表传递给页面
			request.setAttribute("cartList", itemList);
		}

		// 返回逻辑视图
		return "cart";
	}

	/**
	 * 修改显示页面中商品的数量
	 */
	@RequestMapping(value = "/cart/update/num/{itemId}/{updateNum}", method = RequestMethod.POST)
	@ResponseBody
	public E3Result updateNum(@PathVariable Long itemId, @PathVariable Integer updateNum, HttpServletRequest request,
			HttpServletResponse response) {
		// 判断用户是否为登录状态
		TbUser user = (TbUser) request.getAttribute("user");
		if (user != null) {
			cartService.updateCartNum(user.getId(), itemId, updateNum);
			return E3Result.ok();
		}
		// 查询cookie，获得购物车列表
		List<TbItem> itemList = getCartListFromCookie(request);
		// 遍历商品列表，找到该商品，修改购物车列表中的商品数量
		for (TbItem tbItem : itemList) {
			if (tbItem.getId().equals(itemId)) {
				tbItem.setNum(updateNum);
				break;
			}
		}
		// 将修改后的结果保存到cookie中
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(itemList), EXPIRE_TIME, true);
		// 返回成功

		return E3Result.ok();
	}

	/**
	 * 从cookie中获取购物车列表的方法
	 * 
	 * @param request
	 * @return
	 */
	private List<TbItem> getCartListFromCookie(HttpServletRequest request) {
		String json = CookieUtils.getCookieValue(request, "cart", true);
		// 判断json是否为空
		if (StringUtils.isNotBlank(json)) {
			// 把json转化成商品列表
			List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
			return list;
		}
		return new ArrayList<TbItem>();
	}

	/**
	 * 删除选中的商品
	 */
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteItem(@PathVariable Long itemId, HttpServletRequest request, HttpServletResponse response) {

		// 判断用户是否为登录状态
		TbUser user = (TbUser) request.getAttribute("user");
		if (user != null) {
			cartService.deleteCartItem(user.getId(), itemId);
			return "redirect:/cart/cart.html";
		}
		// 从cookie中取到商品列表
		List<TbItem> cartList = getCartListFromCookie(request);
		for (TbItem tbItem : cartList) {
			// 找到对应的商品，将其数量置为0
			// 找到对应的商品，进行删除
			if (tbItem.getId().equals(itemId)) {
				cartList.remove(tbItem);
				// 跳出循环
				break;
			}
		}
		// 将删除完的商品列表保存到cookie中
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), EXPIRE_TIME, true);
		// 重定向视图
		return "redirect:/cart/cart.html"; // 重定向到cart/cart 不加/表示为相对路径
											// 加上/表示为绝对路径
	}

}
