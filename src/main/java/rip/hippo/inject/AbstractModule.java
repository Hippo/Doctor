package rip.hippo.inject;

import rip.hippo.inject.binding.Binder;
import rip.hippo.inject.binding.ImplementationBinder;

/**
 * @author Hippo
 */
public abstract class AbstractModule implements DoctorModule {

  private Binder binder;

  @Override
  public final void configure(Binder binder) {
    if (this.binder != null) {
      throw new IllegalStateException("Entered configuration stage in module twice.");
    }

    this.binder = binder;
    configure();
  }

  protected abstract void configure();

  public <T> ImplementationBinder<T> bind(Class<T> type) {
    return binder.bind(type);
  }
}
