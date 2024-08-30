package com.bumingzeyi.events.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author :BuMing
 * @date : 2022-04-04 20:58
 */
@Configuration
public class RabbitMQConfig {

    // 配置队列
    @Bean
    public Queue queue(){
        return new Queue("eventsQueue");
    }

    // 配置交换机
    @Bean
    public TopicExchange exchange(){
        return new TopicExchange("eventsExchange");
    }

    // 配置交换机与队列绑定关系
    @Bean
    public Binding bindingExchangeMessage(Queue queue, TopicExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("events");
    }

}
