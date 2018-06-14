package cn.e3mall.jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class TestJedis {
	
	/*@Test
	public void fun1(){
		//创建jedis对象   参数：host和port
		Jedis jedis = new Jedis("192.168.1.144", 6379);
		//使用jedis操作redis
		jedis.set("test", "123");
		System.out.println(jedis.get("test"));
		//关闭连接
		jedis.close();
		
		
	}
	@Test
	public void fun2(){
		//创建连接池对象  参数：host和port
		JedisPool jedisPool = new JedisPool("192.168.1.144", 6379);
		//从连接池中获得连接   一个jedis对象
		Jedis jedis = jedisPool.getResource();
		//同上，使用jedis操作redis
		String string = jedis.get("test");
		System.out.println(string);
		//每次使用完毕后关闭连接，连接池回收资源
		jedis.close();
		//关闭连接池（项目结束之后关闭连接池，连接池是单例的）
		jedisPool.close();
		
	}
	*//**
	 * 连接集群
	 *//*
	@Test
	public void fun3(){
		//连接jedis集群，创建对象，只有一个参数nodes，set类型，其中包含若干个hostAndPort对象
		Set<HostAndPort> nodes = new HashSet<HostAndPort>();
		nodes.add(new HostAndPort("192.168.1.144", 7001));
		nodes.add(new HostAndPort("192.168.1.144", 7002));
		nodes.add(new HostAndPort("192.168.1.144", 7003));
		nodes.add(new HostAndPort("192.168.1.144", 7004));
		nodes.add(new HostAndPort("192.168.1.144", 7005));
		nodes.add(new HostAndPort("192.168.1.144", 7006));
		JedisCluster jedisCluster = new JedisCluster(nodes);
				
		//直接使用jedisCluster对象操作redis
		String string = jedisCluster.get("test");
		System.out.println(string);
		
		//关闭jedisCluster对象
		jedisCluster.close();
				
		
	}*/
}
