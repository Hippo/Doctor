package rip.hippo.inject.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import rip.hippo.inject.Doctor;
import rip.hippo.inject.Injector;

import javax.inject.Inject;

/**
 * @author Hippo
 */
public final class CircularDependencyTest {

  @Test()
  public void test() {
    Injector injector = Doctor.createInjector();
    Assertions.assertThrows(RuntimeException.class, () -> injector.getInstance(A.class));

    Assertions.assertThrows(RuntimeException.class, () -> injector.getInstance(C.class));
  }


  private static final class A {
    @Inject B b;
  }

  private static final class B {
    @Inject A a;
  }

  private static final class C {
    @Inject void d(D d){}
  }

  private static final class D {
    @Inject void c(C c){}
  }
}
