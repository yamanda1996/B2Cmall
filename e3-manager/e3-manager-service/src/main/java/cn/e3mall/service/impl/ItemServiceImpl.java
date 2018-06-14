package cn.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQTopic;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.DataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.pojo.TbItemExample.Criteria;
import cn.e3mall.service.ItemService;

/**
 * 商品管理service
 * 
 * @author ymdhi
 *
 */
@Service
public class ItemServiceImpl implements ItemService {
	@Autowired
	private TbItemMapper itemMapper;

	@Autowired
	private TbItemDescMapper itemDescMapper;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Resource
	private Destination topicDestination; // @Resource默认根据id注入

	@Autowired
	private JedisClient jedisClient;

	@Value("${ITEM_EXPIRE_TIME}")
	private int expireTime;

	@Value("${REDIS_ITEM_PRE}")
	private String redisPre;

	@Value("${REDIS_ITEM_POST}")
	private String redisPost;

	@Value("${REDIS_ITEMDESC_PRE}")
	private String redisDescPre;

	@Value("${REDIS_ITEMDESC_POST}")
	private String redisDescPost;

	public TbItem getItemById(long itemId) {

		// 查询缓存缓存，添加缓存不能影响正常的业务逻辑
		try {
			String itemStr = jedisClient.get(redisPre + itemId + redisPost);

			if (StringUtils.isNotBlank(itemStr)) {

				TbItem jsonItem = JsonUtils.jsonToPojo(itemStr, TbItem.class);
				return jsonItem;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// 如果缓存中没有，查询数据库
		// 根据主键查询
		// return itemMapper.selectByPrimaryKey(itemId);

		// 设置条件查询
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		// 设置查询条件
		criteria.andIdEqualTo(itemId);
		// 执行查询
		List<TbItem> list = itemMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			// 把结果添加到缓存
			try {
				jedisClient.set(redisPre + itemId + redisPost, JsonUtils.objectToJson(list.get(0)));
				// 设置过期时间
				jedisClient.expire(redisPre + itemId + redisPost, expireTime);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return list.get(0);
		}
		return null;

	}

	public DataGridResult getItemList(int page, int rows) {
		// 设置分页信息
		PageHelper.startPage(page, rows); // 静态方法

		// 执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		// 创建返回值
		DataGridResult result = new DataGridResult();

		result.setRows(list);
		// 取分页结果
		PageInfo<TbItem> pageInfo = new PageInfo<TbItem>(list); // 将查询得到的结果进行包装
		// 取总记录数
		long total = pageInfo.getTotal();
		result.setTotal(total);

		return result;
	}

	public E3Result addItem(TbItem item, String desc) {
		// 生成商品id
		final long genItemId = IDUtils.genItemId();
		// 补全item属性
		item.setId(genItemId);
		// 1-正常，2-下架，3-删除
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		// 向商品表插入数据
		itemMapper.insert(item);
		// 创建商品描述表对应的pojo对象
		TbItemDesc itemDesc = new TbItemDesc();
		// 补全属性
		itemDesc.setItemDesc(desc);
		itemDesc.setItemId(genItemId);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());

		// 向商品描述表插入数据
		itemDescMapper.insert(itemDesc);

		// 发送商品添加消息
		jmsTemplate.send(topicDestination, new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {

				return session.createTextMessage(genItemId + "");
			}
		});
		// 返回成功
		return E3Result.ok(); // ok是静态方法，返回E3Result对象
	}

	public TbItemDesc getItemDescById(long itemId) {

		// 查询缓存缓存，添加缓存不能影响正常的业务逻辑
		try {
			String itemDescStr = jedisClient.get(redisDescPre + itemId + redisDescPost);

			if (StringUtils.isNotBlank(itemDescStr)) {

				TbItemDesc jsonItemDesc = JsonUtils.jsonToPojo(itemDescStr, TbItemDesc.class);
				return jsonItemDesc;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// 如果缓存中没有，查询数据库
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		// 把结果添加到缓存
		try {
			jedisClient.set(redisDescPre + itemId + redisDescPost, JsonUtils.objectToJson(itemDesc));
			// 设置过期时间
			jedisClient.expire(redisDescPre + itemId + redisDescPost, expireTime);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return itemDesc;
	}

}
