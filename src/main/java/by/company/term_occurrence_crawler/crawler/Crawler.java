package by.company.term_occurrence_crawler.crawler;

import by.company.term_occurrence_crawler.content.ContentFetcher;
import by.company.term_occurrence_crawler.content.OccurrenceCounter;
import by.company.term_occurrence_crawler.exceptions.DocumentFetchingException;
import by.company.term_occurrence_crawler.model.OccurrenceRecording;
import by.company.term_occurrence_crawler.model.WebPage;
import by.company.term_occurrence_crawler.output.CsvWriter;
import by.company.term_occurrence_crawler.util.DriverManager;
import by.company.term_occurrence_crawler.util.PropertiesManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
@RequiredArgsConstructor
@Slf4j
public class Crawler {

    private final ContentFetcher contentFetcher;
    private final PropertiesManager propertiesManager;
    private final OccurrenceCounter occurrenceCounter;
    private final DriverManager driverManager;
    private final CsvWriter csvWriter;

    private ConcurrentLinkedQueue<WebPage> webPageQueue;
    private Set<String> rejectedUrls;
    private Map<String, OccurrenceRecording> hitsByUrl;

    /**
     * Creates collections required for crawler's work.
     * Tries to {@link #crawl(WebPage)} initial WebPage.
     */
    public void initiateCrawler() {
        WebPage initialWebPage = new WebPage();
        initialWebPage.setUrl(propertiesManager.getUrl());
        initialWebPage.setDepth(0);

        hitsByUrl = new LinkedHashMap<>();
        rejectedUrls = new HashSet<>();
        webPageQueue = new ConcurrentLinkedQueue<>();

        webPageQueue.add(initialWebPage);

        try {
            crawl(initialWebPage);
        } catch (DocumentFetchingException e) {
            log.error("Initial page has been rejected. ({})", initialWebPage);
        }
    }

    /**
     * Core method of Term Occurrence Crawler.
     * Tries to fetch content to the WebPage
     * {@link by.company.term_occurrence_crawler.content.ContentFetcher#fetchContent(WebPage)}.
     * Makes a term occurrence recording based on that content
     * {@link by.company.term_occurrence_crawler.content.OccurrenceCounter#makeOccurrenceRecording(WebPage, Map)}.
     * After that it sends child pages to the crawler's queue {@link #sendChildPagesToQueue(WebPage)}
     * and tries to start recursively on a next page in that that queue {@link #crawlNextPage()}.
     *
     * @param webPage WebPage to be crawled
     */
    private void crawl(WebPage webPage) {

        webPageQueue.remove(webPageQueue.iterator().next());

        if (isPageVisitLimitReached() || isPageDepthLimitReached(webPage.getDepth())) return;

        contentFetcher.fetchContent(webPage);

        occurrenceCounter.makeOccurrenceRecording(webPage, hitsByUrl);

        log.info(hitsByUrl.size() + ". " + webPage);

        sendChildPagesToQueue(webPage);

        crawlNextPage();

        checkQueueForEmptiness();
    }

    private void crawlNextPage() {
        for (WebPage nextWebPage : webPageQueue) {
            try {
                crawl(nextWebPage);
                break;
            } catch (DocumentFetchingException e) {
                log.warn("Page rejected. ({})", nextWebPage);
                rejectedUrls.add(nextWebPage.getUrl());
                webPageQueue.remove(nextWebPage);
            }
        }
    }

    private void sendChildPagesToQueue(WebPage webPage) {
        for (WebPage childWebPage : webPage.getChildWebPages()) {
            if (!hitsByUrl.containsKey(childWebPage.getUrl())
                    && !webPageQueue.contains(childWebPage)
                    && !rejectedUrls.contains(childWebPage.getUrl())) {
                webPageQueue.add(childWebPage);
            }
        }
    }

    private boolean isPageDepthLimitReached(int currentDepth) {
        if (currentDepth > propertiesManager.getMaxPageDepth()) {
            log.info("Depth limit has been reached.");
            finishCrawling();
            return true;
        }
        return false;
    }

    private boolean isPageVisitLimitReached() {
        if (hitsByUrl.size() >= propertiesManager.getMaxPageVisits()) {
            log.info("Visits limit has been reached.");
            finishCrawling();
            return true;
        }
        return false;
    }

    /**
     * Terminates Selenium WebDriver (if any is running).
     * Sends crawling results to the {@link CsvWriter by.company.term_occurrence_crawler.output.CsvWriter}.
     */
    private void finishCrawling() {
        if (propertiesManager.isLoadingJs()) {
            driverManager.close();
        }
        csvWriter.write(hitsByUrl);
    }

    private void checkQueueForEmptiness() {
        if (webPageQueue.isEmpty()) {
            log.info("No more pages in a queue.");
            finishCrawling();
        }
    }

}
