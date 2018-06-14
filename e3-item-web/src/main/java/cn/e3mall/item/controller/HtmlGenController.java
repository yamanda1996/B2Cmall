package cn.e3mall.item.controller;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateNotFoundException;

/**
 * 生成静态页面controller
 * @author ymdhi
 *
 */
@Controller
public class HtmlGenController {
	
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfiguration;
	
	
	@RequestMapping("/genhtml")
	@ResponseBody
	public String genHtml() throws Exception{
		Configuration configuration = freeMarkerConfiguration.getConfiguration();
		//加载模板
		Template template = configuration.getTemplate("hello.ftl");
		//创建一个数据集
		Map map = new HashMap();
		map.put("hello", "广濑绫");
		//指定文件输出路径及文件名
		Writer writer = new FileWriter("E:/Document/JAVA/freemarker/hello2.html");
		
		//输出文件
		template.process(map, writer);
		//关闭资源
		writer.close();
		return "ok";
	}
	

}
