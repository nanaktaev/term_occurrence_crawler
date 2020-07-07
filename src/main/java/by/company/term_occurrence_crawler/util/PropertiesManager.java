package by.company.term_occurrence_crawler.util;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Provides access to crawler's properties.
 */
@Getter
@Component
public class PropertiesManager {

    private String url = "https://en.wikipedia.org/wiki/Water";
    private List<String> terms = Arrays.asList("water", "world");
    private int maxPageVisits = 100;
    private int maxPageDepth = 3;
    private boolean wholeTermOnly = false;
    private boolean caseSensitive = false;
    private boolean diacriticSensitive = false;
    private boolean loadingJs = false;
    @Getter(AccessLevel.NONE)
    private final File PROPERTIES_FILE = new File("crawler.properties");

    /**
     * Creates properties file if it does not exist (and populates it with default values).
     * If file exists, reads it and stores values from it in this class.
     */
    @PostConstruct
    private void initPropertiesManager() {

        if (!PROPERTIES_FILE.exists()) {
            writeProperties();
        }

        try {
            Properties properties = new Properties();
            FileReader reader = new FileReader(PROPERTIES_FILE);
            properties.load(reader);
            reader.close();

            url = properties.getProperty("url");
            terms = Arrays.asList(properties.getProperty("terms").split(" "));
            maxPageVisits = Integer.parseInt(properties.getProperty("max_page_visits"));
            maxPageDepth = Integer.parseInt(properties.getProperty("max_page_depth"));
            wholeTermOnly = Boolean.parseBoolean(properties.getProperty("whole_term_only"));
            caseSensitive = Boolean.parseBoolean(properties.getProperty("case_sensitive"));
            diacriticSensitive = Boolean.parseBoolean(properties.getProperty("diacritic_sensitive"));
            loadingJs = Boolean.parseBoolean(properties.getProperty("loading_js"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes properties from this class's fields to the crawler.properties file.
     */
    private void writeProperties() {

        try {
            Properties properties = new Properties();
            properties.setProperty("url", url);
            properties.setProperty("terms", String.join(" ", terms));
            properties.setProperty("whole_term_only", String.valueOf(wholeTermOnly));
            properties.setProperty("case_sensitive", String.valueOf(caseSensitive));
            properties.setProperty("diacritic_sensitive", String.valueOf(diacriticSensitive));
            properties.setProperty("max_page_visits", String.valueOf(maxPageVisits));
            properties.setProperty("max_page_depth", String.valueOf(maxPageDepth));
            properties.setProperty("loading_js", String.valueOf(loadingJs));

            FileWriter writer = new FileWriter(PROPERTIES_FILE);
            properties.store(writer, "Crawler settings");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUrl(String url) {
        this.url = url;
        writeProperties();
    }

    public void setTerms(List<String> terms) {
        this.terms = terms;
        writeProperties();
    }

    public void setMaxPageVisits(int maxPageVisits) {
        this.maxPageVisits = maxPageVisits;
        writeProperties();
    }

    public void setMaxPageDepth(int maxPageDepth) {
        this.maxPageDepth = maxPageDepth;
        writeProperties();
    }

    public void setWholeTermOnly(boolean wholeTermOnly) {
        this.wholeTermOnly = wholeTermOnly;
        writeProperties();
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
        writeProperties();
    }

    public void setDiacriticSensitive(boolean diacriticSensitive) {
        this.diacriticSensitive = diacriticSensitive;
        writeProperties();
    }

    public void setLoadingJs(boolean loadingJs) {
        this.loadingJs = loadingJs;
        writeProperties();
    }

}
