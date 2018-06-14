package cn.e3mall.content.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.TreeNode;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import cn.e3mall.pojo.TbContentCategoryExample.Criteria;

/**
 * 内容分类服务
 * 
 * @author ymdhi
 *
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;

	public List<TreeNode> getContentCatList(long parentId) {
		// 根据parentId查询分类列表
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		// 设置查询条件
		criteria.andParentIdEqualTo(parentId);
		// 执行查询
		List<TbContentCategory> catList = contentCategoryMapper.selectByExample(example);

		List<TreeNode> treeNodeList = new ArrayList<TreeNode>();
		//转换列表
		for (TbContentCategory tbContentCategory : catList) {
			TreeNode node = new TreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			//如果是父节点则返回closed,反之返回open
			node.setState(tbContentCategory.getIsParent()?"closed":"open");  
			treeNodeList.add(node);
		}

		return treeNodeList;
	}

	
	public E3Result addContentCategory(long parentId, String name) {
		
		//创建一个TbContentCategory的对象进行数据库插入
		TbContentCategory contentCategory = new TbContentCategory();
		
		//属性补全
		contentCategory.setParentId(parentId);
		contentCategory.setIsParent(false);    //新添加的节点是叶子节点
		contentCategory.setCreated(new Date());
		contentCategory.setName(name);
		contentCategory.setSortOrder(1);    //默认排序为1
		contentCategory.setStatus(1);   //1(正常),2(删除)
		contentCategory.setUpdated(new Date());
		//插入数据库，使用了selectKey自动返回id
		contentCategoryMapper.insert(contentCategory);    //contentCategory对象中自动给id赋值
		
		//判断其父节点的isParent属性，如果是0的话，改成1 ！！！！！！！！！！！！！
		TbContentCategory parentContentCategory = contentCategoryMapper.selectByPrimaryKey(parentId);
		if(!parentContentCategory.getIsParent()){
			parentContentCategory.setIsParent(true);
			//更新到数据库中
			contentCategoryMapper.updateByPrimaryKey(parentContentCategory);
		}
		
		//返回结果，E3Result中包含一个data对象，data对象中有id属性
		return E3Result.ok(contentCategory);
	}

}
