package cn.smile.smilemall.order;

import com.rabbitmq.client.Channel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-02-27
 */
@SpringBootTest
@Slf4j
public class RabbitMqTest {
	
	@Autowired
	private AmqpAdmin amqpAdmin;
	
	/**
	 * 创建Exchange 和Queue 和对应的绑定关系 AmqpAdmin
	 */
	@Test
	public void createExchange() {
		/**
		 * name 交换器名称
		 * durable 是否持久化
		 * autoDelete 是否自动删除
		 * arguments 指定参数
		 */
		Exchange directExchange = new DirectExchange("smile-exchange", true, false);
		amqpAdmin.declareExchange(directExchange);
	}
	
	@Test
	public void createQueue() {
		/**
		 * name 队列名
		 * durable 是否持久化
		 * exclusive 是否排他队列, 如果设置是排他的话就只能在声明的链接中使用
		 * autoDelete 是否自动删除
		 * arguments 指定参数
		 */
		Queue queue = new Queue("smile-queue", false, false, false);
		amqpAdmin.declareQueue(queue);
	}
	
	@Test
	public void createBinding() {
		/**
		 * destination 目的地 [可以是队列，也可以是交换器]
		 * destinationType 目的地的类型
		 * exchange 交换器
		 * routingKey 路由键
		 * arguments 指定义参数
		 */
		Binding binding = new Binding("smile-queue", Binding.DestinationType.QUEUE,
				"smile-exchange", "smile.queue.java", null);
		amqpAdmin.declareBinding(binding);
	}
	
	
	/*=====================================sendMessage=========================================*/
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Test
	public void sendMessage() {
		QueueEntity queueEntity = new QueueEntity();
		queueEntity.setId(1L);
		queueEntity.setName("obj");
		rabbitTemplate.convertAndSend("smile-exchange", "smile.queue.java", queueEntity,
				new CorrelationData(UUID.randomUUID().toString()));
		
	}
	
	@Data
	public static class QueueEntity implements Serializable {
		public final Long serialVersionUID = 1L;
		private Long id;
		private String name;
	}
	
	/**
	 * 接收的参数可以是
	 * -- Message
	 * -- 也可以是发送的时候是什么类型就用什么类型接收(消息体)
	 * -- Channel(通道)
	 *
	 * @RabbitListener 可以标在类上也可以标在方法上
	 * @RabbitHandler 只能标在方法上, 配合 @RabbitListener 使用
	 */
	@RabbitListener(queues = "smile-queue")
	public void listenerMessage(@Nullable Message message, QueueEntity queueEntity, Channel channel) throws IOException {
		MessageProperties header = message.getMessageProperties();
		System.out.println("接收到的消息" + queueEntity + "头部信息" + header + "通道" + channel);
		/**
		 * long deliveryTag 通道内自增的，消息序列号
		 * boolean multiple 是否批量确认, false 只确认自己当前的消息
		 */
		// 手动确认消息
		try {
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		} catch (IOException e) {
			channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
			e.printStackTrace();
		}
	}
	
}
