package by.company.term_occurrence_crawler.util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;

import java.io.Closeable;

/**
 * Provides access to Chrome Web Driver.
 * chromedriver.exe has to be located in the same directory with
 * crawler's executable jar.
 */
@Component
public class DriverManager implements Closeable {

    private ChromeDriver driver;

    public WebDriver getDriver() {
        if (driver == null) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments(
                    "--headless",
                    "--disable-gpu",
                    "--blink-settings=imagesEnabled=false",
                    "--start-maximized");
            driver = new ChromeDriver(options);
        }
        return this.driver;
    }

    @Override
    public void close() {
        if (driver != null) {
            this.driver.close();
        }
    }

}
