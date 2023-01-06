package app.core.exeptions;

public class LoginException extends RuntimeException{
    public LoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginException() {
    }

    public LoginException(String s) {
        super(s);
    }
}
