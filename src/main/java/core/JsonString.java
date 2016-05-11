package core;

/**
 * Created by whogben on 5/5/16.
 */
public class JsonString implements JsonValue {

    private String _value;

    public JsonString(String value) {
        _value = value;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof JsonString)) {
            return false;
        }

        JsonString js = (JsonString) other;

        return js._value.equals(this._value);
    }

    @Override
    public String toString() {
        return "\"" + this._value + "\"";
    }

}
