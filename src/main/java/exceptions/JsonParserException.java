package exceptions;

/**
 * Created by whogben on 5/5/16.
 */
public class JsonParserException extends IllegalArgumentException {

    public JsonParserException() { super(); }

    public JsonParserException(String msg) { super(msg); }
}
