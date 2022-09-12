package rip.hippo.inject.instance.impl;

import rip.hippo.inject.DoctorModule;
import rip.hippo.inject.Injector;
import rip.hippo.inject.annotation.Provides;
import rip.hippo.inject.binding.Binder;
import rip.hippo.inject.instance.InstanceFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Qualifier;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

/**
 * @author Hippo
 */
public final class StandardInstanceFactory implements InstanceFactory {

  private final Injector injector;
  private final Map<Class<?>, Object> singletonMap;
  private final Map<Class<?>, Provider<?>> providerMap;

  public StandardInstanceFactory(Injector injector) {
    this.injector = injector;
    this.singletonMap = new HashMap<>();
    this.providerMap = new HashMap<>();
  }

  @Override
  public synchronized <T> T createInstance(Class<T> type) {
    try {
      boolean singleton = type.getDeclaredAnnotation(Singleton.class) != null;
      if (singleton) {
        Object instance = singletonMap.get(type);
        if (instance != null) {
          return type.cast(instance);
        }
      }

      List<Class<?>> classHierarchy = new ArrayList<>();
      Class<?> current = type;
      while (current != null) {
        classHierarchy.add(current);
        current = current.getSuperclass();
      }
      Collections.reverse(classHierarchy);

      Constructor<?> constructor = null;
      for (Constructor<?> declaredConstructor : type.getDeclaredConstructors()) {
        if (declaredConstructor.getDeclaredAnnotation(Inject.class) != null) {
          constructor = declaredConstructor;
          break;
        }
      }
      if (constructor == null) {
        constructor = type.getDeclaredConstructor();
      }
      constructor.setAccessible(true);
      Object[] parameters = new Object[constructor.getParameterCount()];
      setParameters(constructor, parameters);

      T instance = type.cast(constructor.newInstance(parameters));

      for (Class<?> superClass : classHierarchy) {
        circularDependencyCheck(instance.getClass(), superClass);
      }
      for (Class<?> superClass : classHierarchy) {
        injectFields(instance, superClass);
        injectMethods(instance, superClass);
      }

      if (singleton) {
        singletonMap.put(type, instance);
      }
      return instance;
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void requestStaticInjections(Class<?> type) {
    List<Class<?>> classHierarchy = new ArrayList<>();
    Class<?> current = type;
    while (current != null) {
      classHierarchy.add(current);
      current = current.getSuperclass();
    }
    Collections.reverse(classHierarchy);

    for (Class<?> superClass : classHierarchy) {
      circularDependencyCheck(type, superClass);
    }

    for (Class<?> superClass : classHierarchy) {
      try {
        injectFields(null, superClass);
      } catch (ReflectiveOperationException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public void findProvides(Binder binder, DoctorModule module) {
    try {
      for (Method method : module.getClass().getDeclaredMethods()) {
        if (method.getDeclaredAnnotation(Provides.class) != null) {
          method.setAccessible(true);

          Class<?>[] parameterTypes = method.getParameterTypes();
          boolean isStatic = Modifier.isStatic(method.getModifiers());
          Class<?> returnType = method.getReturnType();

          Class<? extends Annotation> qualifier = null;
          for (Annotation annotation : method.getDeclaredAnnotations()) {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            if (annotationType.getDeclaredAnnotation(Qualifier.class) != null) {
              qualifier = annotationType;
              break;
            }
          }
          Object provide;
          if (parameterTypes.length == 0) {
            provide = method.invoke(isStatic ? null : module);
          } else {
            Object[] parameters = new Object[method.getParameterCount()];
            setParameters(method, parameters);
            provide = method.invoke(isStatic ? null : module, parameters);
          }
          Objects.requireNonNull(provide, "Provided object cannot be null");
          binder.bind(returnType).toInstance(provide).with(qualifier);
        }
      }
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException(e);
    }
  }

  private void setParameters(Executable executable, Object[] parameters) {
    for (int i = 0; i < parameters.length; i++) {
      Class<?> parameterType = executable.getParameterTypes()[i];
      Annotation[] parameterAnnotations = executable.getParameterAnnotations()[i];

      if (parameterType.equals(Provider.class)) {
        Class<?> providerType = (Class<?>) ((ParameterizedType) executable.getGenericParameterTypes()[i]).getActualTypeArguments()[0];
        parameters[i] = getProvider(providerType);
      } else {
        Class<? extends Annotation> provider = null;

        if (parameterAnnotations.length != 0) {
          for (Annotation parameterAnnotation : parameterAnnotations) {
            Class<? extends Annotation> annotationType = parameterAnnotation.annotationType();
            if (annotationType.getDeclaredAnnotation(Qualifier.class) != null) {
              provider = annotationType;
              break;
            }
          }
        }

        parameters[i] = injector.getInstance(parameterType, provider);
      }
    }
  }

  private Provider<?> getProvider(Class<?> type) {
    return providerMap.computeIfAbsent(type, ignored -> () -> createInstance(type));
  }


  private void circularDependencyCheck(Class<?> parent, Class<?> child) {
    for (Field declaredField : child.getDeclaredFields()) {
      if (declaredField.getDeclaredAnnotation(Inject.class) != null) {
        Class<?> fieldType = declaredField.getType();
        if (fieldType.equals(parent)) {
          throw new RuntimeException("Circular dependency detected.");
        }
        circularDependencyCheck(parent, fieldType);
      }
    }

    for (Method declaredMethod : child.getDeclaredMethods()) {
      if (declaredMethod.getDeclaredAnnotation(Inject.class) != null) {
        Class<?>[] parameterTypes = declaredMethod.getParameterTypes();
        for (Class<?> parameterType : parameterTypes) {
          if (parameterType.equals(parent)) {
            throw new RuntimeException("Circular dependency detected.");
          }
          circularDependencyCheck(parent, parameterType);
        }
      }
    }
  }

  private void injectFields(Object instance, Class<?> parent) throws ReflectiveOperationException {
    for (Field declaredField : parent.getDeclaredFields()) {

      boolean isStatic = Modifier.isStatic(declaredField.getModifiers());
      if (instance == null && !isStatic) {
        continue;
      }

      if (declaredField.getDeclaredAnnotation(Inject.class) != null) {
        declaredField.setAccessible(true);

        Object value;

        Class<?> type = declaredField.getType();

        if (type.equals(Provider.class)) {
          Class<?> providerType = (Class<?>) ((ParameterizedType) declaredField.getGenericType()).getActualTypeArguments()[0];
          value = getProvider(providerType);
        } else {
          value = injector.getInstance(type);
        }

        declaredField.set(isStatic ? null : instance, value);
      }
    }
  }

  private void injectMethods(Object instance, Class<?> parent) throws ReflectiveOperationException {
    for (Method declaredMethod : parent.getDeclaredMethods()) {
      if (declaredMethod.getDeclaredAnnotation(Inject.class) != null) {
        boolean isStatic = Modifier.isStatic(declaredMethod.getModifiers());
        declaredMethod.setAccessible(true);

        Object[] parameters = new Object[declaredMethod.getParameterCount()];
        setParameters(declaredMethod, parameters);

        declaredMethod.invoke(isStatic ? null : instance, parameters);
      }
    }
  }
}
