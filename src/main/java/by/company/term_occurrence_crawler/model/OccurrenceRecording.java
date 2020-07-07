package by.company.term_occurrence_crawler.model;

import by.company.term_occurrence_crawler.content.OccurrenceCounter;
import by.company.term_occurrence_crawler.crawler.Crawler;
import by.company.term_occurrence_crawler.output.CsvWriter;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * This domain object represents a recording which contains
 * numbers of hits by term and total hits on a given WebPage.
 * {@link OccurrenceCounter by.company.term_occurrence_crawler.content.OccurrenceCounter} creates
 * OccurrenceRecordings.
 * {@link Crawler by.company.term_occurrence_crawler.crawler.Crawler} accumulates them and
 * {@link CsvWriter by.company.term_occurrence_crawler.output.CsvWriter} outputs
 * them to the "results" directory.
 */
@Getter
@Setter
public class OccurrenceRecording implements Comparable<OccurrenceRecording> {

    private List<Integer> hits;
    private int totalHits;

    @Override
    public int compareTo(OccurrenceRecording anotherRecording) {
        return Integer.compare(totalHits, anotherRecording.getTotalHits());
    }

    public void addHits(int hits) {
        if (this.hits == null) this.hits = new ArrayList<>();
        this.hits.add(hits);
        this.totalHits += hits;
    }

}
