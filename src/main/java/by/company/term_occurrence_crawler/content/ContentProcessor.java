package by.company.term_occurrence_crawler.content;

import by.company.term_occurrence_crawler.util.PropertiesManager;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContentProcessor {

    private final PropertiesManager propertiesManager;

    /**
     * Prepares content before it will be analyzed by
     * {@link OccurrenceCounter by.company.term_occurrence_crawler.content.OccurrenceCounter}.
     *
     * @param content content to be prepared.
     */
    public String prepareContent(String content) {
        if (!propertiesManager.isDiacriticSensitive()) {
            content = StringUtils.stripAccents(content);
        }
        if (!propertiesManager.isCaseSensitive()) {
            content = StringUtils.lowerCase(content);
        }
        if (propertiesManager.isWholeTermOnly()) {
            content = content.replaceAll("[^a-zA-Zа-яА-Я0-9 ]", " ");
        }

        return content;
    }

}
