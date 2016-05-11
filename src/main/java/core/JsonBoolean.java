package core;

/**
 * Created by whogben on 5/5/16.
 */
public class JsonBoolean implements JsonValue {

    private boolean _value;

    public JsonBoolean(boolean value) {
        _value = value;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof JsonBoolean)) {
            return false;
        }
        JsonBoolean jb = (JsonBoolean)other;

        return jb._value == this._value;
    }

    @Override
    public String toString() {
        if (this._value) {
            return "true";
        } else {
            return "false";
        }
    }

}
