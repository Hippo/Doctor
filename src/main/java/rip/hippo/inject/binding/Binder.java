package rip.hippo.inject.binding;

import java.util.List;
import java.util.Map;

/**
 * @author Hippo
 */
public interface Binder {
  <T> ImplementationBinder<T> bind(Class<T> type);
  Map<Class<?>, List<Binding<?>>> getBindings();
}
