package core;

/**
 * Created by whogben on 5/5/16.
 */
public class JsonNull implements JsonValue {

    @Override
    public boolean equals(Object other) {
        return other instanceof JsonNull;
    }

    @Override
    public String toString() {
        return "null";
    }
}
