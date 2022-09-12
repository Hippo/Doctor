package rip.hippo.inject.binding;

import java.lang.annotation.Annotation;
import java.util.Optional;

/**
 * @author Hippo
 */
public interface Binding<T> {
  T getInstance();
  Class<T> getType();
  Class<?> getImplementation();

  Optional<Class<? extends Annotation>> getQualifier();
  void setQualifier(Class<? extends Annotation> qualifier);

  Optional<T> getProvidedInstance();

  boolean isSingleton();
}
