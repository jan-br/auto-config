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

## Examples
**Usage with Guice:**
In usage with Guice auto-config is able to autocreate the configuration and format it to a given ConfigurationType
via @Configuration. 

*ExampleConfiguration:*
```java
@Singleton
@Configuration(path = "./test.json", type = JsonConfigurationType.class)
public final class ExampleConfiguration extends ConfigurationAccessor{
	private String name = "Jan Brachthäuser";
	private int age = 18;
	
	public String getName(){
		return this.name;
	}
	
	public int getAge(){
		return this.age;
	}

	public ExampleConfiguration setAge(int age){
		if(age < 0) throw new IllegalArgumentException("Age must not be < 0."); 
		this.age = age;
		return this;
	}

	public ExampleConfiguration setName(String name){
		if(name == null || name.isEmpty()) throw new IllegalArgumentException("Name must not be empty.");
		this.name = name;
		return this;
	}
}
```
*Program entry point:*
```java
public final class ExampleProgram{
	
	public static void main(String[] args){
		Guice.createInjector().getInstance(Test.class);
	}
	
}
```
*Configuration Usage:*
```java
public final class Test{
	private final ExampleConfiguration exampleConfiguration;

	@Inject
	private Test(ExampleConfiguration exampleConfiguration){
		//Will be autocreated at this point if file doesn't exist and load all default values in it.
		this.exampleConfiguration = exampleConfiguration;
		this.init();
	}

	public void init(){
		System.out.println(exampleConfiguration.getName()); //Will return 'Jan Brachthäuser' by default.
		exampleConfiguration.setName("Nobody");
		exampleConfiguration.save(); //Will save the configuration in the given format to the file specified in @Configuration.
		
		System.out.println(exampleConfiguration.getName()); //Will return 'Nobody' now.
	}
}
```

**Usage without Guice:**
not supportet yet.
