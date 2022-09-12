package rip.hippo.inject.test;

import org.junit.jupiter.api.Test;
import rip.hippo.inject.Doctor;
import rip.hippo.inject.Injector;

import javax.inject.Inject;

/**
 * @author Hippo
 */
public final class TransitiveDependencyTest {

  @Test
  public void test() {
    Injector injector = Doctor.createInjector();
    Foo foo = injector.getInstance(Foo.class);
    System.out.println(foo.bar.baz);
  }

  public final static class Foo {
    private final Bar bar;

    @Inject
    public Foo(Bar bar) {
      this.bar = bar;
    }
  }

  public final static class Bar {
    private final Baz baz;

    @Inject
    public Bar(Baz baz) {
      this.baz = baz;
    }
  }

  public final static class Baz {}
}
