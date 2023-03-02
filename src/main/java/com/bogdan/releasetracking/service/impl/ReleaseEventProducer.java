package com.bogdan.releasetracking.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReleaseEventProducer {

    private static final Logger LOG = LoggerFactory.getLogger(ReleaseEventProducer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public ReleaseEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendReleaseEvent(String topic, String payload) {
        LOG.info("sending payload='{}' to topic='{}'", payload, topic);
        kafkaTemplate.send(topic, payload);
    }

}
