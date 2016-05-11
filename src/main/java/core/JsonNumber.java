package core;

/**
 * Created by whogben on 5/5/16.
 */
public class JsonNumber implements JsonValue {

    private double _value;

    public JsonNumber(double value) {
        _value = value;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof JsonNumber)) {
            return false;
        }
        JsonNumber jn = (JsonNumber) other;

        return jn._value == this._value;
    }

    @Override
    public String toString() {
        return "" + _value;
    }
}
