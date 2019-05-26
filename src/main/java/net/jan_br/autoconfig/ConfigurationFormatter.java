package net.jan_br.autoconfig;

public interface ConfigurationFormatter<T extends ConfigurationBase> {

    String[] format(T config);

}
