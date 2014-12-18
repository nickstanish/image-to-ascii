package net.vizbits.imagetoascii;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class PixelHelper {
  public File imageFile;
  public BufferedImage image;

  public PixelHelper(String fileName, int blockSize) {
    imageFile = new File(fileName);
    try {
      image = ImageIO.read(imageFile);
    } catch (IOException e) {
      System.err.println("Invalid Image File!");
      e.printStackTrace();
      System.exit(-1);
    }
    int height = image.getHeight();
    int width = image.getWidth();

    ArrayList<String> texts = new ArrayList<String>();
    int rows = height / blockSize;
    int columns = width / blockSize;
    for (int i = 0; i < rows; i++) {
      StringBuilder builder = new StringBuilder();
      for (int j = 0; j < columns; j++) {
        Block block =
            new Block(image.getSubimage(j * blockSize, i * blockSize, blockSize, blockSize));
        char selection;
        // expected value is 0-255, should give range of 0-7, with higher being brighter
        selection = indexToChar8(block.getGrayIndex());
        // selection = indexToCharSimple(block.getGrayIndex());
        builder.append(selection);
        // System.out.print(selection);
      }

      System.out.println(builder.toString());
      texts.add(builder.toString());
    }

    new TextPainter(texts, rows, columns);

  }

  public static boolean isAsciiPrintable(char ch) {
    return ch >= 32 && ch < 127;
  }

  public static char indexToCharSimple(int index) {
    return (char) (index);
  }

  public static char indexToChar8(int index) {
    char selection;
    switch (index / 32) {
      case 0:
        selection = '#';
        break;
      case 1:
        selection = '@';
        break;
      case 2:
        selection = '$';
        break;
      case 3:
        selection = '%';
        break;
      case 4:
        selection = '/';
        break;
      case 5:
        selection = '*';
        break;
      case 6:
        selection = '.';
        break;
      case 7:
      default:
        selection = ' ';
        break;
    }
    return selection;
  }

}
