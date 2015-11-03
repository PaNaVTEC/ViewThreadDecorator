package me.panavtec.threaddecoratedview.writer;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import me.panavtec.threaddecoratedview.ViewAnnotationProcessor;
import me.panavtec.threaddecoratedview.model.EnclosingView;
import me.panavtec.threaddecoratedview.model.ViewMethod;
import me.panavtec.threaddecoratedview.views.qualifiers.DoNotStrip;

public class EmptyViewStrategy implements ViewWriterStrategy {

  private static final String CLASS_PREFIX = "Null";

  @Override public void writeView(Filer filer, EnclosingView view) throws IOException {
    List<MethodSpec> viewMethods = new ArrayList<>();
    List<ViewMethod> methods = view.getMethods();
    for (ViewMethod method : methods) {
      viewMethods.add(processMethod(method));
    }
    TypeSpec coordinateInjectorClass = createInjectClass(view, viewMethods);
    JavaFile.builder(view.getPackageName(), coordinateInjectorClass)
        .addFileComment("Do not modify this file!")
        .build()
        .writeTo(filer);
  }

  private MethodSpec processMethod(ViewMethod method) {
    MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(method.getMethodName())
        .addModifiers(Modifier.PUBLIC)
        .returns(ClassName.get(method.getReturnType()));

    List<TypeMirror> parameters = method.getParameters();
    for (int i = 0; i < parameters.size(); i++) {
      String paramName = "param" + i;
      methodBuilder.addParameter(ClassName.get(parameters.get(i)), paramName, Modifier.FINAL);
    }

    methodBuilder.
        addAnnotation(Override.class);

    return methodBuilder.build();
  }

  private TypeSpec createInjectClass(EnclosingView view, List<MethodSpec> viewMethods) {
    ClassName viewType = ClassName.get(view.getPackageName(), view.getClassName());
    TypeSpec.Builder classBuilder = TypeSpec.classBuilder(CLASS_PREFIX + view.getClassName())
        .addModifiers(Modifier.PUBLIC)
        .addSuperinterface(viewType)
        .addAnnotation(AnnotationSpec.builder(DoNotStrip.class).build())
        .addAnnotation(AnnotationSpec.builder(Generated.class)
            .addMember("value", "$S", ViewAnnotationProcessor.class.getCanonicalName())
            .build());
    addDecoratedViewMethods(viewMethods, classBuilder);

    return classBuilder.build();
  }

  private void addDecoratedViewMethods(List<MethodSpec> viewMethods,
      TypeSpec.Builder classBuilder) {
    for (MethodSpec methodSpec : viewMethods) {
      classBuilder.addMethod(methodSpec);
    }
  }
}
