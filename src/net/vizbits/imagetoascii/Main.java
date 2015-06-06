package net.vizbits.imagetoascii;


public class Main {
  private static String filename = "media/webcam-toy-photo1.jpg";
  private static Integer blockSize = 4;

  public static void main(String[] args) {
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        new PixelHelper(filename, blockSize);
      }
    });


  }

}
