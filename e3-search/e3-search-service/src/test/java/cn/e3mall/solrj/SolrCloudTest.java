package cn.e3mall.solrj;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class SolrCloudTest {
	
	
	@Test
	public void fun1() throws Exception{
		//创建一个集群的连接cloudSolrServer创建
		CloudSolrServer solrServer = new CloudSolrServer("192.168.1.144:2182,192.168.1.144:2183,192.168.1.144:2184");
		//参数：zkHost(zookeeper的地址列表)192.168.1.144:2182,192.168.1.144:2183,192.168.1.144:2184
		//设置一个defaultCollection属性
		solrServer.setDefaultCollection("collection2");
		//创建一个文档对象
		SolrInputDocument document = new SolrInputDocument();
		//向文档中添加域
		document.setField("id", "solr");
		document.setField("item_title", "yamanda");
		
		//把文件写入索引库
		solrServer.add(document);
		//提交
		solrServer.commit();
		
		
		
	}
}
