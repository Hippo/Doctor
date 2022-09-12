package rip.hippo.inject;

import rip.hippo.inject.binding.Binder;


/**
 * @author Hippo
 */
@FunctionalInterface
public interface DoctorModule {
  void configure(Binder binder);
}
