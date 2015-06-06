package net.vizbits.imagetoascii;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Block {
  private BufferedImage image;
  private Color averageColor;
  private int grayIndex;

  public Block(BufferedImage image) {
    this.image = image;
    init();
  }

  private void init() {
    int height = image.getHeight();
    int width = image.getWidth();

    int reds = 0;
    int greens = 0;
    int blues = 0;
    int alphas = 0;

    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        int clr = image.getRGB(i, j);
        int red = (clr & 0x00ff0000) >> 16;
        int green = (clr & 0x0000ff00) >> 8;
        int blue = clr & 0x000000ff;
        int alpha = (clr >> 24) & 0xff;

        reds += red;
        greens += green;
        blues += blue;
        alphas += alpha;
      }
    }
    int pixelCount = height * width;
    int red = reds / pixelCount;
    int green = greens / pixelCount;
    int blue = blues / pixelCount;
    int alpha = alphas / pixelCount;
    averageColor = new Color(red, green, blue, alpha);
    grayIndex = (int) ((red + blue + green) / 3.0);
  }

  public int getAlpha() {
    return averageColor.getAlpha();
  }

  public Color getAverageColor() {
    return averageColor;
  }

  public int getGrayIndex() {
    return grayIndex;
  }

}
