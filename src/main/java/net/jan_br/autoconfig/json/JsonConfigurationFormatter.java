package net.jan_br.autoconfig.json;

import net.jan_br.autoconfig.ConfigurationFormatter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class JsonConfigurationFormatter implements ConfigurationFormatter<JsonConfigurationType> {

    public String[] format(JsonConfigurationType config) {
        List<String> formatted = new LinkedList<>();
        this.writeObject(0, null, config.root().unsafe(), formatted, false);
        return formatted.stream().toArray(String[]::new);
    }


    private void writeJSONObject(int spaces, String parentKey, JSONObject jsonObject, List<String> list, boolean expectNext) {
        if (parentKey == null) {
            list.add(this.generateSpaces(spaces) + "{");
        } else {
            list.add(this.generateSpaces(spaces) + "\"" + parentKey + "\"" + ":{");
        }

        List<String> strings = new LinkedList<>(jsonObject.keySet());
        for (int i = 0; i < strings.size(); i++) {
            Object o = jsonObject.get(strings.get(i));
            writeObject(spaces + 1, strings.get(i), o, list, i != strings.size() - 1);
        }
        list.add(this.generateSpaces(spaces) + "}" + (expectNext ? "," : ""));
    }

    private void writeJSONArray(int spaces, String parentKey, JSONArray object, List<String> list, boolean expectNext) {
        list.add(this.generateSpaces(spaces) + "\"" + parentKey + "\"" + ":[");
        for (int i = 0; i < object.size(); i++) {
            this.writeObject(spaces + 1, null, object.get(i), list, i != object.size() - 1);
        }
        list.add(this.generateSpaces(spaces) + "]" + (expectNext ? "," : ""));
    }

    private String generateSpaces(int spaces) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < spaces * 2; i++) {
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    private void writeObject(int spaces, String parentKey, Object o, List<String> list, boolean expectNext) {
        if (o instanceof JSONArray) {
            this.writeJSONArray(spaces, parentKey, ((JSONArray) o), list, expectNext);
        } else if (o instanceof JSONObject) {
            this.writeJSONObject(spaces, parentKey, (JSONObject) o, list, expectNext);
        } else {
            String stringRepresentation = this.generateSpaces(spaces) + (parentKey == null ? "" : ("\"" + parentKey + "\": "));

            if (o instanceof String) {
                stringRepresentation += "\"" + o + "\"";
            } else if (o instanceof Character) {
                stringRepresentation += "'" + o + "'";
            } else {
                stringRepresentation += o.toString();
            }
            if (expectNext) stringRepresentation += ",";

            list.add(stringRepresentation);
        }
    }

}
