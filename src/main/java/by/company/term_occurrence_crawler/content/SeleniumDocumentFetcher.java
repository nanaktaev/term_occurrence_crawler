package by.company.term_occurrence_crawler.content;

import by.company.term_occurrence_crawler.util.DriverManager;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeleniumDocumentFetcher implements DocumentFetcher {

   private final DriverManager driverManager;

    @Override
    public Document fetch(String url) {
        WebDriver driver = driverManager.getDriver();
        driver.get(url);

        JavascriptExecutor javascriptExecutor = ((JavascriptExecutor) driver);
        javascriptExecutor.executeScript("window.scrollTo(0, document.body.scrollHeight);");

        return Jsoup.parse(driver.getPageSource());
    }

}
