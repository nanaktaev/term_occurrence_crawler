package by.company.term_occurrence_crawler.content;

import org.jsoup.nodes.Document;

public interface DocumentFetcher {

    /**
     * Fetches web document by URL.
     *
     * @param url URL by which document is fetched.
     */
    Document fetch(String url);
}
