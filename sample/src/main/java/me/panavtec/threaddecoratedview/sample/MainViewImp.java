package me.panavtec.threaddecoratedview.sample;

public class MainViewImp implements MainView {

  @Override public void initUi() {
    System.out.println("initUi");
  }

  @Override public void sampleUiMethod() {
    System.out.println("sampleUiMethod");
  }
}