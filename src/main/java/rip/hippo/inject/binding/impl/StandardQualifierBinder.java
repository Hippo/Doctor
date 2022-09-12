package rip.hippo.inject.binding.impl;

import rip.hippo.inject.binding.Binding;
import rip.hippo.inject.binding.QualifierBinder;

import java.lang.annotation.Annotation;

/**
 * @author Hippo
 */
public final class StandardQualifierBinder implements QualifierBinder {

  private final Binding<?> binding;

  public StandardQualifierBinder(Binding<?> binding) {
    this.binding = binding;
  }

  @Override
  public void with(Class<? extends Annotation> qualifier) {
    binding.setQualifier(qualifier);
  }
}
