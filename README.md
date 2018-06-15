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

#### 注册中心--zookeeper
1、可以作为集群的管理工具使用。<br>
2、可以集中管理配置文件。<br>
















