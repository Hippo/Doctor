package rip.hippo.inject.binding.impl;

import rip.hippo.inject.Injector;
import rip.hippo.inject.binding.Binder;
import rip.hippo.inject.binding.Binding;
import rip.hippo.inject.binding.ImplementationBinder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hippo
 */
public final class StandardBinder implements Binder {
  final Injector injector;
  final Map<Class<?>, List<Binding<?>>> bindingMap;

  public StandardBinder(Injector injector) {
    this.injector = injector;
    this.bindingMap = new HashMap<>();
  }

  @Override
  public <T> ImplementationBinder<T> bind(Class<T> type) {
    return new StandardImplementationBinder<>(this, type);
  }

  @Override
  public Map<Class<?>, List<Binding<?>>> getBindings() {
    return bindingMap;
  }
}
