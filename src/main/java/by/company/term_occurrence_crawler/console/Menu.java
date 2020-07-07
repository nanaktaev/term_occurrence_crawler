package by.company.term_occurrence_crawler.console;

import by.company.term_occurrence_crawler.crawler.Crawler;
import by.company.term_occurrence_crawler.exceptions.PropertiesInputException;
import by.company.term_occurrence_crawler.util.PropertiesManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class Menu {

    private final PropertiesManager propertiesManager;
    private final Crawler crawler;
    private final CommandReader commandReader;

    private final Scanner scanner = new Scanner(System.in);

    public void openMainMenu() {
        System.out.println("\n--- TERM OCCURRENCE CRAWLER ---\n");
        System.out.println("" +
                "URL: " + propertiesManager.getUrl() + "\n" +
                "Terms: " + String.join(" ", propertiesManager.getTerms()));
        System.out.println("\n" +
                "run - start crawling\n" +
                "prop - change properties\n" +
                "quit - shut down crawler\n");
        readMainMenuCommand();
    }

    private void openPropertiesMenu() {
        System.out.println("\n--- CRAWLER'S PROPERTIES ---\n");
        System.out.println("" +
                "url    - URL: " + propertiesManager.getUrl() + "\n" +
                "term   - Terms: " + String.join(" ", propertiesManager.getTerms()) + "\n" +
                "vis    - Max page visits: " + propertiesManager.getMaxPageVisits() + "\n" +
                "depth  - Max page depth: " + propertiesManager.getMaxPageDepth() + "\n" +
                "case   - Case sensitive: " + propertiesManager.isCaseSensitive() + "\n" +
                "dia    - Diacritic sensitive: " + propertiesManager.isDiacriticSensitive() + "\n" +
                "whole  - Whole term only: " + propertiesManager.isWholeTermOnly() + "\n" +
                "js     - Load JavaScript: " + propertiesManager.isLoadingJs() + "\n\n" +
                "back   - back to main menu\n");
        readPropertiesMenuCommand();
    }

    private void readPropertiesMenuCommand() {
        try {
            String value = scanner.nextLine().toLowerCase();
            switch (value) {
                case "url":
                    propertiesManager.setUrl(commandReader.readUrl());
                    openPropertiesMenu();
                    break;
                case "term":
                    propertiesManager.setTerms(commandReader.readTerms());
                    openPropertiesMenu();
                    break;
                case "vis":
                    propertiesManager.setMaxPageVisits(commandReader.readNumericValue());
                    openPropertiesMenu();
                    break;
                case "depth":
                    propertiesManager.setMaxPageDepth(commandReader.readNumericValue());
                    openPropertiesMenu();
                    break;
                case "case":
                    propertiesManager.setCaseSensitive(commandReader.readBooleanValue());
                    openPropertiesMenu();
                    break;
                case "dia":
                    propertiesManager.setDiacriticSensitive(commandReader.readBooleanValue());
                    openPropertiesMenu();
                    break;
                case "whole":
                    propertiesManager.setWholeTermOnly(commandReader.readBooleanValue());
                    openPropertiesMenu();
                    break;
                case "js":
                    propertiesManager.setLoadingJs(commandReader.readBooleanValue());
                    openPropertiesMenu();
                    break;
                case "back":
                    openMainMenu();
                    break;
                default:
                    System.out.println("Invalid command.");
                    readPropertiesMenuCommand();
            }
        } catch (PropertiesInputException e) {
            System.out.println(e.getMessage());
            openPropertiesMenu();
        }
    }

    private void readMainMenuCommand() {
        String value = scanner.nextLine().toLowerCase();
        switch (value) {
            case "run":
                crawler.initiateCrawler();
                break;
            case "prop":
                openPropertiesMenu();
                break;
            case "quit":
                break;
            default:
                System.out.println("Invalid command.");
                readMainMenuCommand();
        }
    }

}
