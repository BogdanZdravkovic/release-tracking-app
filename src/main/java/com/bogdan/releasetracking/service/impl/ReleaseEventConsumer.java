package com.bogdan.releasetracking.service.impl;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ReleaseEventConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(ReleaseEventProducer.class);

//    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
//    -> TODO @Kafka Listener disabled because of missing configuraiton
    public void consume(ConsumerRecord<String, String> payload){
        LOG.info("key:", payload.key());
    }
}
