package me.panavtec.threaddecoratedview.views;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import me.panavtec.threaddecoratedview.views.qualifiers.ThreadDecoratedView;

public class ViewInjector {
  private static final String CLASS_PREFIX = "Decorated";

  public static <T> T inject(T viewImplementation, ThreadSpec mainThreadSpec) {
    return injectView(viewImplementation, mainThreadSpec);
  }

  private static <T> T injectView(T viewImplementation, ThreadSpec mainThreadSpec) {
    try {
      Class<?> viewInterface = findThreadDecoratedView(viewImplementation.getClass());
      Class<?> decoratedView = getDecoratedView(viewInterface);
      Constructor<?> constructor = decoratedView.getConstructor(viewInterface, ThreadSpec.class);
      return (T) constructor.newInstance(viewImplementation, mainThreadSpec);
    } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

  private static Class<?> findThreadDecoratedView(Class<?> implementedViewClass) {
    Class<?>[] viewInterfaces = implementedViewClass.getInterfaces();
    for (Class<?> viewInterface : viewInterfaces) {
      if (viewInterface.isAnnotationPresent(ThreadDecoratedView.class)) {
        return viewInterface;
      }
    }
    throw new RuntimeException(
        "Cannot find any View annotated with @" + ThreadDecoratedView.class.getName());
  }

  public static <T> T nullObjectPatternView(Object view) {
    return (T) Proxy.newProxyInstance(view.getClass().getClassLoader(),
        new Class[] { findThreadDecoratedView(view.getClass()) }, new InvocationHandler() {
          @Override public Object invoke(Object proxy, Method method, Object[] args)
              throws Throwable {
            return null;
          }
        });
  }

  private static Class<?> getDecoratedView(Class<?> viewInterface) {
    String packageName = viewInterface.getPackage().getName();
    String className = packageName + "." + CLASS_PREFIX + viewInterface.getSimpleName();
    try {
      Class<?> decoratedViewClass = Class.forName(className);
      if (decoratedViewClass == Void.TYPE) {
        throw new RuntimeException("Can't find decoratedView class");
      }
      return decoratedViewClass;
    } catch (ClassNotFoundException e) {
      System.err.println(
          String.format("Class %s not found. Please annotate with @%s the class %s", className,
              ThreadDecoratedView.class.getSimpleName(), viewInterface.getCanonicalName()));
    }
    return Void.TYPE;
  }
}
