package net.jan_br.autoconfig;

public interface ConfigurationFormatter<T extends ConfigurationType> {

    String[] format(T config);

}
