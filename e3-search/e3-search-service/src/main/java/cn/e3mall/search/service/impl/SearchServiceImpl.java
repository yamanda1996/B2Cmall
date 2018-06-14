package cn.e3mall.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.dao.SearchDao;
import cn.e3mall.search.service.SearchService;
/**
 * 商品搜索service
 * @author ymdhi
 *
 */
@Service
public class SearchServiceImpl implements SearchService{
	
	
	@Autowired
	private SearchDao searchDao; 

	public SearchResult search(String keyword, int page, int rows) throws Exception {
		//创建solrQuery对象
		SolrQuery query = new SolrQuery();
		//设置查询条件
		
		query.setQuery(keyword);
		//设置分页条件
		if(page<=0){
			page = 1;
		}
		query.setStart((page-1)*rows);
		query.setRows(rows);
		//设置默认搜索域
		query.set("df","item_title");
		//开启高亮
		query.setHighlight(true);
		query.addHighlightField("item_title");   //设置高亮显示的域
		query.setHighlightSimplePre("<span style=\"color:red\">");
		query.setHighlightSimplePost("</span>");
		//调用dao执行查询
		SearchResult searchResult = searchDao.getSearchResult(query);
		//计算总页数
		long recourdCount = searchResult.getRecourdCount();
//		int totalPage = 0;
//		if(recourdCount%rows == 0){
//			totalPage = (int) (recourdCount/rows);
//		}else {
//			totalPage = (int) (recourdCount/rows) + 1;
//		}
		int totalPage = (int) (recourdCount/rows);
		if(recourdCount%rows>0){
			totalPage++;
		}
		searchResult.setTotalPages(totalPage);
		//返回结果
		return searchResult;
	}

}
