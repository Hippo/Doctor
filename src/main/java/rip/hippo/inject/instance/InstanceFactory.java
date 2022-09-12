package rip.hippo.inject.instance;


import rip.hippo.inject.DoctorModule;
import rip.hippo.inject.binding.Binder;

/**
 * @author Hippo
 */
public interface InstanceFactory {
  <T> T createInstance(Class<T> type);
  void requestStaticInjections(Class<?> type);
  void findProvides(Binder binder, DoctorModule module);
}
