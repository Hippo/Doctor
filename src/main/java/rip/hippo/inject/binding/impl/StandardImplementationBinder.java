package rip.hippo.inject.binding.impl;

import rip.hippo.inject.binding.Binding;
import rip.hippo.inject.binding.ImplementationBinder;
import rip.hippo.inject.binding.QualifierBinder;

import java.util.LinkedList;
import java.util.function.Consumer;

/**
 * @author Hippo
 */
public final class StandardImplementationBinder<T> implements ImplementationBinder<T> {

  final StandardBinder binder;
  final Class<T> type;

  public StandardImplementationBinder(StandardBinder binder, Class<T> type) {
    this.binder = binder;
    this.type = type;
  }

  @Override
  public <U extends T> QualifierBinder to(Class<U> type) {
    Binding<T> binding = new StandardBinding<>(binder.injector, this.type, null, type);
    binder.bindingMap.computeIfAbsent(this.type, ignored -> new LinkedList<>()).add(binding);
    return new StandardQualifierBinder(binding);
  }

  @Override
  public QualifierBinder toInstance(Object instance) {
    Binding<T> binding = new StandardBinding<>(binder.injector, type, type.cast(instance), instance.getClass());
    binder.bindingMap.computeIfAbsent(this.type, ignored -> new LinkedList<>()).add(binding);
    return new StandardQualifierBinder(binding);
  }

  @Override
  public void accept(Consumer<ImplementationBinder<T>> consumer) {
    consumer.accept(this);
  }
}
