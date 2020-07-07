package by.company.term_occurrence_crawler.output;

import by.company.term_occurrence_crawler.model.OccurrenceRecording;
import by.company.term_occurrence_crawler.util.PropertiesManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Outputs crawling results to the CSV storage.
 */
@Component
@RequiredArgsConstructor
public class CsvWriter {

    private final PropertiesManager propertiesManager;

    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

    /**
     * Collects top ten occurrence recordings by total hits
     * from the map of all occurrence recordings
     * {@link #getTopTenPagesRecordings(Map)}.
     * Prepares file names based on current date time.
     * Creates directory "results" and puts new CSVs in it
     * (one with all recordings, second with top ten recordings).
     *
     * @param allPagesRecordings content of this map
     *                           will be written to CSVs.
     */
    public void write(Map<String, OccurrenceRecording> allPagesRecordings) {
        Map<String, OccurrenceRecording> topTenPagesRecordings = getTopTenPagesRecordings(allPagesRecordings);

        String currentDateTime = DATE_FORMAT.format(new Date());
        String allPagesFileName = "results/" + currentDateTime + ".csv";
        String topTenPagesFileName = "results/" + currentDateTime + " TOP.csv";

        File csvDirectory = new File("results");
        //noinspection ResultOfMethodCallIgnored
        csvDirectory.mkdir();

        try {
            writeToCsv(allPagesRecordings, allPagesFileName);
            writeToCsv(topTenPagesRecordings, topTenPagesFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void writeToCsv(Map<String, OccurrenceRecording> hitsByUrl, String fileName) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(new File(fileName));
        String resultString = createResultString(hitsByUrl);

        writer.write(resultString);
        writer.close();
    }

    private String createResultString(Map<String, OccurrenceRecording> hitsByUrl) {
        StringBuilder sb = new StringBuilder();

        sb.append("URL,");
        for (String term : propertiesManager.getTerms()) {
            sb.append(term).append(",");
        }
        sb.append("TOTAL");
        sb.append("\r\n");

        for (Map.Entry<String, OccurrenceRecording> entry : hitsByUrl.entrySet()) {
            OccurrenceRecording occurrenceRecording = entry.getValue();

            sb.append(entry.getKey()).append(",");

            for (int i = 0; i < occurrenceRecording.getHits().size(); i++) {
                sb.append(occurrenceRecording.getHits().get(i)).append(",");
            }

            sb.append(occurrenceRecording.getTotalHits());
            sb.append("\r\n");
        }
        return sb.toString();
    }

    private Map<String, OccurrenceRecording> getTopTenPagesRecordings(Map<String, OccurrenceRecording> allPagesRecordings) {
        return allPagesRecordings.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(10)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }

}
