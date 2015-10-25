package me.panavtec.threaddecoratedview.sample;

import me.panavtec.threaddecoratedview.views.qualifiers.NotDecorated;
import me.panavtec.threaddecoratedview.views.qualifiers.ThreadDecoratedView;

@ThreadDecoratedView public interface MainView {
  @NotDecorated void initUi();
  void sampleUiMethod();
}
