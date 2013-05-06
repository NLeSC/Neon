package nl.esciencecenter.esight.exceptions;

public class FileOpeningException extends Exception {
    
    private static final long serialVersionUID = 0L;

    public FileOpeningException(String message) {
        super(message);
    }

    public FileOpeningException() {
        super();
    }

    public FileOpeningException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileOpeningException(Throwable cause) {
        super(cause);
    }
}
