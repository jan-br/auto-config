package net.jan_br.autoconfig.json;

import com.google.common.base.Preconditions;
import net.jan_br.autoconfig.ConfigurationPath;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Arrays;
import java.util.Collection;

public class JsonConfigurationPath implements ConfigurationPath<JSONObject> {

    private final JSONObject jsonObject;

    private JsonConfigurationPath(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public JsonConfigurationPath getPath(String path) {
        if (!this.jsonObject.containsKey(path)) {
            this.jsonObject.put(path, new JSONObject());
        }
        return create((JSONObject) this.jsonObject.get(path));
    }

    public Collection<String> getKeys() {
        return this.jsonObject.keySet();
    }

    public Object get(String key) {
        return this.jsonObject.get(key);
    }

    public JsonConfigurationPath set(String key, Object value) {
        if (value == null) return this;
        if (value.getClass().isArray()) {
            JSONArray jsonArray = new JSONArray();
            jsonArray.addAll(Arrays.asList(this.toObjectArray(value)));
            this.jsonObject.put(key, jsonArray);
            return this;
        }
        this.jsonObject.put(key, value);
        return this;
    }

    public JSONObject unsafe() {
        return this.jsonObject;
    }

    public static JsonConfigurationPath create(JSONObject jsonObject) {
        Preconditions.checkNotNull(jsonObject);
        return new JsonConfigurationPath(jsonObject);
    }


}
