package app.core.exeptions;

public class ClientServiceException extends RuntimeException{
    public ClientServiceException() {
    }

    public ClientServiceException(String message) {
        super(message);
    }

    public ClientServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
