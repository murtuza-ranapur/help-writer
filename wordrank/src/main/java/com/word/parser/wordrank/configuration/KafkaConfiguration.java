package com.word.parser.wordrank.configuration;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Properties;

@Configuration
public class KafkaConfiguration {
    private static final String BOOTSTRAP_SERVERS = "localhost:10000,localhost:10001,localhost:10002";
    private static final String consumerGroup = "defaultGroup";
    private static final String TOPIC = "wordphrase";

    @Bean
    public Consumer<Long, String> createConsumer(){
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);//Just for logging
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 10);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);//Best practice. After consuming record consumer
        // may fail in that case we want producer to sent message again
        Consumer<Long, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singleton(TOPIC));
        return consumer;
    }
}
