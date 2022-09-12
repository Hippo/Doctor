package rip.hippo.inject.test;

import org.junit.jupiter.api.Test;
import rip.hippo.inject.Doctor;
import rip.hippo.inject.Injector;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author Hippo
 */
public final class ProviderTest {


  @Test
  public void test() {
    Injector injector = Doctor.createInjector();
    A instance = injector.getInstance(A.class);
    System.out.println(instance.b.a.get());
  }

  private static final class A {

    private final B b;

    @Inject
    public A(B b) {
      this.b = b;
    }

    @Override
    public String toString() {
      return "it works";
    }
  }

  private static final class B {

    private final Provider<A> a;

    @Inject
    public B(Provider<A> a) {
      this.a = a;
    }
  }
}
