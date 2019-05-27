package de.jan_br.autoconfig;

public interface ConfigurationType {

    void save(ConfigurationAccessor configurationAccessor);

    void delete(ConfigurationAccessor accessor);

    void init(ConfigurationAccessor accessor);

}
