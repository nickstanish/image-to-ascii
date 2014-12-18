package net.vizbits.imagetoascii;


public class Main {
  private static String filename = "media/chi.jpg";
  private static Integer blockSize = 8;

  public static void main(String[] args) {
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        new PixelHelper(filename, blockSize);
      }
    });


  }

}
