import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import net.jan_br.autoconfig.Configuration;
import net.jan_br.autoconfig.ConfigurationAccessor;
import net.jan_br.autoconfig.json.JsonConfigurationType;

@Singleton
@Configuration(path = "./test.json", type = JsonConfigurationType.class)
public final class ExampleConfiguration extends ConfigurationAccessor {

    //All Types of Dependency Injection can be used here.
    //Use modifier transient to mark fields which you don't want to be serialized.
    private final transient Injector injector;

    private String name;
    private Long age;

    @Inject
    private ExampleConfiguration(Injector injector) {
        this.injector = injector;
    }

    /**
     * This method is required because a lazy initialized field would badly break the simple
     * configuration design. The configuration values are initialized after the super constructor, so
     * NEVER use a direct initialization in the class, always use loadDefaults otherwise the value
     * will not be loaded into the field.
     */
    public void loadDefaults() {
        this.name = "Jan Brachthäuser";
        this.age = 18L;
    }

    public String getName() {
        return this.name;
    }

    public long getAge() {
        return this.age;
    }

    public ExampleConfiguration setAge(long age) {
        if (age < 0) throw new IllegalArgumentException("Age must not be < 0.");
        this.age = age;
        return this;
    }

    public ExampleConfiguration setName(String name) {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Name must not be empty.");
        this.name = name;
        return this;
    }
}
