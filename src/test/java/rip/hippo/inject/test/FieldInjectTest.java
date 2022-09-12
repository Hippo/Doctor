package rip.hippo.inject.test;

import org.junit.jupiter.api.Test;
import rip.hippo.inject.AbstractModule;
import rip.hippo.inject.Doctor;
import rip.hippo.inject.DoctorModule;
import rip.hippo.inject.Injector;

import javax.inject.Inject;

/**
 * @author Hippo
 */
public final class FieldInjectTest {


  private static final DoctorModule FIELD_MODULE = new AbstractModule() {
    @Override
    protected void configure() {
      bind(String.class).toInstance("Hello, World!");
    }
  };

  private static final class Example {
    @Inject
    private String message;

    public void test() {
      System.out.println(message);
    }
  }

  @Test
  public void test() {
    Injector injector = Doctor.createInjector(FIELD_MODULE);
    Example example = injector.getInstance(Example.class);
    example.test();
  }

}
