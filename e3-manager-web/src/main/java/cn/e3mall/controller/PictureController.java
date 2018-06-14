package cn.e3mall.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.e3mall.common.utils.FastDFSClient;
import cn.e3mall.common.utils.JsonUtils;

/**
 * 图片上传controller
 * @author ymdhi
 *
 */
@Controller
public class PictureController {
	
	@Value("${IMAGE_SERVER_URL}")  //在springmvc.xml中配置好了resource.properties配置文件，现在进行使用
	private String IMAGE_SERVER_URL;
	
	@RequestMapping(value="/pic/upload",produces=MediaType.TEXT_PLAIN_VALUE+";charset=utf-8")  //设置responseHeader的content-type为text/plain;charset=utf-8
	@ResponseBody
	public String uploadFile(MultipartFile uploadFile){    //返回String，为了处理多浏览器的兼容性
		
		try {
			//把图片上传到图片服务器
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:conf/client.conf");   //classpath:后面不用加/
			//得到上传文件的扩展名
			String originalFilename = uploadFile.getOriginalFilename();
			String extName = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
			//得到图片的地址和文件名
			String url = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
			//补充为完整的url
			url = IMAGE_SERVER_URL+url;
			//封装到map中返回
			Map result = new HashMap();
			result.put("error", 0);
			result.put("url", url);
			return JsonUtils.objectToJson(result);
			
		} catch (Exception e) {
			e.printStackTrace();
			Map result = new HashMap();
			result.put("error", 1);
			result.put("message", "图片上传失败");
			return JsonUtils.objectToJson(result);
			
		}
	}
}
