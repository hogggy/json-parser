/**
 * Created by whogben on 5/10/16.
 */
import core.*;
import exceptions.JsonParserException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JsonParserTest {

    private JsonParser parser = new JsonParser();

    @Test
    public void testParseWithValidObjectInput() {
        String json = "{ \"value\":\"bacon\", \"list\": [ 1, 2, \"bacon\", null]}";
        JsonValue jval = parser.parse(json);
        List<JsonParameter> params = new ArrayList<JsonParameter>(2);
        params.add(new JsonParameter("value", new JsonString("bacon")));
        List<JsonValue> list = new ArrayList<JsonValue>(4);
        list.add(new JsonNumber(1));
        list.add(new JsonNumber(2));
        list.add(new JsonString("bacon"));
        list.add(new JsonNull());
        params.add(new JsonParameter("list", new JsonList(list)));
        JsonValue expected = new JsonObject(params);
        Assert.assertEquals(expected, jval);
    }

    @Test
    public void testParserWithValidListInput() {
        String json = "[ 1, 2, \"bacon\", null, { \"value\":\"bacon\"}]";
        JsonValue jval = parser.parse(json);
        List<JsonParameter> params = new ArrayList<JsonParameter>(1);
        params.add(new JsonParameter("value", new JsonString("bacon")));
        List<JsonValue> list = new ArrayList<JsonValue>(5);
        list.add(new JsonNumber(1));
        list.add(new JsonNumber(2));
        list.add(new JsonString("bacon"));
        list.add(new JsonNull());
        list.add(new JsonObject(params));
        JsonValue expected = new JsonList(list);
        Assert.assertEquals(expected, jval);
    }

    @Test
    public void testParserWithNestedObjects() {
        String json = "{ \"a\": 1, \"nest\":  {\"a\":2 }}";
        JsonValue jval = parser.parse(json);
        List<JsonParameter> params = new ArrayList<JsonParameter>(2);
        params.add(new JsonParameter("a", new JsonNumber(1)));
        List<JsonParameter> nestedParams = new ArrayList<JsonParameter>(1);
        nestedParams.add(new JsonParameter("a", new JsonNumber(2)));
        params.add(new JsonParameter("nest", new JsonObject(nestedParams)));
        JsonValue expected = new JsonObject(params);
        Assert.assertEquals(expected, jval);
    }

    @Test
    public void testDifferentNumberTypes() {
        String json = "[ 12e-2, 5, 1.2 ]";
        JsonValue jval = parser.parse(json);
        List<JsonValue> params = new ArrayList<JsonValue>(3);
        params.add(new JsonNumber(12e-2));
        params.add(new JsonNumber(5));
        params.add(new JsonNumber(1.2));
        JsonValue expected = new JsonList(params);
        Assert.assertEquals(expected, jval);
    }

    @Test
    public void testEmptyList() {
        String json = "[]";
        JsonValue jval = parser.parse(json);
        JsonValue expected = new JsonList(new ArrayList<JsonValue>(0));
        Assert.assertEquals(expected, jval);
    }

    @Test
    public void testEmptyObject() {
        String json = "{}";
        JsonValue jval = parser.parse(json);
        JsonValue expected = new JsonObject(new ArrayList<JsonParameter>(0));
        Assert.assertEquals(expected, jval);
    }

    @Test
    public void testValidEscapedCharacter() {
        String json = "[\"\\b\\n\\f\\t\\u\\\\\"]";
        JsonValue jval = parser.parse(json);
        List<JsonValue> list = new ArrayList<JsonValue>(1);
        list.add(new JsonString("\\b\\n\\f\\t\\u\\\\"));
        JsonValue expected = new JsonList(list);
        Assert.assertEquals(expected, jval);
    }

    @Test(expected = JsonParserException.class)
    public void testUnescapedBackSlash() {
        String json = "[\"\\\"]";
        JsonValue jval = parser.parse(json);
    }

    @Test(expected = JsonParserException.class)
    public void testUnfinishedParameter() {
        String json = "{\"bacon\":}";
        JsonValue jval = parser.parse(json);
    }

    @Test(expected = JsonParserException.class)
    public void testUnfinishedLeftOpenParameter() {
        String json = "{\"bacon\":   ";
        JsonValue jval = parser.parse(json);
    }

    @Test(expected = JsonParserException.class)
    public void testJustOpenBracket() {
        String json = "[";
        JsonValue jval = parser.parse(json);
    }

    @Test(expected = JsonParserException.class)
    public void testJustOpenBracketWithSpaces() {
        String json = "[   ";
        JsonValue jval = parser.parse(json);
    }

    @Test(expected = JsonParserException.class)
    public void testListMissingClosingBrace() {
        String json = "[ 1, 2, \"bacon\", null, { \"value\":\"bacon\"}";
        parser.parse(json);
    }

    @Test(expected = JsonParserException.class)
    public void testObjectMissingClosingBrace() {
        String json = "{ \"value\":\"bacon\"";
        parser.parse(json);
    }

    @Test(expected = JsonParserException.class)
    public void testNestedObjectMissingClosingBrace() {
        String json = "{ \"value\":\"bacon\", \"nest\": {\"asdf\":12 }";
        parser.parse(json);
    }

    @Test(expected = JsonParserException.class)
    public void testUnquotedString() {
        String json = "{ \"value\": bacon }";
        parser.parse(json);
    }

    @Test(expected = JsonParserException.class)
    public void testUnquotedParamName() {
        String json = "{ value :\"bacon\" }";
        parser.parse(json);
    }

}
