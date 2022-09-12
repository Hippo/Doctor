package rip.hippo.inject.test;

import org.junit.jupiter.api.Test;
import rip.hippo.inject.AbstractModule;
import rip.hippo.inject.Doctor;
import rip.hippo.inject.DoctorModule;
import rip.hippo.inject.Injector;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Hippo
 */
public final class QualifierTest {

  private static final DoctorModule QUALIFIER_MODULE = new AbstractModule() {
    @Override
    protected void configure() {
      bind(List.class).accept(binder -> {
        binder.to(ArrayList.class).with(Named.class);
        binder.to(LinkedList.class);
      });
    }
  };

  @Test
  public void test() {
    Injector injector = Doctor.createInjector(QUALIFIER_MODULE);
    A a = injector.getInstance(A.class);
    B b = injector.getInstance(B.class);
    a.check();
    b.check();
  }

  private static final class A {
    private final List<String> list;

    @Inject
    public A(@Named List<String> list) {
      this.list = list;
    }

    public void check() {
      System.out.println(list.getClass().getSimpleName());
    }
  }

  private static final class B {
    private final List<String> list;

    @Inject
    public B(List<String> list) {
      this.list = list;
    }

    public void check() {
      System.out.println(list.getClass().getSimpleName());
    }
  }
}
