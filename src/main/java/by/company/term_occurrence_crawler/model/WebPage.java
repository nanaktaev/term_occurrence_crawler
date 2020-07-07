package by.company.term_occurrence_crawler.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

/**
 * Key domain object of Term Occurrence Crawler.
 * Represents a web page with a set of it's children.
 * Equality of WebPages is checked solely by URL equality.
 */
@Getter
@Setter
public class WebPage {

    private String url;
    private int depth;
    private String content;
    private Set<WebPage> childWebPages;

    @Override
    public String toString() {
        return "Depth = " + depth + ", URL = " + url;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof WebPage) {
            WebPage anotherWebPage = (WebPage) o;
            return url.equals(anotherWebPage.getUrl());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }

}
