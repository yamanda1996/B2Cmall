package cn.e3mall.item.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import cn.e3mall.item.pojo.Item;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;


/**
 * 监听商品添加消息，生成对应的静态页面
 * @author ymdhi
 *
 */
public class HtmlGenListener implements MessageListener {
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	@Value("${HTML_STORE_PATH}")
	private String HTML_STORE_PATH;

	
	public void onMessage(Message message) {
		try {
			
			//创建一个模板，参考jsp
			
			//从消息中取商品id
			TextMessage textMessage = (TextMessage) message;
			String itemIdStr = textMessage.getText();
			Long itemId = new Long(itemIdStr);
			
			//等待事务提交，不能立刻查询，因为可能没反应过来查出来是空值
			Thread.sleep(1000);
			
			//根据商品id查询商品信息，商品基本信息和商品描述
			TbItem tbItem = itemService.getItemById(itemId);    //TbItem中不包含images属性
			TbItemDesc tbItemDesc = itemService.getItemDescById(itemId);
			Item item = new Item(tbItem);     //自定义的Item类中包含images属性
			//创建一个数据集，把商品信息封装
			Map data = new HashMap<>();
			data.put("item", item);
			data.put("itemDesc", tbItemDesc);
			//加载模板对象
			Configuration configuration = freeMarkerConfigurer.getConfiguration();
			Template template = configuration.getTemplate("item.ftl");
			//创建一个输出流，指定输出的目录及文件名
			String fileName = itemId + ".html";
			Writer writer = new FileWriter(new File(HTML_STORE_PATH + fileName));
			
			//生成静态页面
			template.process(data, writer);
			//关闭流
			writer.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
