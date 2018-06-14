package cn.e3mall.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionConsumer;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

public class TestActiveMq {
	/**
	 * point to point点到点方式,发送消息
	 * 
	 * @throws Exception
	 */
	@Test
	public void fun1() throws Exception {
		// 创建一个连接工厂对象，需要指定服务的ip地址及端口
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.1.144:61616");
		// 使用工厂对象创建connection对象
		Connection connection = connectionFactory.createConnection();
		// 开启连接，调用对象的start方法
		connection.start();
		// 创建一个session对象
		// 第一个参数：是否开启事务（如果消息没发出去就重发），一般不开启事务,如果开启事务，第二个参数无意义
		// 第二个参数：应答模式，一般自动应答或者手动应答，一般自动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 使用session对象创建一个destination对象，两种形式，queue和topic
		Queue queue = session.createQueue("test_queue");
		// 使用session对象创建一个producer对象
		MessageProducer producer = session.createProducer(queue);
		// 创建一个message对象，使用textmessage
		/*
		 * TextMessage message = new ActiveMQTextMessage();
		 * message.setText("hello world");
		 */
		TextMessage message = session.createTextMessage("hello world");
		// 发送消息
		producer.send(message);
		// 关闭资源producer、session、connection
		producer.close();
		session.close();
		connection.close();
	}

	/**
	 * 接受消息consumer消费者
	 * 
	 * @throws Exception
	 */
	@Test
	public void fun2() throws Exception {
		// 创建一个connectionFactory对象连接MQ服务器
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.1.144:61616");
		// 创建一个连接对象
		Connection connection = connectionFactory.createConnection();
		// 开启连接
		connection.start();
		// 受用connection对象创建一个session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 创建一个destination对象，queue对象
		Queue queue = session.createQueue("test_queue");
		// 使用session对象创建一个消费者对象
		MessageConsumer consumer = session.createConsumer(queue);
		// 接受消息
		consumer.setMessageListener(new MessageListener() {

			@Override
			public void onMessage(Message message) {
				// 打印结果
				TextMessage textMessage = (TextMessage) message;
				try {
					String text = textMessage.getText();
					System.out.println(text);
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		// 等待接受消息
		System.in.read(); // 等待敲击键盘
		// 关闭资源
		consumer.close();
		session.close();
		connection.close();

	}

	/**
	 * topic模式的发送消息
	 * @throws JMSException 
	 */
	@Test
	public void fun3() throws JMSException {
		// 创建一个连接工厂对象，需要指定服务的ip地址及端口
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.1.144:61616");
		// 使用工厂对象创建connection对象
		Connection connection = connectionFactory.createConnection();
		// 开启连接，调用对象的start方法
		connection.start();
		// 创建一个session对象
		// 第一个参数：是否开启事务（如果消息没发出去就重发），一般不开启事务,如果开启事务，第二个参数无意义
		// 第二个参数：应答模式，一般自动应答或者手动应答，一般自动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 使用session对象创建一个destination对象，两种形式，queue和topic
		Topic topic = session.createTopic("test_topic");
		// 使用session对象创建一个producer对象
		MessageProducer producer = session.createProducer(topic);
		// 创建一个message对象，使用textmessage
		/*
		 * TextMessage message = new ActiveMQTextMessage();
		 * message.setText("hello world");
		 */
		TextMessage message = session.createTextMessage("hello world");
		// 发送消息
		producer.send(message);
		// 关闭资源producer、session、connection
		producer.close();
		session.close();
		connection.close();
	}
	/**
	 * topic模式消费者
	 * @throws Exception
	 */
	@Test
	public void fun4() throws Exception {
		// 创建一个connectionFactory对象连接MQ服务器
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.1.144:61616");
		// 创建一个连接对象
		Connection connection = connectionFactory.createConnection();
		// 开启连接
		connection.start();
		// 受用connection对象创建一个session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 创建一个destination对象，queue对象
		Topic topic = session.createTopic("test_topic");
		// 使用session对象创建一个消费者对象
		MessageConsumer consumer = session.createConsumer(topic);
		// 接受消息
		consumer.setMessageListener(new MessageListener() {

			@Override
			public void onMessage(Message message) {
				// 打印结果
				TextMessage textMessage = (TextMessage) message;
				try {
					String text = textMessage.getText();
					System.out.println(text);
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		System.out.println("消费者22已经启动");
		// 等待接受消息
		System.in.read(); // 等待敲击键盘输入，以敲击回车结束
		// 关闭资源
		consumer.close();
		session.close();
		connection.close();

	}

}
