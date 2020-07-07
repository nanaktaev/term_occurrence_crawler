package by.company.term_occurrence_crawler.console;

import by.company.term_occurrence_crawler.exceptions.PropertiesInputException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@Component
public class CommandReader {

    private final Scanner scanner = new Scanner(System.in);

    private String readStringValue() {
        String value = scanner.nextLine();
        if (StringUtils.isBlank(value)) {
            throw new PropertiesInputException("Input canceled.");
        }
        return value;
    }

    String readUrl() {
        String value = readStringValue();
        try {
            new URL(value);
            return value;
        } catch (MalformedURLException e) {
            System.out.println("URL has to be correct.");
            return readUrl();
        }
    }

    List<String> readTerms() {
        String value = readStringValue();
        if (value.matches("^[a-zA-Zа-яА-Я0-9 ]+$")) {
            return Arrays.asList(value.split(" "));
        }
        System.out.println("Terms have to include only letters and numbers.");
        return readTerms();
    }

    int readNumericValue() {
        String value = readStringValue();
        if (StringUtils.isNumeric(value)) {
            return Integer.parseInt(value);
        }
        System.out.println("Input value has to be numeric.");
        return readNumericValue();
    }

    boolean readBooleanValue() {
        String value = readStringValue().toLowerCase();
        if (value.equals("true") || value.equals("false")) {
            return Boolean.parseBoolean(value);
        }
        System.out.println("Input value has to be 'true' or 'false'.");
        return readBooleanValue();
    }

}


