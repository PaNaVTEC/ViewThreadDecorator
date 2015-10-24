package me.panavtec.threaddecoratedview;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;

public class ElementTools {

  public String getFieldName(Element e) {
    return e.getSimpleName().toString();
  }

  public String getElementClassName(Element e) {
    String parentClassName = e.toString();
    return parentClassName.substring(parentClassName.lastIndexOf('.') + 1,
        parentClassName.length());
  }

  public String getElementPackagename(Element e) {
    String className = e.toString();
    return className.substring(0, className.lastIndexOf('.'));
  }
}
