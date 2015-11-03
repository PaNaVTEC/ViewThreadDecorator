package me.panavtec.threaddecoratedview.views;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import me.panavtec.threaddecoratedview.views.qualifiers.ThreadDecoratedView;

public class ViewInjector {

  private static final String DECORATED_CLASS_PREFIX = "Decorated";
  private static final String NULL_CLASS_PREFIX = "Null";

  public static <T> T inject(T viewImplementation, ThreadSpec mainThreadSpec) {
    try {
      Class<?> viewInterface = findThreadDecoratedView(viewImplementation.getClass());
      Class<?> decoratedView = findViewClassWithPrefix(DECORATED_CLASS_PREFIX, viewInterface);
      Constructor<?> constructor = decoratedView.getConstructor(viewInterface, ThreadSpec.class);
      return (T) constructor.newInstance(viewImplementation, mainThreadSpec);
    } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static <T> T nullObjectPatternView(T viewImplementation) {
    try {
      Class<?> viewInterface = findThreadDecoratedView(viewImplementation.getClass());
      Class<?> decoratedView = findViewClassWithPrefix(NULL_CLASS_PREFIX, viewInterface);
      Constructor<?> constructor = decoratedView.getConstructor();
      return (T) constructor.newInstance();
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
    if (implementedViewClass.getSuperclass() != null) {
      return findThreadDecoratedView(implementedViewClass.getSuperclass());
    }
    throw new RuntimeException(
        "Cannot find any View annotated with @" + ThreadDecoratedView.class.getName());
  }

  private static Class<?> findViewClassWithPrefix(String classPrefix, Class<?> viewInterface) {
    String packageName = viewInterface.getPackage().getName();
    String className = packageName + "." + classPrefix + viewInterface.getSimpleName();
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
