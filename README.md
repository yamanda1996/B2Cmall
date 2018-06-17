# B2Cmall网上商城

## 介绍

本项目是基于SOA架构（Service Oriented Architecture）即面向服务的架构。

也就是把工程拆分成服务层、表现层两个工程。服务层中包含业务逻辑，只需要对外提供服务即可。
表现层只需要处理和页面的交互，业务逻辑都是调用服务层的服务来实现。
![](https://github.com/yamanda1996/B2Cmall/blob/master/model.JPG)<br>
具体的结构：<br>
![](https://github.com/yamanda1996/B2Cmall/blob/master/detailed.JPG)<br>

## Dubbo的使用

### Dubbo架构

![](https://github.com/yamanda1996/B2Cmall/blob/master/dubbo.jpg)<br>

#### 节点角色说明
* Provider: 暴露服务的服务提供方。
* Consumer: 调用远程服务的服务消费方。
*	Registry: 服务注册与发现的注册中心。
*	Monitor: 统计服务的调用次调和调用时间的监控中心。
*	Container: 服务运行容器。<br>
#### 调用关系说明
* 0、服务容器负责启动，加载，运行服务提供者。
* 1、服务提供者在启动时，向注册中心注册自己提供的服务。
* 2、服务消费者在启动时，向注册中心订阅自己所需的服务。
* 3、注册中心返回服务提供者地址列表给消费者，如果有变更，注册中心将基于长连接推送变更数据给消费者。
* 4、服务消费者，从提供者地址列表中，基于软负载均衡算法，选一台提供者进行调用，如果调用失败，再选另一台调用。
* 5、服务消费者和提供者，在内存中累计调用次数和调用时间，定时每分钟发送一次统计数据到监控中心。<br>
```xml
//发布服务
<bean id="xxxService" class="com.xxx.XxxServiceImpl" />
<dubbo:service interface="com.xxx.XxxService" ref="xxxService" />
//调用服务
<dubbo:reference id="xxxService" interface="com.xxx.XxxService" />
<bean id="xxxAction" class="com.xxx.XxxAction">
	<property name="xxxService" ref="xxxService" />
</bean>
```
#### 注册中心--zookeeper
1、可以作为集群的管理工具使用。<br>
2、可以集中管理配置文件。<br>
## nginx的使用
Nginx是一款高性能的http 服务器/反向代理服务器及电子邮件（IMAP/POP3）代理服务器。由俄罗斯的程序设计师Igor Sysoev所开发，官方测试nginx能够支支撑5万并发链接，并且cpu、内存等资源消耗却非常低，运行非常稳定。非常适合访问静态资源，此外利用nginx还可以做负载均衡服务器。<br>
### nginx反向代理
正向代理是客户端访问代理服务器来上网或者转发请求，而反向代理则是外界的请求反向代理服务器来决定哪台服务器来响应这个请求。<br>
### 负载均衡
nginx默认采用轮询的方式来实现负载均衡，而这个轮询可以加权，权数越大被使用到的几率也就越大。<br>
```xml
//加权配置
 upstream tomcat {
	server 192.168.1.144:8080;
	server 192.168.1.144:8081 weight=2;
 }
```
## Redis用来做缓存
### Redis的前台启动和后台启动
前台启动就直接./redis-server启动就行了，但缺点是这样启动之后命令行就一直被redis占据，所以应该使用redis的后台启动方式，需要修改redis.conf配置文件，同时启动时使用./redis-server redis.conf来后台启动
### Redis的五种数据类型
* String
* Hash
* List
* Set
* Sort Set<br>
其中本项目中主要使用String（登录模块中的用户uuid）和Hash（购物车模块中的商品列表）这两种类型<br>
### Redis持久化方案
Redis是运行在内存空间中的数据库，所以一旦出现停电的故障很容易造成数据的丢失，所以redis的持久化非常重要<br>
#### RDB形式
Rdb：快照形式，定期把内存中当前时刻的数据保存到磁盘。Redis默认支持的持久化方案。
#### AOF形式
aof形式：append only file。把所有对redis数据库操作的命令，增删改操作的命令。保存到文件中。数据库恢复时把所有的命令执行一遍即可。
### Redis集群的搭建
#### Redis集群架构图
![](https://github.com/yamanda1996/B2Cmall/blob/master/redis-cluster.jpg)<br>
![](https://github.com/yamanda1996/B2Cmall/blob/master/redis-cluster2.jpg)<br>
架构细节<br>
* 所有的redis节点彼此互联(PING-PONG机制),内部使用二进制协议优化传输速度和带宽.
* 节点的fail是通过集群中超过半数的节点检测失效时才生效.
* 客户端与redis节点直连,不需要中间proxy层.客户端不需要连接集群所有节点,连接集群中任何一个可用节点即可
* redis-cluster把所有的物理节点映射到[0-16383]slot上,cluster 负责维护node<->slot<->value<br>
#### 集群的搭建
Redis集群中至少应该有三个节点。要保证集群的高可用，需要每个节点有一个备份机，所以Redis集群至少需要6台服务器。<br>
可以搭建伪分布式。可以使用一台虚拟机运行6个redis实例。需要修改redis的端口号7001-7006
#### Redis的使用
可以使用JedisClient对redis缓存进行操作，我配置了两个版本的JedisClient，集群版（发布之后使用）和单机版（测试时使用，因为作为服务器的电脑配置太渣了，所以用单机版凑活用吧= =）<br>
```xml
	<!-- 连接redis单机版 -->
	<bean class="cn.e3mall.common.jedis.JedisClientPool" id="jedisClientPool"> 
		<property name="jedisPool" ref="jedisPool"></property> </bean> <bean class="redis.clients.jedis.JedisPool" 
		id="jedisPool"> <constructor-arg name="host" value="192.168.1.144"></constructor-arg> 
		<constructor-arg name="port" value="6379"></constructor-arg> </bean>
		
	

	<!-- 连接redis集群版 -->
	<!-- <bean class="cn.e3mall.common.jedis.JedisClientCluster" id="jedisClientCluster">
		<property name="jedisCluster" ref="jedisCluster"></property>
	</bean>
	<bean class="redis.clients.jedis.JedisCluster" id="jedisCluster">
		<constructor-arg name="nodes">
			<set>
				<bean class="redis.clients.jedis.HostAndPort">
					<constructor-arg name="host" value="192.168.1.144"></constructor-arg>
					<constructor-arg name="port" value="7001"></constructor-arg>
				</bean>
				<bean class="redis.clients.jedis.HostAndPort">
					<constructor-arg name="host" value="192.168.1.144"></constructor-arg>
					<constructor-arg name="port" value="7002"></constructor-arg>
				</bean>
				<bean class="redis.clients.jedis.HostAndPort">
					<constructor-arg name="host" value="192.168.1.144"></constructor-arg>
					<constructor-arg name="port" value="7003"></constructor-arg>
				</bean>
				<bean class="redis.clients.jedis.HostAndPort">
					<constructor-arg name="host" value="192.168.1.144"></constructor-arg>
					<constructor-arg name="port" value="7004"></constructor-arg>
				</bean>
				<bean class="redis.clients.jedis.HostAndPort">
					<constructor-arg name="host" value="192.168.1.144"></constructor-arg>
					<constructor-arg name="port" value="7005"></constructor-arg>
				</bean>
				<bean class="redis.clients.jedis.HostAndPort">
					<constructor-arg name="host" value="192.168.1.144"></constructor-arg>
					<constructor-arg name="port" value="7006"></constructor-arg>
				</bean>
			</set>
		</constructor-arg>
	</bean> -->
```
## Solr给搜索模块提供服务
本项目使用solrj来实现对索引库的维护。




































