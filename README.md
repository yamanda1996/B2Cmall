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
本项目使用solrj来实现对索引库的维护。<br>
### Solr-cloud
SolrCloud(solr 云)是Solr提供的分布式搜索方案，当需要大规模，容错，分布式索引和检索能力时使用 SolrCloud。
当一个系统的索引数据量少的时候是不需要使用SolrCloud的，当索引量很大，搜索请求并发很高，这时需要使用SolrCloud来满足这些需求。
SolrCloud是基于Solr和Zookeeper的分布式搜索方案，它的主要思想是使用Zookeeper作为集群的配置信息中心。<br>
它有几个特色功能：
* 集中式的配置信息
* 自动容错
* 近实时搜索
* 查询时自动负载均衡<br>
#### Solr-cloud系统架构图
![](https://github.com/yamanda1996/B2Cmall/blob/master/solr-cloud.png)<br>
物理结构：三个Solr实例（ 每个实例包括两个Core），组成一个SolrCloud。<br>
逻辑结构：索引集合包括两个Shard（shard1和shard2），shard1和shard2分别由三个Core组成，其中一个Leader两个Replication，Leader是由zookeeper选举产生，zookeeper控制每个shard上三个Core的索引数据一致，解决高可用问题。<br>
用户发起索引请求分别从shard1和shard2上获取，解决高并发问题。<br>
#### 本项目实现的solr集群架构
![](https://github.com/yamanda1996/B2Cmall/blob/master/solr-cloud2.png)<br>
使用Zookeeper作为集群的管理工具<br>
* 集群管理：容错、负载均衡。
* 配置文件的集中管理
* 集群的入口<br>
需要实现zookeeper 高可用。需要搭建集群。建议是奇数节点。需要三个zookeeper服务器。<br>
搭建solr集群需要7台服务器。<br>
搭建伪分布式：<br>
需要三个zookeeper节点、四个tomcat节点。<br>
## 使用activemq同步索引库
### activemq介绍
ActiveMQ 是Apache出品，最流行的，能力强劲的开源消息总线。ActiveMQ 是一个完全支持JMS1.1和J2EE 1.4规范的 JMS Provider实现,尽管JMS规范出台已经是很久的事情了,但是JMS在当今的J2EE应用中间仍然扮演着特殊的地位。<br>
### activemq的消息形式
对于消息的传递有两种类型：<br>
一种是点对点的，即一个生产者和一个消费者一一对应。<br>
另一种是发布/订阅模式，即一个生产者产生消息并进行发送后，可以由多个消费者进行接收。<br>
示意图如下：<br>
![](https://github.com/yamanda1996/B2Cmall/blob/master/activemq.png)<br>
JMS定义了五种不同的消息正文格式，以及调用的消息类型，允许你发送并接收以一些不同形式的数据，提供现有消息格式的一些级别的兼容性。<br>
* StreamMessage -- Java原始值的数据流
* MapMessage--一套名称-值对
* TextMessage--一个字符串对象
* ObjectMessage--一个序列化的 Java对象
* BytesMessage--一个字节的数据流<br>
## freemarker实现网页静态化
FreeMarker是一个用Java语言编写的模板引擎，它基于模板来生成文本输出。FreeMarker与Web容器无关，即在Web运行时，它并不知道Servlet或HTTP。它不仅可以用作表现层的实现技术，而且还可以用于生成XML，JSP或Java 等。<br>

















































