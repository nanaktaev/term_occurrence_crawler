package by.company.term_occurrence_crawler.exceptions;

public class DocumentFetchingException extends RuntimeException {

    private final String message;

    public DocumentFetchingException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}

