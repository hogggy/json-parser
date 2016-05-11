package core;

import java.util.List;

/**
 * Created by whogben on 5/5/16.
 */
public class JsonList implements JsonValue {

    private List<JsonValue> _values;

    public JsonList(List<JsonValue> values) {
        _values = values;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof JsonList)) {
            return false;
        }
        JsonList jl = (JsonList)other;
        if (jl._values.size() != this._values.size()) {
            return false;
        }
        for (int i = 0; i < this._values.size(); i++) {
            if (!this._values.get(i).equals(jl._values.get(i))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return _values.toString();
    }

}
