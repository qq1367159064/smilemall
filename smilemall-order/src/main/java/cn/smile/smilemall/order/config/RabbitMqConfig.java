package cn.smile.smilemall.order.config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-02-27
 */
@Configuration
public class RabbitMqConfig {
	
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
	public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}
	
	@Bean
	public MessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}
	@PostConstruct
	public void initRabbitTemplate() {
		/**
		 * correlationData 当前消息的唯一关联数据(消息的唯一id)
		 * ack 消息是否成功收到
		 * cause 失败的原因
		 */
		rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
			System.out.println("消息确认");
		});
		/**
		 * 设置消息抵达队列的回调, 只要消息进队失败就会触发这个回调
		 * 参数
		 *   - Message 投递失败的消息详细的信息
		 *   - replyCode 回复的状态码
		 *   - replyText 回复的文本内容
		 *   - exchange  当时这个消息发送给的交换机
		 *   - routingKey  消息发送的时候指定的路由键
		 */
		rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
			
		});
	}
}
