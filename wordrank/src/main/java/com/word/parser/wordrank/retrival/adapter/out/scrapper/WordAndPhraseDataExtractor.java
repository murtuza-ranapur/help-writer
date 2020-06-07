package com.word.parser.wordrank.retrival.adapter.out.scrapper;

import com.word.parser.wordrank.exception.WordNotFound;
import com.word.parser.wordrank.configuration.WebManager;
import com.word.parser.wordrank.retrival.application.out.GetWordPort;
import com.word.parser.wordrank.retrival.application.out.GetWordRankPort;
import lombok.AllArgsConstructor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class WordAndPhraseDataExtractor implements GetWordRankPort {
    public static final String URL = "https://www.wordandphrase.info/frequencyList.asp";
    public static final String QUERY_SECTION = "lefto";
    public static final String OUTPUT_SECTION = "righto";
    public static final String INPUT_BOX = "//*[@id=\"w1\"]";
    public static final String SUBMIT_BUTTON = "/html/body/table[1]/tbody/tr[1]/td[3]/input[1]";
    public static final String RANK = "/html/body/div/table[2]/tbody/tr[2]/td[2]/a";
    public static final Logger log= LoggerFactory.getLogger(WordAndPhraseDataExtractor.class);

    private int count;
    private WebManager webManager;

    public WordAndPhraseDataExtractor(WebManager webManager) {
        this.count = 0;
        this.webManager = webManager;
    }

    @PostConstruct
    public void init(){
        webManager.open(URL);
    }

    @Override
    public int getRank(String word) {
        try {
            if(count == 18){
                count = 0;
                resetPage();
            }
            webManager.moveToFrame(QUERY_SECTION);
            WebElement input = webManager.getElementByXPath(INPUT_BOX);
            input.clear();
            input.sendKeys(word);
            WebElement submitButton = webManager.getElementByXPath(SUBMIT_BUTTON);
            submitButton.click();
            count++;
            Thread.sleep(3000);
            webManager.moveToParent();
            webManager.moveToFrame(OUTPUT_SECTION);
            WebElement rank = webManager.getElementByXPath(RANK);
            return Integer.parseInt(rank.getText());
        }catch (NoSuchElementException | TimeoutException | InterruptedException no){
            log.error("Word not present: "+word, no);
            throw new WordNotFound();
        } finally {
            webManager.moveToParent();
            webManager.refresh();
        }
    }

    private void resetPage() {
        webManager.disconnect();
        webManager = WebManager.newInstance();
        init();
    }

    public static void main(String[] args) {
        
    }
}
