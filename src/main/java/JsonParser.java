import core.*;
import exceptions.JsonParserException;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class JsonParser {

    private Queue<Character> jQueue;

    public static void main(String[] args) {
        JsonParser parser = new JsonParser();
        parser.parse(args[0]);
    }

    /**
     * Parse the input json into a JsonValue object.
     *
     * @param json The json string to be parsed.
     *
     * @return Returns the resulting JsonValue object.
     */
    public JsonValue parse(String json) {
        // create the queue for processing the json
        jQueue = new ArrayBlockingQueue<Character>(json.length());
        for (int i = 0; i < json.length(); i++) {
            jQueue.add(json.charAt(i));
        }
        removeLeadingWhiteSpace();
        JsonValue jval;

        // a json text can only be a list or an object
        if (jQueue.peek() == '[') {
            jval = parseList();
        } else if (jQueue.peek() == '{') {
            jval = parseObject();
        } else {
            throw new JsonParserException("Provided json string must be either a list or an object.");
        }
        removeLeadingWhiteSpace();

        // we can't have any non whitespace left over
        if (jQueue.peek() != null) {
            throw new JsonParserException("Unexpected character at end of document: " + jQueue.peek());
        }

        return jval;
    }

    /**
     * Parses a valid json object from the front of the queue.
     *
     * @return The parsed JsonObject.
     */
    private JsonObject parseObject() {
        removeLeadingWhiteSpace();
        if (jQueue.poll() != '{') {
            throw new JsonParserException("Invalid leading character of object.");
        }
        checkNull();
        List<JsonParameter> params = new ArrayList<JsonParameter>(0);
        while (jQueue.peek() != '}') {
            params.add(parseParameter());
            removeLeadingWhiteSpace();
            checkNull();
            if (jQueue.peek() == '}') {
                break;
            }
            if (jQueue.poll() != ',') {
                throw new JsonParserException(
                    "Json object parameters should be separated by a comma."
                );
            }
        }
        // remove trailing bracket
        jQueue.poll();

        return new JsonObject(params);
    }

    /**
     * Parses a valid json list from the front of the queue.
     *
     * @return The parsed JsonList object.
     */
    private JsonList parseList() {
        removeLeadingWhiteSpace();
        if (jQueue.poll() != '[') {
            throw new JsonParserException("Invalid start of list.");
        }
        checkNull();
        List<JsonValue> values = new ArrayList<JsonValue>(0);
        while (jQueue.peek() != ']') {
            values.add(parseValue());
            removeLeadingWhiteSpace();
            checkNull();
            if (jQueue.peek() == ']') {
                break;
            }
            else if (jQueue.poll() != ',') {
                throw new JsonParserException("Json list parameters should be separated by a comma");
            }
        }
        // remove trailing bracket
        jQueue.poll();

        return new JsonList(values);
    }

    /**
     * Throws an exception if we reach the end of the file early.
     */
    private void checkNull() {
        this.removeLeadingWhiteSpace();
        if (jQueue.peek() == null) {
            throw new JsonParserException("Unexpected end of file!");
        }
    }

    /**
     * Takes the next quoted string and parses it into a JsonString object.
     *
     * @return The Parsed JsonString
     */
    private JsonString parseJString() {
        String str = parseString();

        return new JsonString(str);
    }

    /**
     * Parses the next available jsonValue or throws an exception if there is no valid value.
     *
     * @return JsonValue
     */
    private JsonValue parseValue() {
        checkNull();
        switch (jQueue.peek()) {
            case '{': return parseObject();
            case '[': return parseList();
            case '"': return parseJString();
            case 'n': parseUnquotedString("null");
                return new JsonNull();
            case 'f': parseUnquotedString("false");
                return new JsonBoolean(false);
            case 't': parseUnquotedString("true");
                return new JsonBoolean(true);
            default: return parseNumber();
        }
    }

    /**
     * Parses a number from the front of the queue or throws an exception if the front of the queue
     * does not contain a valid json number.
     *
     * @return The parsed JsonNumber object.
     */
    private JsonNumber parseNumber() {
        String numberString = "";
        while (jQueue.peek() != null && "\t \n,}]".indexOf(jQueue.peek()) == -1) {
            numberString += jQueue.poll();
        }
        try {
            Double dub = Double.parseDouble(numberString);
            return new JsonNumber(dub);

        } catch (NumberFormatException n) {
            throw new JsonParserException("Invalid number string: " + numberString);
        }
    }

    /**
     * Helper function for parsing special word values like false, true or null. Throws
     * an exception if the top of the queue does not match the provided check string.
     *
     * @param check String The expected unquoted word to be found in the queue.
     */
    private void parseUnquotedString(String check) {
        for (Integer i = 0; i < check.length(); i++) {
            if (jQueue.poll() != check.charAt(i)) {
                throw new JsonParserException("Invalid value. expected: " + check);
            }
        }
    }

    /**
     * Parses an object parameter from the front of the queue.
     *
     * @return Returns the parsed JsonParameter.
     */
    private JsonParameter parseParameter() {
        removeLeadingWhiteSpace();

        // we start off with a string
        String name = parseString();
        if (jQueue.peek() != ':') {
            throw new JsonParserException("Error parsing parameter: Missing colon separator");
        }
        jQueue.poll();
        JsonValue value = parseValue();
        return new JsonParameter(name, value);
    }

    /**
     * Removes any leading whitespace in the queue including newlines, tabs and spaces.
     */
    private void removeLeadingWhiteSpace() {
        while (jQueue.peek() != null && " \t\n".indexOf(jQueue.peek()) != -1) {
            jQueue.poll();
        }
    }

    /**
     * Parses a quoted string. Also looks for special characters that would be escaped.
     *
     * @return The parsed string.
     */
    private String parseString() {
        removeLeadingWhiteSpace();
        String str = "";
        checkNull();
        if (jQueue.poll() != '"') {
            throw new JsonParserException("String did not include leading quote.");
        }
        checkNull();
        while (jQueue.peek() != '"') {
            // check for valid escape sequences
            if (jQueue.peek() == '\\') {
                str += jQueue.poll();
                if ("\"\\/bfntu".indexOf(jQueue.peek()) == -1) {
                    throw new JsonParserException("Escape Backlash not followed by valid character");
                }
            }
            str += jQueue.poll();
            checkNull();
        }
        // remove trailing quote
        jQueue.poll();

        return str;
    }
}
