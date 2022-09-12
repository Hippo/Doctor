package rip.hippo.inject.test;

import org.junit.jupiter.api.Test;
import rip.hippo.inject.AbstractModule;
import rip.hippo.inject.Doctor;
import rip.hippo.inject.DoctorModule;
import rip.hippo.inject.Injector;

import javax.inject.Inject;
import java.util.Random;

/**
 * @author Hippo
 */
public final class BasicBindingTest {

  private interface Output {
    void out(String info);
  }

  private static final class StandardOutput implements Output {

    @Override
    public void out(String info) {
      System.out.println(info);
    }
  }

  private static final class ReverseOutput implements Output {
    @Override
    public void out(String info) {
      System.out.println(new StringBuilder(info).reverse());
    }
  }

  private interface Printer {
    void print(String info);
  }

  private static final class BasicPrinter implements Printer {

    private final Output output;

    @Inject
    public BasicPrinter(Output output) {
      this.output = output;
    }

    @Override
    public void print(String info) {
      output.out(info);
    }
  }

  private static final DoctorModule BINDING_TEST_MODULE = new AbstractModule() {
    @Override
    protected void configure() {
      bind(Printer.class).to(BasicPrinter.class);

      if (new Random().nextBoolean()) {
        bind(Output.class).to(StandardOutput.class);
      } else {
        bind(Output.class).to(ReverseOutput.class);
      }

      bind(Boolean.class).toInstance(true);
    }
  };

  @Test
  public void basicBindTest() {
    Injector injector = Doctor.createInjector(BINDING_TEST_MODULE);
    Printer printer = injector.getInstance(Printer.class);
    Boolean bool = injector.getInstance(Boolean.class);
    printer.print("Hello World! " + bool);
  }
}
