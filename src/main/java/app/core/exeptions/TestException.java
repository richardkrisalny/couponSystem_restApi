package app.core.exeptions;

public class TestException extends RuntimeException{
    public TestException(String message, Throwable cause) {
        super(message, cause);
    }

    public TestException() {
    }
}
