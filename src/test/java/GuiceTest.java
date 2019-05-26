import com.google.inject.Guice;
import com.google.inject.Inject;

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
