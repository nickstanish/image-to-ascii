package net.vizbits.imagetoascii;

import net.vizbits.imagetoascii.service.AsciiService;
import net.vizbits.imagetoascii.ui.ImageToAsciiWindow;

import com.alee.laf.WebLookAndFeel;



public class Main {

  private static String filename = "media/lake.jpg";
  private static Integer blockSize = 4;


  public static void main(String[] args) {
    WebLookAndFeel.install();
    AsciiService asciiService = new AsciiService();

    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        new ImageToAsciiWindow(asciiService);
        // new PixelHelper(filename, blockSize);
      }
    });


  }

}
