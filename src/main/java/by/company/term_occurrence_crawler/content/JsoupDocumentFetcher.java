package by.company.term_occurrence_crawler.content;

import by.company.term_occurrence_crawler.exceptions.DocumentFetchingException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class JsoupDocumentFetcher implements DocumentFetcher {

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) " +
            "AppleWebKit/535.2 (KHTML, like Gecko) " +
            "Chrome/15.0.874.120 Safari/535.2";

    @Override
    public Document fetch(String url) {
        try {
            return Jsoup.connect(url)
                    .userAgent(USER_AGENT)
                    .get();
        } catch (IOException | IllegalArgumentException e) {
            throw new DocumentFetchingException("Failed to fetch Jsoup document.");
        }
    }

}
