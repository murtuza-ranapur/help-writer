package com.word.parser.commons;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

public class WebManager {
    private static final Logger log = LoggerFactory.getLogger(WebDriver.class);
    private final WebDriver webDriver;
    private final WebDriverWait webDriverWait;
    private final Actions actions;

    private WebManager(WebDriver webDriver){
        this.webDriver = webDriver;
        this.webDriver.manage().window().maximize();
        this.webDriverWait = new WebDriverWait(webDriver,30);
        this.actions = new Actions(webDriver);
    }

    public static WebManager newInstance(){
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--window-size=1920x1080");
        return new WebManager(new ChromeDriver(chromeOptions));
    }

    public void disconnect() {
        try {
            webDriver.close();
        }catch (Exception ex){
            log.error("Failed to close the web driver", ex);
        }
    }

    public void open(String url) {
        webDriver.get(url);
    }

    public WebElement getElementByXPath(String xpath) {
        waitForElement(xpath);
        return webDriver.findElement(By.xpath(xpath));
    }

    public List<WebElement> getElementByClass(String className) {
        waitForElementClass(className);
        return webDriver.findElements(By.className(className));
    }

    public WebElement getElementByXPath(WebElement element, String xpath) {
        return element.findElement(By.xpath(xpath));
    }

    public List<WebElement> getElementsByTag(WebElement element, String tag) {
        return element.findElements(By.tagName(tag));
    }

    public List<WebElement> getElementsByTag(String tag) {
        return webDriver.findElements(By.tagName(tag));
    }

    public void moveToFrame(String id) {
        webDriver.switchTo().frame(id);
    }

    public void moveToParent() {
        webDriver.switchTo().parentFrame();
    }

    public void refresh() {
        webDriver.navigate().refresh();
    }

    public void waitForElement(String xpath) {
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
    }

    private void waitForElementClass(String classname) {
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className(classname)));
    }

    @SneakyThrows
    public void scrollToElement(WebElement element){
        actions.moveToElement(element);
        actions.perform();
    }

    public void phantomScroll(){
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    @SneakyThrows
    public void screenShot(){
        TakesScreenshot scrShot = (TakesScreenshot) webDriver;
        File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
        File str = new File("C:\\Users\\Admin\\Pictures\\ss.jpg");
        FileUtils.copyFile(SrcFile,str);
    }
}
