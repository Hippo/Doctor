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
public final class MethodInjectTest {


  private static final DoctorModule METHOD_MODULE = new AbstractModule() {
    @Override
    protected void configure() {
      bind(String.class).toInstance("Hello, World!");
    }
  };

  private static final class Example {

    @Inject
    public void test(String message) {
      System.out.println(message);
    }
  }

  @Test
  public void test() {
    Injector injector = Doctor.createInjector(METHOD_MODULE);
    injector.getInstance(Example.class);
  }
}
