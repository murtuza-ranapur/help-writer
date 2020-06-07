package com.word.parser.commons;

import org.jsoup.HttpStatusException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MerriumBaseWordServiceTest {

    @Test
    void getBaseWord_positive() throws IOException {
        MerriumBaseWordService merriumBaseWordService = new MerriumBaseWordService();
        String expected = "thank";
        assertEquals(expected, merriumBaseWordService.getBaseWord("thanked"));
    }

    @Test
    void getBaseWord_negative(){
        MerriumBaseWordService merriumBaseWordService = new MerriumBaseWordService();
        assertThrows(HttpStatusException.class, ()->{
            merriumBaseWordService.getBaseWord("jerad");
        });
    }
}