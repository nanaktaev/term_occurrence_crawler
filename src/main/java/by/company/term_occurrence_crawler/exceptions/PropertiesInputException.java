package by.company.term_occurrence_crawler.exceptions;

public class PropertiesInputException extends RuntimeException {

    private final String message;

    public PropertiesInputException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
