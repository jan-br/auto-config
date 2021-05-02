package de.jan_br.autoconfig.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import de.jan_br.autoconfig.Configuration;
import de.jan_br.autoconfig.ConfigurationAccessor;
import de.jan_br.autoconfig.ConfigurationType;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class JsonConfigurationType implements ConfigurationType {

  private final Configuration configuration;

  public JsonConfigurationType(Configuration configuration) {
    this.configuration = configuration;
  }

  public void init(ConfigurationAccessor accessor) {
    Gson gson = new Gson();
    File file = new File(configuration.path());
    if (!file.exists()) {
      accessor.loadDefaults();
      this.save(accessor);
    }
    try {
      Type mapType = new TypeToken<Map<String, Object>>() {
      }.getType();
      Map<String, Object> map =
          gson.fromJson(
              new String(
                  FileUtils.readFileToString(file, StandardCharsets.UTF_8).getBytes(StandardCharsets.UTF_8),
                  "UTF-8"),
              mapType);
      for (Map.Entry<String, Object> entry : map.entrySet()) {
        Field declaredField = accessor.getClass().getDeclaredField(entry.getKey());
        declaredField.setAccessible(true);

        Object value = entry.getValue();
        if (declaredField.getType().isAssignableFrom(double.class)) {
          declaredField.setDouble(accessor, ((Number) value).doubleValue());
        } else if (declaredField.getType().isAssignableFrom(int.class)) {
          declaredField.setInt(accessor, ((Number) value).intValue());
        } else if (declaredField.getType().isAssignableFrom(float.class)) {
          declaredField.setFloat(accessor, ((Number) value).floatValue());
        } else if (declaredField.getType().isAssignableFrom(byte.class)) {
          declaredField.setByte(accessor, ((Number) value).byteValue());
        } else if (declaredField.getType().isAssignableFrom(short.class)) {
          declaredField.setShort(accessor, ((Number) value).shortValue());
        } else if (declaredField.getType().isAssignableFrom(long.class)) {
          declaredField.setLong(accessor, ((Number) value).longValue());
        } else if (declaredField.getType().isAssignableFrom(boolean.class)) {
          declaredField.setBoolean(accessor, ((Boolean) value).booleanValue());
        } else if (declaredField.getType().isAssignableFrom(char.class)) {
          declaredField.setChar(accessor, ((Character) value).charValue());
        } else {
          if (declaredField.getType().isInstance(entry.getValue())) {
            declaredField.set(accessor, declaredField.getType().cast(entry.getValue()));
          } else {
            declaredField.set(accessor, gson.fromJson(gson.toJsonTree(entry.getValue()), declaredField.getType()));
          }
        }

      }
    } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  public void save(ConfigurationAccessor accessor) {
    File file = new File(configuration.path());
    try {
      System.setProperty("file.encoding", "ISO-8859-1");

      FileOutputStream fos = new FileOutputStream(file);
      String in = new GsonBuilder().setPrettyPrinting().create().toJson(accessor);
      fos.write(in.getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void delete(ConfigurationAccessor accessor) {
    Configuration configuration = accessor.getClass().getDeclaredAnnotation(Configuration.class);
    File file = new File(configuration.path());
    if (file.exists()) {
      file.delete();
    }
  }
}
