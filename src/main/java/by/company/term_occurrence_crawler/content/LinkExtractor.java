package by.company.term_occurrence_crawler.content;

import by.company.term_occurrence_crawler.model.WebPage;
import com.google.common.collect.ImmutableList;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class LinkExtractor {

    /**
     * Extracts a set of child WebPages from parent WebPage's content.
     * Normalizes child pages URLs and skips malformed or disallowed ones.
     *
     * @param document is parsed to get from it all links.
     * @param parentWebPage parent WebPage is used to determine child WebPages depth
     *                      and to add parentURL's host to partial URLs (starting with "/")
     */
    Set<WebPage> extractLinksToWebPagesFromContent(Document document, WebPage parentWebPage) {

        Set<WebPage> webPages = new HashSet<>();
        int depth = parentWebPage.getDepth() + 1;
        String parentHost = getPageHost(parentWebPage);

        List<Element> linkElements = document.getElementsByTag("a");
        Set<String> webPageUrls = new HashSet<>();

        for (Element linkElement : linkElements) {
            webPageUrls.add(linkElement.attr("href"));
        }

        for (String url : webPageUrls) {

            boolean protocolAllowed = isProtocolAllowed(url);
            boolean extensionAllowed = isExtensionAllowed(url);

            if (StringUtils.isNotBlank(url) && protocolAllowed && extensionAllowed) {
                url = normalizeUrl(parentHost, url);

                WebPage webPage = new WebPage();
                webPage.setDepth(depth);
                webPage.setUrl(url);

                webPages.add(webPage);
            }
        }

        return webPages;
    }

    private String normalizeUrl(String parentHost, String url) {
        if (url.contains("#")) {
            url = StringUtils.substringBefore(url, "#");
        }
        if (url.startsWith("/")) {
            url = parentHost.concat(url);
        }
        if (url.contains(",")) {
            url = url.replaceAll(",", "%2");
        }
        return url;
    }

    private boolean isExtensionAllowed(String url) {
        boolean extensionAllowed = true;
        for (String disallowedExtension : DISALLOWED_EXTENSIONS) {
            if (url.endsWith(disallowedExtension)) {
                extensionAllowed = false;
                break;
            }
        }
        return extensionAllowed;
    }

    private boolean isProtocolAllowed(String url) {
        boolean protocolAllowed = false;
        for (String allowedProtocol : ALLOWED_PROTOCOLS) {
            if (url.startsWith("/") || url.startsWith(allowedProtocol)) {
                protocolAllowed = true;
                break;
            }
        }
        return protocolAllowed;
    }

    @SneakyThrows
    private String getPageHost(WebPage parentWebPage) {
        URL url = new URL(parentWebPage.getUrl());
        return url.getProtocol() + "://" + url.getHost();
    }

    private static final ImmutableList<String> ALLOWED_PROTOCOLS = ImmutableList.of(
            "https",
            "http",
            "ftp");

    private static final ImmutableList<String> DISALLOWED_EXTENSIONS = ImmutableList.of(
            ".pdf",
            ".jpeg",
            ".jpg",
            ".png");

}
