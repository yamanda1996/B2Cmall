package cn.e3mall.activemq;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class SpringWithActivemqTest {

	@Test
	public void fun1() {
		// 初始化Spring容器
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:spring/applicationContext-activemq.xml");
		// 从spring容器中获得jmsTemplate对象
		JmsTemplate jmsTemplate = applicationContext.getBean(JmsTemplate.class);
		// 从容器中获得destination对象
		// ActiveMQQueue queue =
		// applicationContext.getBean(ActiveMQQueue.class);
		// 根据id取对象
		Destination queue = (Destination) applicationContext.getBean("queueDestination");

		// 发送消息
		jmsTemplate.send(queue, new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				
				return session.createTextMessage("hello world");
			}
		});

	}
}
