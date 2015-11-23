# ViewThreadDecorator

This project attempts to make a common way to decorate a View interface from your MV* implementation to avoid suffer about threading problems. It's just a code generator that decorates an interface in order to post results in the Ui Thread, but you can use it in other ways. Here is a full explanation:

Further information: http://panavtec.me/say-goodbye-to-all-main-thread-problems-in-mvp/

## Importing to your project
Add this dependency to your build.gradle file:

```java
dependencies {
    compile 'me.panavtec:threaddecoratedview-common:1.5.1'
    apt/provided 'me.panavtec:threaddecoratedview-compiler:1.5.1'
}
```
## Basic usage
Annotate your view interface contract with @ThreadDecoratedView:
```java
@ThreadDecoratedView public interface MainView {
  void initUi();
}
```
Implement your custom ThreadSpec, here is a sample:
Annotate your view interface contract with @ThreadDecoratedView:
```java
public class MainThreadSpec implements ThreadSpec {
  Handler handler = new Handler();
  @Override public void execute(Runnable action) {
    handler.post(action);
  }
}
```
Where you need the decorated view, inject it with:
```java
this.mainView = ViewInjector.inject(mainViewImplementation, mainThreadSpec);
```
If you need some methods of your interface to run without the decorator, just annotate those methods with  @NotDecorated:
```java
@ThreadDecoratedView public interface MainView {
  @NotDecorated void initUi();
  void refreshAList();
}
```
When you need to detach the view from the presenter, you can set your view to null if you handle nulls or
you can use an included NullObjectPattern (an implemented view with empty methods) in this way:
```java
this.mainView = ViewInjector.nullObjectPatternView(mainView);
```
