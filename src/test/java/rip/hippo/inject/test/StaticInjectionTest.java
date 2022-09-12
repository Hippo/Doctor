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
public final class StaticInjectionTest {

  private static final DoctorModule STATIC_INJECTION_MODULE = new AbstractModule() {
    @Override
    public void configure() {
      bind(Boolean.class).toInstance(true);
    }
  };

  @Test
  public void test() {
    Injector injector = Doctor.createInjector(STATIC_INJECTION_MODULE);
    injector.requestStaticInjections(Example.class);
    System.out.println(Example.bool);
  }


  private static final class Example {

    @Inject private static Boolean bool;
  }
}
