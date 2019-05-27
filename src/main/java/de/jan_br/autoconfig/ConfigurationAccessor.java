package de.jan_br.autoconfig;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class ConfigurationAccessor {

  private transient ConfigurationType configurationType;

  protected ConfigurationAccessor() {
    Configuration configuration = this.getClass().getDeclaredAnnotation(Configuration.class);
    if (configuration == null) throw new NullPointerException("@Configuration is missing.");
    try {
      Constructor<? extends ConfigurationType> declaredConstructor =
          configuration.type().getDeclaredConstructor(Configuration.class);
      declaredConstructor.setAccessible(true);
      this.configurationType = declaredConstructor.newInstance(configuration);
      this.configurationType.init(this);
    } catch (NoSuchMethodException
        | IllegalAccessException
        | InstantiationException
        | InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  public void save() {
    this.configurationType.save(this);
  }

  public void delete() {
    this.configurationType.delete(this);
  }

  public abstract void loadDefaults();
}
