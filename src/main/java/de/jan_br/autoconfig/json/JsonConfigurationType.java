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
import java.nio.charset.Charset;
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
      Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
      Map<String, Object> map =
          gson.fromJson(
              new String(
                  FileUtils.readFileToString(file, Charset.defaultCharset()).getBytes("ISO-8859-1"),
                  "UTF-8"),
              mapType);
      for (Map.Entry<String, Object> entry : map.entrySet()) {
        Field declaredField = accessor.getClass().getDeclaredField(entry.getKey());
        declaredField.setAccessible(true);
        declaredField.set(accessor, entry.getValue());
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
