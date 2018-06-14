package cn.e3mall.service;

import java.util.List;

import cn.e3mall.common.pojo.TreeNode;

/**
 * 商品分类
 * @author ymdhi
 *
 */
public interface ItemCatService {
	List<TreeNode> getItemCatList(long parentId);
}
