# Auto-Config

Auto-Config is an easy, lightweight and simple to use way to create object-related configurations in many different formats.
It is meant to be used with the dependency framework [guice by google](https://github.com/google/guice) 
but also supports standalone usage.

## Supported Formats:
**Auto-Config supports at the time the following configuration formats:**

|Format|Implemented with|
|:----:|:--------------:|
|JSON|[Gson](https://github.com/google/gson)|
|Yaml|`In Development`|

## Examples with Guice

*ExampleConfiguration:*
```java
@Singleton //Used to mark this class as a Singleton for Guice.
@Configuration(path = "example.json", type = JsonConfigurationType.class)
public class ExampleConfig extends ConfigurationAccessor {

  private final transient Injector injector; //Any type of dependency injection is possible here.
  private transient String test; //transient fields will be ignored
  private String name;

  @Inject
  private ExampleConfig(Injector injector) {
    this.injector = injector;
  }


  public void loadDefaults() {
    name = "Jan Brachthäuser";
    test = "Test";
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}

```
*Usage:*
```java
public class GuiceTest {

  private final ExampleConfig exampleConfig;

  @Inject
  public GuiceTest(ExampleConfig exampleConfig) {
      System.out.println(exampleConfiguration.getName()); //Will return 'Jan Brachthäuser' by default.

    exampleConfiguration.setName("Melvin");
    exampleConfiguration.save(); //Will save the configuration in the given format to the file specified in @Configuration.

    System.out.println(exampleConfiguration.getName()); //Will return 'Melvin' now.
  }

  public static void main(String[] args) {
    Guice.createInjector().getInstance(GuiceTest.class);
  }
}
```

## Examples without Guice:

*ExampleConfiguration:*
```java

@Configuration(path = "example.json", type = JsonConfigurationType.class)
public class ExampleConfig extends ConfigurationAccessor {

  private transient String test; //transient fields will be ignored
  private String name;


  public void loadDefaults() {
    name = "Jan Brachthäuser";
    test = "Test";
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
```
*Usage:*
```java
public final class NoGuiceTest {

  private ExampleConfig exampleConfiguration;

  private NoGuiceTest() {
    this.exampleConfiguration = new ExampleConfig();
    this.init();
  }

  private void init() {
    System.out.println(exampleConfiguration.getName()); //Will return 'Jan Brachthäuser' by default.

    exampleConfiguration.setName("Melvin");
    exampleConfiguration.save(); //Will save the configuration in the given format to the file specified in @Configuration.

    System.out.println(exampleConfiguration.getName()); //Will return 'Melvin' now.
  }


  public static void main(String[] args) {
    new NoGuiceTest();
  }
}
```
