package core;

import java.util.List;

/**
 * Created by whogben on 5/5/16.
 */
public class JsonObject implements JsonValue {

    private List<JsonParameter> _parameters;

    public JsonObject(List<JsonParameter> parameters) {
        _parameters = parameters;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof JsonObject)) {
            return false;
        }

        JsonObject jo = (JsonObject) other;
        if (jo._parameters.size() != this._parameters.size()) {
            return false;
        }

        for (int i = 0; i < this._parameters.size(); i++) {
            if (!this._parameters.get(i).equals(jo._parameters.get(i))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        String obj = "{";
        for (int i = 0; i < this._parameters.size() - 1; i++) {
            obj += this._parameters.get(i).toString() + ", ";
        }

        if (this._parameters.size() > 0) {
            obj += this._parameters.get(this._parameters.size() - 1);
        }

        return obj + "}";
    }

}



