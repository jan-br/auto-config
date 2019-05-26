
public final class NoGuiceTest {

  private ExampleConfiguration exampleConfiguration;

  private NoGuiceTest() {
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
