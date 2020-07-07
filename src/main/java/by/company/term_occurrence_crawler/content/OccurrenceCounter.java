package by.company.term_occurrence_crawler.content;

import by.company.term_occurrence_crawler.model.OccurrenceRecording;
import by.company.term_occurrence_crawler.model.WebPage;
import by.company.term_occurrence_crawler.output.CsvWriter;
import by.company.term_occurrence_crawler.util.PropertiesManager;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OccurrenceCounter {

    private final PropertiesManager propertiesManager;
    private final ContentProcessor contentProcessor;

    /**
     * Counts term occurrence in WebPage's content and puts it in a result map.
     *
     * @param webPage it's content will be scanned for term occurrence,
     *                it's URL will be the key of a new result map entry.
     * @param hitsByUrl result map, where all OccurrenceRecordings are stored
     *                  by the key of WebPage's URL.
     *                  Later this map will be written to CSV file at
     *                  {@link CsvWriter by.company.term_occurrence_crawler.output.CsvWriter}.
     */
    public void makeOccurrenceRecording(WebPage webPage, Map<String, OccurrenceRecording> hitsByUrl) {
        String content = contentProcessor.prepareContent(webPage.getContent());
        String url = webPage.getUrl();
        List<String> terms = prepareTerms(propertiesManager.getTerms());

        OccurrenceRecording occurrenceRecording = propertiesManager.isWholeTermOnly()
                ? getWholeTermOnlyOccurrenceRecording(content, terms)
                : getAllOccurrenceRecording(content, terms);

        hitsByUrl.put(url, occurrenceRecording);
    }

    private OccurrenceRecording getWholeTermOnlyOccurrenceRecording(String content, List<String> terms) {
        OccurrenceRecording occurrenceRecording = new OccurrenceRecording();
        String[] contentArray = content.split(" ");

        for (String term : terms) {
            int hits = (int) Arrays.stream(contentArray).filter(word -> word.equals(term)).count();
            occurrenceRecording.addHits(hits);
        }
        return occurrenceRecording;
    }

    private OccurrenceRecording getAllOccurrenceRecording(String content, List<String> terms) {
        OccurrenceRecording occurrenceRecording = new OccurrenceRecording();
        for (String term : terms) {
            int hits = StringUtils.countMatches(content, term);
            occurrenceRecording.addHits(hits);
        }
        return occurrenceRecording;
    }

    private List<String> prepareTerms(List<String> terms) {
        List<String> preparedTerms = new ArrayList<>();

        for (String term : terms) {
            if (!propertiesManager.isCaseSensitive()) {
                term = StringUtils.lowerCase(term);
            }
            if (!propertiesManager.isDiacriticSensitive()) {
                term = StringUtils.stripAccents(term);
            }
            preparedTerms.add(term);
        }

        return preparedTerms;
    }

}
