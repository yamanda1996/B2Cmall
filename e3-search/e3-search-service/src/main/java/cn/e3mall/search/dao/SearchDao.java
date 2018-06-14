package cn.e3mall.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.pojo.SearchResult;

/**
 * 商品搜索dao
 * 
 * @author ymdhi
 *
 */

@Repository
public class SearchDao {
	
	
	@Autowired
	private SolrServer solrServer;    //已经在spring容器中配置好了，可以直接注入
	
	public SearchResult getSearchResult(SolrQuery query) throws Exception {
		
		SearchResult searchResult = new SearchResult();
		// 执行查询
		// 执行查询，得到queryResponse对象
		QueryResponse queryResponse = solrServer.query(query);
		
		//取查询结果和总记录数
		//高亮显示

		// 取文档列表，取查询结果的总记录数
		SolrDocumentList documentList = queryResponse.getResults();
		long totalNum = documentList.getNumFound(); // 得到总记录数
		searchResult.setRecourdCount(totalNum);

		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		List<SearchItem> itemList = new ArrayList<SearchItem>();
		// 遍历文档列表，从文档中取域的内容
		for (SolrDocument solrDocument : documentList) {
			
			SearchItem searchItem = new SearchItem();
			searchItem.setId((String) solrDocument.get("id"));
			searchItem.setCategory_name((String) solrDocument.get("item_category_name"));
			searchItem.setImage((String) solrDocument.get("item_image"));
			searchItem.setPrice((long) solrDocument.get("item_price"));
			searchItem.setSell_point((String) solrDocument.get("item_sell_point"));
			searchItem.setTitle((String) solrDocument.get("item_title"));
			//添加到商品列表
			itemList.add(searchItem);
			//取高亮显示
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title"); // 高亮取值
			String title = "";
			if (list != null && list.size() > 0) {
				title = list.get(0); // 如果标题中有手机，高亮显示

			} else {
				title = (String) solrDocument.get("item_title"); // 如果标题中没有手机，正常显示标题即可
			}
			searchItem.setTitle(title);
			

		}
		searchResult.setItemList(itemList);
		

		return searchResult;
	}
}
