package rip.hippo.inject.test;

import org.junit.jupiter.api.Test;
import rip.hippo.inject.AbstractModule;
import rip.hippo.inject.Doctor;
import rip.hippo.inject.DoctorModule;
import rip.hippo.inject.Injector;
import rip.hippo.inject.annotation.Provides;

import javax.inject.Inject;
import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Hippo
 */
public final class ModuleProvidesTest {

  @Qualifier
  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.METHOD)
  @interface TestQualifier {}


  private static final DoctorModule PROVIDE_MODULE = new AbstractModule() {
    @Override
    protected void configure() {
      bind(List.class).toInstance(Arrays.asList("Hello", "World"));
    }

    @TestQualifier
    @Provides
    public String provideString() {
      return "Hello World!";
    }

    @Provides
    @Inject
    public Set<String> getSet(List<String> list) {
      return new HashSet<>(list);
    }
  };

  @Test
  public void test() {
    Injector injector = Doctor.createInjector(PROVIDE_MODULE);
    String message = injector.getInstance(String.class, TestQualifier.class);
    Set<?> set = injector.getInstance(Set.class);
    System.out.println(message);
    System.out.println(set);
  }

}
