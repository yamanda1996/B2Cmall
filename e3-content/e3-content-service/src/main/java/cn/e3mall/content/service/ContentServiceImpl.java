package cn.e3mall.content.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import cn.e3mall.pojo.TbContentExample.Criteria;

/**
 * 内容管理服务
 * 
 * @author ymdhi
 *
 */
@Service
public class ContentServiceImpl implements ContentService {
	
	@Value("${contentList}")
	private String contentList;
	@Autowired
	private TbContentMapper contentMapper;
	
	@Autowired
	private JedisClient jedisClient;
	

	

	public E3Result addContent(TbContent content) {
		// 将内容数据插入到内容表中
		content.setCreated(new Date());
		content.setUpdated(new Date());
		// 插入数据库
		contentMapper.insert(content);
		//缓存同步，删除缓存中对应的数据，对数据库增删改操作执行时需要缓存同步
		jedisClient.hdel(contentList, content.getCategoryId()+"");    //只删除对应的域值即可，不用全部删除
		return E3Result.ok();
	}

	/**
	 * 根据内容分类id查询内容列表
	 */
	public List<TbContent> getContentListByCid(long categoryId) {
		// 先查询缓存，为了避免因为查询缓存报错而影响整个业务逻辑，在缓存处添加try catch
		try {
			// 如果缓存中有直接相应结果
			String json = jedisClient.hget(contentList, categoryId+"");
			if(StringUtils.isNotBlank(json)){    //判断字符串不为空
				
				List<TbContent> tbContentList = JsonUtils.jsonToList(json, TbContent.class);
				return tbContentList;
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 如果没有再查询数据库
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example); // 包含大文本

		// 查询完结果后，把结果添加到缓存中
		try {
			jedisClient.hset(contentList, categoryId+"", JsonUtils.objectToJson(list));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

}
