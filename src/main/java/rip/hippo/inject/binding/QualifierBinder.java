package rip.hippo.inject.binding;

import java.lang.annotation.Annotation;

/**
 * @author Hippo
 */
@FunctionalInterface
public interface QualifierBinder {
  void with(Class<? extends Annotation> qualifier);
}
