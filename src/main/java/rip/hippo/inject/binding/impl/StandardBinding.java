package rip.hippo.inject.binding.impl;

import rip.hippo.inject.Injector;
import rip.hippo.inject.binding.Binding;

import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.util.Optional;

/**
 * @author Hippo
 */
public final class StandardBinding<T> implements Binding<T> {

  private final Injector injector;
  private final Class<T> type;
  private final T instance;
  private final Class<?> implementation;
  private Class<? extends Annotation> qualifier;
  private final boolean singleton;

  public StandardBinding(Injector injector, Class<T> type, T instance, Class<?> implementation) {
    this.injector = injector;
    this.type = type;
    this.instance = instance;
    this.implementation = implementation;
    this.singleton = type.getDeclaredAnnotation(Singleton.class) != null;
  }

  @Override
  public T getInstance() {
    return injector.getInstance(type, qualifier);
  }

  @Override
  public Class<T> getType() {
    return type;
  }

  @Override
  public Class<?> getImplementation() {
    return implementation;
  }

  @Override
  public Optional<Class<? extends Annotation>> getQualifier() {
    return Optional.ofNullable(qualifier);
  }

  @Override
  public void setQualifier(Class<? extends Annotation> qualifier) {
    this.qualifier = qualifier;
  }

  @Override
  public Optional<T> getProvidedInstance() {
    return Optional.ofNullable(instance);
  }

  @Override
  public boolean isSingleton() {
    return singleton;
  }
}
