package me.panavtec.threaddecoratedview.sample;

import me.panavtec.threaddecoratedview.views.qualifiers.NoDecorate;
import me.panavtec.threaddecoratedview.views.qualifiers.ThreadDecoratedView;

@ThreadDecoratedView public interface MainView {
  @NoDecorate void initUi();
  void sampleUiMethod();
}
