package cn.e3mall.solrj;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class SolrjTest {
	@Test
	public void fun1() throws Exception {
		// 向索引库中添加索引
		// 创建solrServer对象，创建一个连接，参数：solr服务的url
		SolrServer solrServer = new HttpSolrServer("http://192.168.1.144:8080/solr/collection1");
		// 创建一个文档对象SolrInputDocument
		SolrInputDocument solrInputDocument = new SolrInputDocument();
		// 向文档对象中添加域，文档中必须包含一个id域,所有域的名称必须在schema.xml中定义
		solrInputDocument.addField("id", "abc");
		solrInputDocument.addField("item_title", "哈哈哈");
		solrInputDocument.addField("item_price", 1000);

		// 把文档写入索引库
		solrServer.add(solrInputDocument);
		// 提交
		solrServer.commit();

	}

	// 更新就是先删除再添加

	@Test
	public void fun2() throws Exception {

		SolrServer solrServer = new HttpSolrServer("http://192.168.1.144:8080/solr/collection1");
		// 两种删除方式
		// solrServer.deleteById("abc");

		solrServer.deleteByQuery("id:abc");
		// 提交
		solrServer.commit();

	}

	// 测试查询
	@Test
	public void fun3() throws Exception {

		// 创建solrServer对象
		SolrServer solrServer = new HttpSolrServer("http://192.168.1.144:8080/solr/collection1");
		// 创建solrQuery对象
		SolrQuery solrQuery = new SolrQuery();
		// 设置查询条件
		// solrQuery.setQuery("*:*"); //这两种写法一样
		solrQuery.set("q", "*:*");

		// 执行查询，得到queryResponse对象
		QueryResponse queryResponse = solrServer.query(solrQuery);

		// 取文档列表，取查询结果的总记录数
		SolrDocumentList documentList = queryResponse.getResults();
		long totalNum = documentList.getNumFound(); // 得到总记录数
		System.out.println("the total num is " + totalNum);

		// 遍历文档列表，从文档中取域的内容
		for (SolrDocument solrDocument : documentList) {
			System.out.println(solrDocument.get("id"));
			System.out.println(solrDocument.get("item_title"));
		}

	}

	@Test
	public void fun4() throws Exception {
		// 复杂查询
		// 创建solrServer对象
		SolrServer solrServer = new HttpSolrServer("http://192.168.1.144:8080/solr/collection1");
		// 创建solrQuery对象
		SolrQuery solrQuery = new SolrQuery();
		// 设置查询条件
		// solrQuery.setQuery("*:*"); //这两种写法一样
		solrQuery.set("q", "手机");
		solrQuery.setStart(0);
		solrQuery.setRows(20);
		solrQuery.set("df", "item_title");
		solrQuery.setHighlight(true); // 开启高亮
		solrQuery.addHighlightField("item_title"); // 高亮字段
		solrQuery.setHighlightSimplePre("<span>"); // 高亮前缀
		solrQuery.setHighlightSimplePost("</span>"); // 后缀
		// 执行查询
		// 执行查询，得到queryResponse对象
		QueryResponse queryResponse = solrServer.query(solrQuery);

		// 取文档列表，取查询结果的总记录数
		SolrDocumentList documentList = queryResponse.getResults();
		long totalNum = documentList.getNumFound(); // 得到总记录数
		System.out.println("the total num is " + totalNum);

		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		// 遍历文档列表，从文档中取域的内容
		for (SolrDocument solrDocument : documentList) {
			System.out.println(solrDocument.get("id"));
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");     //高亮取值
			String title = "";
			if(list != null && list.size() >0 ){
				title = list.get(0);    //如果标题中有手机，高亮显示
				
			}else{
				title = (String) solrDocument.get("item_title");    //如果标题中没有手机，正常显示标题即可 
			}
			System.out.println(title);
			
			
		}
		
		

	}
}
