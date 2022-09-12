package rip.hippo.inject.binding;

import java.util.function.Consumer;

/**
 * @author Hippo
 */
public interface ImplementationBinder<T> {
  <U extends T> QualifierBinder to(Class<U> type);
  QualifierBinder toInstance(Object instance);

  void accept(Consumer<ImplementationBinder<T>> consumer);
}
