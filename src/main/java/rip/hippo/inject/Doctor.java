package rip.hippo.inject;

import rip.hippo.inject.impl.StandardInjector;
import rip.hippo.inject.instance.InstanceFactory;

import java.util.Arrays;

/**
 * @author Hippo
 */
public final class Doctor {

  private Doctor() {
    throw new IllegalStateException("Cannot instantiate Doctor.");
  }

  public static Injector createInjector(DoctorModule... modules) {
    return createInjector(Arrays.asList(modules));
  }

  public static Injector createInjector(Iterable<? extends DoctorModule> modules) {
    return new StandardInjector(modules);
  }

  public static Injector createInjector(InstanceFactory instanceFactory, DoctorModule... modules) {
    return createInjector(instanceFactory, Arrays.asList(modules));
  }

  public static Injector createInjector(InstanceFactory instanceFactory, Iterable<? extends DoctorModule> modules) {
    return new StandardInjector(instanceFactory, modules);
  }
}
