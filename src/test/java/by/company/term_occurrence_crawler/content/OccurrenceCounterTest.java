package by.company.term_occurrence_crawler.content;

import by.company.term_occurrence_crawler.model.OccurrenceRecording;
import by.company.term_occurrence_crawler.model.WebPage;
import by.company.term_occurrence_crawler.util.PropertiesManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class OccurrenceCounterTest {

    private OccurrenceCounter occurrenceCounter;
    private final ContentProcessor contentProcessor = Mockito.mock(ContentProcessor.class);
    private final PropertiesManager propertiesManager = Mockito.mock(PropertiesManager.class);

    private static WebPage sampleWebPage;
    private static Map<String, OccurrenceRecording> sampleHitsByUrl;
    private final List<String> SAMPLE_TERMS = Arrays.asList("some", "text", "Here");
    @SuppressWarnings("FieldCanBeLocal")
    private final String samplePreparedContentCase = "Some sample text written by somebody Here is a word texting";
    private final String samplePreparedContent = "some sample text written by somebody here is a word texting";

    @BeforeAll
    static void prepareForTests() {
        sampleWebPage = new WebPage();
        sampleWebPage.setUrl("https://sampleurl.com");
        sampleWebPage.setContent("Here is some sample text, written by somebody. Here is a word 'texting'.");

        sampleHitsByUrl = new LinkedHashMap<>();
    }

    @BeforeEach
    void initOccurrenceCounter() {
        occurrenceCounter = new OccurrenceCounter(propertiesManager, contentProcessor);
    }

    @Test
    void onlyWholeTermOccurrenceIsCounted() {
        when(propertiesManager.getTerms()).thenReturn(SAMPLE_TERMS);
        when(propertiesManager.isWholeTermOnly()).thenReturn(true);
        when(contentProcessor.prepareContent(sampleWebPage.getContent())).thenReturn(samplePreparedContent);

        occurrenceCounter.makeOccurrenceRecording(sampleWebPage, sampleHitsByUrl);

        assertEquals(3, sampleHitsByUrl.get(sampleWebPage.getUrl()).getTotalHits());
    }

    @Test
    void allOccurrenceIsCounted() {
        when(propertiesManager.getTerms()).thenReturn(SAMPLE_TERMS);
        when(contentProcessor.prepareContent(sampleWebPage.getContent())).thenReturn(samplePreparedContent);

        occurrenceCounter.makeOccurrenceRecording(sampleWebPage, sampleHitsByUrl);

        assertEquals(5, sampleHitsByUrl.get(sampleWebPage.getUrl()).getTotalHits());
    }

    @Test
    void allOccurrenceIsCountedCaseSensitive() {
        when(propertiesManager.getTerms()).thenReturn(SAMPLE_TERMS);
        when(propertiesManager.isCaseSensitive()).thenReturn(true);
        when(contentProcessor.prepareContent(sampleWebPage.getContent())).thenReturn(samplePreparedContentCase);

        occurrenceCounter.makeOccurrenceRecording(sampleWebPage, sampleHitsByUrl);

        assertEquals(4, sampleHitsByUrl.get(sampleWebPage.getUrl()).getTotalHits());
    }

}
