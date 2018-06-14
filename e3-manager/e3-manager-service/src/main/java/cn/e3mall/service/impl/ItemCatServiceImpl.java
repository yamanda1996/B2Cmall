package cn.e3mall.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.TreeNode;
import cn.e3mall.mapper.TbItemCatMapper;
import cn.e3mall.pojo.TbItemCat;
import cn.e3mall.pojo.TbItemCatExample;
import cn.e3mall.pojo.TbItemCatExample.Criteria;
import cn.e3mall.service.ItemCatService;
@Service
public class ItemCatServiceImpl implements ItemCatService {
	@Autowired
	private TbItemCatMapper itemCatMapper;
	public List<TreeNode> getItemCatList(long parentId) {
		//根据parentId查询子节点列表
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);     //设置查询条件
		List<TbItemCat> itemCatList = itemCatMapper.selectByExample(example);
		//创建返回结果的list
		List<TreeNode> treeNodeList = new ArrayList<TreeNode>();
		//把列表转换成TreeNode列表
		for (TbItemCat tbItemCat : itemCatList) {
			TreeNode treeNode = new TreeNode();
			treeNode.setId(tbItemCat.getId());
			treeNode.setText(tbItemCat.getName());
			treeNode.setState(tbItemCat.getIsParent()?"closed":"open");    //根据是否有子节点进行判断，若有，设置state为closed，如果没有，设置其为open
			treeNodeList.add(treeNode);
		}
		//返回结果
		return treeNodeList;
	}

}
