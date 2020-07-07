package by.company.term_occurrence_crawler.content;

import by.company.term_occurrence_crawler.model.WebPage;
import by.company.term_occurrence_crawler.util.PropertiesManager;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ContentFetcher {

    private final LinkExtractor linkExtractor;
    private final PropertiesManager propertiesManager;
    private final DocumentFetcher jsoupDocumentFetcher;
    private final DocumentFetcher seleniumDocumentFetcher;

    public ContentFetcher(LinkExtractor linkExtractor,
                          PropertiesManager propertiesManager,
                          @Qualifier("jsoupDocumentFetcher") DocumentFetcher jsoupDocumentFetcher,
                          @Qualifier("seleniumDocumentFetcher") DocumentFetcher seleniumDocumentFetcher) {
        this.linkExtractor = linkExtractor;
        this.propertiesManager = propertiesManager;
        this.jsoupDocumentFetcher = jsoupDocumentFetcher;
        this.seleniumDocumentFetcher = seleniumDocumentFetcher;
    }

    /**
     * Fetches string with content to a WebPage.
     * If property "loading_js" is true
     * loads document via headless chrome browser,
     * else loads document via Jsoup without javascript content.
     *
     * @param webPage fetched content will be set to this WebPage
     */
    public void fetchContent(WebPage webPage) {

        Document document = propertiesManager.isLoadingJs()
                ? seleniumDocumentFetcher.fetch(webPage.getUrl())
                : jsoupDocumentFetcher.fetch(webPage.getUrl());

        setContentToDocument(webPage, document);
    }

    private void setContentToDocument(WebPage webPage, Document document) {
        StringBuilder content = new StringBuilder(document.body().text());

        for (Element element : document.getElementsByTag("input")) {
            content.append(" ").append(element.val());
        }

        webPage.setContent(content.toString());
        webPage.setChildWebPages(linkExtractor.extractLinksToWebPagesFromContent(document, webPage));
    }

}
