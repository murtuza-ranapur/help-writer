package com.word.parser.wordrank.retrival.adapter.in.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.word.parser.wordrank.exception.WordNotFound;
import com.word.parser.wordrank.retrival.application.in.RetrieveIndexUseCase;
import com.word.parser.wordrank.retrival.domain.Word;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;

@Component
@Slf4j
@AllArgsConstructor
public class ConsumerAdapter {
    private final Consumer<Long, String> consumer;
    private final RetrieveIndexUseCase retrieveIndexUseCase;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void consumeMessage(){
        retriveIndexesOfWords();
        log.info("Initiated word retrival");
    }

    @Async
    public void retriveIndexesOfWords() {
        while (true){
            ConsumerRecords<Long, String> records = consumer.poll(Duration.ofSeconds(1));

            if(records.isEmpty()){
                //do nothing
            }

            for (ConsumerRecord<Long, String> record : records) {
                System.out.println(String.format("Record value (%d, %s), was sent to partition: %d, offset: %d",
                        record.key(), record.value(),record.partition(), record.offset()));
                try {
                    retrieveIndexUseCase.retrieveIndex(deserializeWord(record.value()));
                } catch (WordNotFound wordNotFound) {
                    //Do nothing
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            consumer.commitAsync();
        }
    }

    private Word deserializeWord(String word){
        try {
            return objectMapper.readValue(word, Word.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
