package me.panavtec.threaddecoratedview.writer;

import java.io.IOException;
import java.util.Collection;
import javax.annotation.processing.Filer;
import me.panavtec.threaddecoratedview.model.EnclosingView;

public class ViewWriter {

  private final ViewWriterStrategy[] strategies;

  public ViewWriter(ViewWriterStrategy... strategies) {
    this.strategies = strategies;
  }

  public void write(Collection<EnclosingView> enclosingViews, Filer filer) {
    for (EnclosingView view : enclosingViews) {
      try {
        for (ViewWriterStrategy strategy : strategies) strategy.writeView(filer, view);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
