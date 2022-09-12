package rip.hippo.inject;

import java.lang.annotation.Annotation;

/**
 * @author Hippo
 */
public interface Injector {
  <T> T getInstance(Class<T> type, Class<? extends Annotation> qualifier);
  <T> T getInstance(Class<T> type);
  void requestStaticInjections(Class<?> type);
}
