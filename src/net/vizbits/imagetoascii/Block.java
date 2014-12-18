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

    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        int clr = image.getRGB(i, j);
        int red = (clr & 0x00ff0000) >> 16;
        int green = (clr & 0x0000ff00) >> 8;
        int blue = clr & 0x000000ff;

        reds += red;
        greens += green;
        blues += blue;
      }
    }
    int pixelCount = height * width;
    int red = reds / pixelCount;
    int green = greens / pixelCount;
    int blue = blues / pixelCount;
    averageColor = new Color(red, green, blue);
    grayIndex = (int) ((red + blue + green) / 3.0);
  }

  public Color getAverageColor() {
    return averageColor;
  }

  public int getGrayIndex() {
    return grayIndex;
  }

}
