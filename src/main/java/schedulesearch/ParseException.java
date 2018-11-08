package schedulesearch;

public class ParseException extends Exception {

    private int line;

    public ParseException(Throwable cause, int line) {
        super(cause);
        this.line = line;
    }

    public ParseException(String message, int line) {
        super(message);
        this.line = line;
    }

    public ParseException(String message, Throwable cause, int line) {
        super(message, cause);
        this.line = line;
    }

    public int getLine() {
        return line;
    }

    @Override
    public String getMessage() {
        String message = super.getMessage();
        return "At line " + line + ": " + message;
    }

}
