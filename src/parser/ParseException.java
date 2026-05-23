package parser;

/** Грешка при парсване на XML. */
public class ParseException extends RuntimeException {
    public final int line;
    public final int column;

    public ParseException(String message, int line, int column) {
        super(message + " (line " + line + ", col " + column + ")");
        this.line = line;
        this.column = column;
    }
}
