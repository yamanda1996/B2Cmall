package cn.e3mall.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreeMarkerTest {

	
	@Test
	public void fun1() throws Exception{
		
		//创建一个模板文件
		//在WEB-INF下的ftl中
		//创建一个configuration对象
		Configuration configuration = new Configuration(Configuration.getVersion());
		//设置模板文件保存目录
		configuration.setDirectoryForTemplateLoading(new File("D:/MyEclipse/e3-item-web/src/main/webapp/WEB-INF/ftl"));  //全路径
		//设置模板文件的编码格式，一般是utf-8
		configuration.setDefaultEncoding("utf8");
		//加载一个模板文件，创建一个模板对象
		Template template = configuration.getTemplate("student.ftl");
		//创建一个数据集，可以是pojo，也可以是map（推荐用map）
		Map data = new HashMap<>();
		data.put("hello", "能年铃奈");
		
		//创建一个pojo对象
		Student stu = new Student("yamanda",18, 123, "中国");
		data.put("student", stu);
		//创建一个list对象
		List<Student> students = new ArrayList<Student>();
		Student stu2 = new Student("能年铃奈",18, 123, "日本");
		students.add(stu);
		students.add(stu2);
		data.put("stuList", students);
		
		//添加日期类型
		data.put("date", new Date());
		
		//加入空值
		data.put("null", null);
		
		//创建一个writer对象，指定输出文件的路径及文件名
		Writer writer = new FileWriter(new File("E:/Document/JAVA/freemarker/student.html"));
		
		//生成静态页面
		template.process(data, writer);
		//关闭流
		writer.close();
		
		
	}
}
