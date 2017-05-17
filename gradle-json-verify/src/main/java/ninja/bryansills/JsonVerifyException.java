package ninja.bryansills;

public class JsonVerifyException extends RuntimeException {

    String fileName;

    public JsonVerifyException(String fileName, Throwable cause) {
        super(fileName, cause);
        this.fileName = fileName;
    }

    @Override
    public String getMessage() {
        return getCause().getMessage() + " in file: " + fileName;
    }
}
