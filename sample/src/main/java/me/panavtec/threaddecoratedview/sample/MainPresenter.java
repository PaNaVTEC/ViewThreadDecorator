package me.panavtec.threaddecoratedview.sample;

import me.panavtec.threaddecoratedview.views.ThreadSpec;
import me.panavtec.threaddecoratedview.views.ViewInjector;

public class MainPresenter {

  private MainView mainView;

  public MainPresenter() {
  }

  public void attachiew(MainView mainView) {
    this.mainView = ViewInjector.inject(mainView, new ThreadSpec() {
      @Override public void execute(Runnable action) {
        System.out.println("Decorated mainView");
        action.run();
      }
    });
  }

  public void doSomeViewAction() {
    mainView.sampleUiMethod();
  }
}
