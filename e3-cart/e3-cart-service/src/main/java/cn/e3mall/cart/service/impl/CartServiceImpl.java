package cn.e3mall.cart.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;

/**
 * 购物车处理服务
 * @author ymdhi
 *
 */
@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private JedisClient jedisClient;
	
	@Autowired
	private TbItemMapper tbItemMapper;
	
	@Value("${CART}")
	private String CART;
	
	public E3Result addCart(Long itemId, Long userId,int num) {
		//向redis中添加购物车
		//数据类型未hash,key为userId，  field为商品id,value为商品信息
		//判断商品是否存在,key值为CART:userId
		String itemInfo = jedisClient.hget(CART + userId, itemId + "");
		if(StringUtils.isNotBlank(itemInfo)){
			//如果商品存在，数量相加
			//把json转换成tbItem
			TbItem tbItem = JsonUtils.jsonToPojo(itemInfo, TbItem.class);
			tbItem.setNum(tbItem.getNum() + num);
			//写回redis
			jedisClient.hset(CART + userId, itemId + "", JsonUtils.objectToJson(tbItem));
			return E3Result.ok();
		}
		//如果商品不存在，根据商品id取商品信息，添加到购物车列表中
		//服务直接不要互相调用，所以在这可以直接查询数据库
		TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
		//设置购物车数量
		tbItem.setNum(num);
		//取一张图片
		String image = tbItem.getImage();
		if(StringUtils.isNotBlank(image)){
			tbItem.setImage(image.split(",")[0]);
		}
		jedisClient.hset(CART + userId, itemId + "", JsonUtils.objectToJson(tbItem));
		//返回成功
		return E3Result.ok();
	}

	
	public E3Result mergeCart(long userId, List<TbItem> itemList) {
		
		//遍历商品列表
		for (TbItem tbItem : itemList) {
			addCart(tbItem.getId(), userId, tbItem.getNum());
		}
		//把列表添加到购物车
		//判断购物车中是否有此商品
		//如果有，数量相加
		//如果没有，添加新的商品
		//返回成功
		return E3Result.ok();
	}


	
	public List<TbItem> getCartList(long userId) {
		//根据用户id查询购物车列表
		
		return null;
	}


	/**
	 * 
	 */
	public E3Result updateCartNum(long userId, long itemId, int num) {
		//从redis中取商品列表
		String json = jedisClient.hget(CART + userId, itemId + "");
		//更新商品数量
		TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
		tbItem.setNum(num);
		//写入redis
		jedisClient.hset(CART + userId, itemId + "", JsonUtils.objectToJson(tbItem));
		return E3Result.ok();
	}


	
	public E3Result deleteCartItem(long userId, long itemId) {
		jedisClient.hdel(CART + userId, itemId + "");
		return E3Result.ok();
	}

}
