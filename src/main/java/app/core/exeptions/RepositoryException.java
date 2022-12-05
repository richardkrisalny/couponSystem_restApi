package app.core.exeptions;

public class RepositoryException extends RuntimeException{
    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepositoryException() {
    }
}
