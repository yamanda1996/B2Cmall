package cn.e3mall.fastDFS;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import cn.e3mall.common.utils.FastDFSClient;

public class FastDFSTest {
	
	@Test
	public void fun1() throws Exception{
		//使用fastDFS需要创建一个配置文件，文件名任意，内容就是tracker服务器的地址
		//使用全局对象加载配置文件
		ClientGlobal.init("D:/MyEclipse/e3-manager-web/src/main/resources/conf/client.conf");
		//创建一个trackerClient对象
		TrackerClient trackerClient = new TrackerClient();
		//通过trackerClient获得trackerServer对象
		TrackerServer trackerServer = trackerClient.getConnection();
		//创建一个storageServer的引用，可以是null
		StorageServer storageServer = null;
		//创建一个storageClient对象，参数需要storageServer和trackerServer
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);
		//使用storageClient上传文件
		String[] strings = storageClient.upload_file("e:/images/111.jpg", "jpg", null);
		for (String string : strings) {
			System.out.println(string);
		}
	}
	@Test
	public void fun2() throws Exception{
		FastDFSClient fastDFSClient = new FastDFSClient("D:/MyEclipse/e3-manager-web/src/main/resources/conf/client.conf");
		String string = fastDFSClient.uploadFile("e:/images/111.jpg");
		System.out.println(string);
		
	}
}
