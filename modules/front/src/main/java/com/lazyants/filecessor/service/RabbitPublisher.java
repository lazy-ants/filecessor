package com.lazyants.filecessor.service;

import com.lazyants.filecessor.configuration.BaseRabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitPublisher {
    private final RabbitTemplate template;

    private final BaseRabbitConfig config;

    @Autowired
    public RabbitPublisher(RabbitTemplate template, BaseRabbitConfig config) {
        this.template = template;
        this.config = config;
    }

    public void publishPhotoId(String id) {
        template.convertAndSend(config.getQueueName(), id);
    }
}
