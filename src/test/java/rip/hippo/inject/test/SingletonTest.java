package rip.hippo.inject.test;

import org.junit.jupiter.api.Test;
import rip.hippo.inject.AbstractModule;
import rip.hippo.inject.Doctor;
import rip.hippo.inject.DoctorModule;
import rip.hippo.inject.Injector;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Hippo
 */
public final class SingletonTest {

  private interface MyDependency {
    int getID();
  }

  @Singleton
  private static final class MyImplementation implements MyDependency {

    private static int count = 0;
    private final int id;

    @Inject
    public MyImplementation() {
      this.id = count++;
    }

    @Override
    public int getID() {
      return id;
    }
  }

  private static final DoctorModule MY_MODULE = new AbstractModule() {
    @Override
    protected void configure() {
      bind(MyDependency.class).to(MyImplementation.class);
    }
  };

  @Test
  public void test() {
    Injector injector = Doctor.createInjector(MY_MODULE);
    MyDependency first = injector.getInstance(MyDependency.class);
    MyDependency second = injector.getInstance(MyDependency.class);
    System.out.println("First: " + first.getID());
    System.out.println("Second: " + second.getID());
  }
}
