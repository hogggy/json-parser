package core;

/**
 * Created by whogben on 5/5/16.
 */
public class JsonParameter {

    private String _name;
    private JsonValue _value;

    public JsonParameter(String name, JsonValue value) {
        _name = name;
        _value = value;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof JsonParameter)) {
            return false;
        }

        JsonParameter jp = (JsonParameter) other;

        return this._name.equals(jp._name) && this._value.equals(jp._value);
    }

    @Override
    public String toString() {
        return "\"" + _name + "\": " + _value.toString();
    }

}