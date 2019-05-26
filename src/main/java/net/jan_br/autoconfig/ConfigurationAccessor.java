package net.jan_br.autoconfig;

public class ConfigurationAccessor {

    private ConfigurationBase configurationBase;

    public void save() {
        System.out.println(this.configurationBase);
        this.configurationBase.save(this);
    }

    public void delete() {
        this.configurationBase.delete();
    }

    protected void setConfigurationBase(ConfigurationBase configurationBase) {
        this.configurationBase = configurationBase;
    }
}
