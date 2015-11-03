package me.panavtec.threaddecoratedview.writer;

import java.io.IOException;
import javax.annotation.processing.Filer;
import me.panavtec.threaddecoratedview.model.EnclosingView;

public interface ViewWriterStrategy {
  void writeView(Filer filer, EnclosingView view) throws IOException;
}
