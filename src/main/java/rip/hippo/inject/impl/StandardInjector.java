package rip.hippo.inject.impl;

import rip.hippo.inject.DoctorModule;
import rip.hippo.inject.Injector;
import rip.hippo.inject.binding.Binder;
import rip.hippo.inject.binding.Binding;
import rip.hippo.inject.binding.impl.StandardBinder;
import rip.hippo.inject.instance.InstanceFactory;
import rip.hippo.inject.instance.impl.StandardInstanceFactory;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;


/**
 * @author Hippo
 */
public final class StandardInjector implements Injector {

  private final InstanceFactory instanceFactory;
  private final Binder binder;

  public StandardInjector(InstanceFactory instanceFactory, Iterable<? extends DoctorModule> modules) {
    this.instanceFactory = instanceFactory;
    this.binder = new StandardBinder(this);

    for (DoctorModule module : modules) {
      module.configure(binder);
    }

    for (DoctorModule module : modules) {
      instanceFactory.findProvides(binder, module);
    }

  }

  public StandardInjector(Iterable<? extends DoctorModule> modules) {
    this.instanceFactory = new StandardInstanceFactory(this);

    this.binder = new StandardBinder(this);

    for (DoctorModule module : modules) {
      module.configure(binder);
    }

    for (DoctorModule module : modules) {
      instanceFactory.findProvides(binder, module);
    }

  }

  @Override
  public <T> T getInstance(Class<T> type, Class<? extends Annotation> qualifier) {
    List<Binding<?>> bindingList = binder.getBindings().get(type);
    if (bindingList == null) {
      return instanceFactory.createInstance(type);
    }

    Binding<?> binding = null;
    for (Binding<?> bind : bindingList) {
      Optional<Class<? extends Annotation>> bindQualifier = bind.getQualifier();
      if (bindQualifier.isPresent()) {
        Class<? extends Annotation> qualifierClass = bindQualifier.get();
        if (qualifierClass.equals(qualifier)) {
          binding = bind;
        }
      } else if (qualifier == null) {
        binding = bind;
        break;
      }
    }

    if (binding == null) {
      throw new RuntimeException("Could not find bind for " + type + (qualifier == null ? "" : " with qualifier: " + qualifier));
    }

    Optional<?> providedInstance = binding.getProvidedInstance();
    if (providedInstance.isPresent()) {
      return type.cast(providedInstance.get());
    }

    Class<?> implementation = binding.getImplementation();
    return type.cast(instanceFactory.createInstance(implementation));
  }

  @Override
  public <T> T getInstance(Class<T> type) {
    return getInstance(type, null);
  }

  @Override
  public void requestStaticInjections(Class<?> type) {
    instanceFactory.requestStaticInjections(type);
  }
}
