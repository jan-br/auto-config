# Auto-Config

Auto-Config is an easy and simple to use way to create object-related configurations in many different formats.
It is meant to be used with the dependency framework [guice by google](https://github.com/google/guice) 
but will support standalone usage in the future.

## Supported Formats:
**Auto-Config supports at the time the following configuration formats:**

|Format|Implemented with|
|:----:|:--------------:|
|JSON|[json-simple](https://github.com/fangyidong/json-simple)|
|Yaml|`In Development`|

## Examples with Guice

*ExampleConfiguration:*
```java
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
```
*Usage:*
```java
public class GuiceTest {

	private final ExampleConfiguration exampleConfiguration;

	@Inject
	private GuiceTest(ExampleConfiguration exampleConfiguration) {
		this.exampleConfiguration = exampleConfiguration;
		this.init();
	}

	private void init() {
		System.out.println(exampleConfiguration.getName()); //Will return 'Jan Brachthäuser' by default.

		exampleConfiguration.setName("Nobody");
		exampleConfiguration.save(); //Will save the configuration in the given format to the file specified in @Configuration.

		System.out.println(exampleConfiguration.getName()); //Will return 'Nobody' now.
	}

	public static void main(String[] args) {
		Guice.createInjector().getInstance(GuiceTest.class);
	}
}
```

## Examples without Guice:

*ExampleConfiguration:*
```java
@Configuration(path = "./test.json", type = JsonConfigurationType.class)
public final class ExampleConfiguration extends ConfigurationAccessor {

    private String name;
    private Long age;

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
```
*Usage:*
```java
public final class NoGuiceTest {

  private ExampleConfiguration exampleConfiguration;

  private NoGuiceTest() {
    this.exampleConfiguration = new ExampleConfiguration();
    this.init();
  }

  private void init() {
    System.out.println(exampleConfiguration.getName()); //Will return 'Jan Brachthäuser' by default.

    exampleConfiguration.setName("Nobody");
    exampleConfiguration.save(); //Will save the configuration in the given format to the file specified in @Configuration.

    System.out.println(exampleConfiguration.getName()); //Will return 'Nobody' now.
  }


  public static void main(String[] args) {
    new NoGuiceTest();
  }
}
```
