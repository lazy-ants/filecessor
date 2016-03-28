package com.lazyants.filecessor.configuration;

import com.lazyants.filecessor.model.PhotoRepository;
import com.lazyants.filecessor.service.ColorFinder;
import com.lazyants.filecessor.worker.Receiver;
import lombok.Data;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix="rabbitmq")
public class RabbitConfig extends BaseRabbitConfig {

    private int concurrency;

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(getQueueName());
        container.setConcurrentConsumers(getConcurrency());
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    @Autowired
    Receiver receiver(PhotoRepository repository, ApplicationConfiguration configuration) {
        return new Receiver(new ColorFinder(), repository, configuration);
    }

    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }
}
