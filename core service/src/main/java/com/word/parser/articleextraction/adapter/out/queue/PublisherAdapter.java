package com.word.parser.articleextraction.adapter.out.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.word.parser.articleextraction.Word;
import com.word.parser.articleextraction.application.port.out.PublishDataPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@AllArgsConstructor
@Slf4j
public class PublisherAdapter implements PublishDataPort {

    private static final String TOPIC = "wordphrase";
    private final Producer<Long, String> kafkaProducer;
    private final ObjectMapper objectMapper;

    @Override
    public void pushWord(Word word) {
        try{
            produceMessageMessages(kafkaProducer, word);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            log.error("Error in distributing words");
        }
    }

    private void produceMessageMessages(Producer<Long, String> producer, Word word) throws ExecutionException, InterruptedException {
        ProducerRecord<Long, String> record = new ProducerRecord<>(TOPIC,serializeToString(word));
        RecordMetadata metadata = producer.send(record).get();
        System.out.println(String.format("Record value (%d, %s), was sent to partition: %d, offset: %d",
                record.key(), record.value(),metadata.partition(), metadata.offset()));
    }

    private String serializeToString(Word word) {
        try {
            return objectMapper.writeValueAsString(word);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize word :"+word, e);
            throw new RuntimeException();
        }
    }
}
