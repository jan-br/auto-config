package net.jan_br.autoconfig.json;

import net.jan_br.autoconfig.Configuration;
import net.jan_br.autoconfig.ConfigurationAccessor;
import net.jan_br.autoconfig.ConfigurationFormatter;
import net.jan_br.autoconfig.ConfigurationType;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.util.Arrays;

public class JsonConfigurationType implements ConfigurationType<JsonConfigurationPath> {

  private static final JsonConfigurationFormatter JSON_FORMATTER = new JsonConfigurationFormatter();
  private final Configuration configuration;
  private final File file;
  private JsonConfigurationPath root;

  public JsonConfigurationType(Configuration configuration) {
    this.configuration = configuration;
    this.file = new File(configuration.path());
  }

  public void init(ConfigurationAccessor accessor) {
    boolean loadDefaults = false;
    if (!this.file.exists()) {
      this.createFile(this.file, "{", "}");
      loadDefaults = true;
    }
    try {
      this.root =
          JsonConfigurationPath.create(
              (JSONObject)
                  new JSONParser()
                      .parse(FileUtils.readFileToString(file, Charset.defaultCharset())));
    } catch (ParseException | IOException e) {
      e.printStackTrace();
    }
    if (loadDefaults) {
      accessor.loadDefaults();
      this.save(accessor);
    } else {
      Arrays.stream(accessor.getClass().getDeclaredFields())
          .filter(field -> !Modifier.isTransient(field.getModifiers()))
          .forEach(
              field -> {
                field.setAccessible(true);
                try {
                  field.set(accessor, this.root.get(field.getName()));
                } catch (IllegalAccessException e) {
                  e.printStackTrace();
                }
              });
    }
  }

  public JsonConfigurationPath root() {
    return root;
  }

  public void save(ConfigurationAccessor accessor) {
    Arrays.stream(accessor.getClass().getDeclaredFields())
        .filter(field -> !Modifier.isTransient(field.getModifiers()))
        .forEach(
            field -> {
              try {
                field.setAccessible(true);
                this.root.set(field.getName(), field.get(accessor));
                FileUtils.writeLines(this.file, Arrays.asList(this.format()));
              } catch (IllegalAccessException | IOException e) {
                e.printStackTrace();
              }
            });
  }

  public void delete() {
    this.file.delete();
  }

  public String[] format() {
    return this.getFormatter().format(this);
  }

  public ConfigurationFormatter getFormatter() {
    return JSON_FORMATTER;
  }
}
