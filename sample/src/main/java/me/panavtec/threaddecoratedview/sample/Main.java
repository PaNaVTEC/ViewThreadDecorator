package me.panavtec.threaddecoratedview.sample;

public class Main {
  public static void main(String[] args) {
    MainPresenter mainPresenter = new MainPresenter();
    mainPresenter.attachView(new MainViewChild());
    mainPresenter.doSomeViewAction();
    mainPresenter.detachView();
    mainPresenter.doSomeViewAction();
  }

}