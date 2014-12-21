package net.vizbits.imagetoascii.service;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.function.Consumer;

import javax.imageio.ImageIO;

import net.vizbits.imagetoascii.Block;
import net.vizbits.imagetoascii.PixelHelper;
import net.vizbits.imagetoascii.service.helpers.AsciiOptionsSet;

public class AsciiService {

  private Consumer<BufferedImage> callback;
  private static String FONT_NAME = "Monospaced";
  private static Integer FONT_SIZE = 16;
  private static Integer PADDING = 20;


  public AsciiService() {}

  public void setCallback(Consumer<BufferedImage> callback) {
    this.callback = callback;
  }

  public void process(final BufferedImage preimage, final AsciiOptionsSet optionsSet) {
    Integer _blockSize = optionsSet.getOption(AsciiOptionsSet.KEY_BLOCK_SIZE);
    if (_blockSize == null || _blockSize < 0) {
      _blockSize = createDefaultOptionSet().getOption(AsciiOptionsSet.KEY_BLOCK_SIZE);
    }
    final Integer blockSize = _blockSize;

    int height = preimage.getHeight();
    int width = preimage.getWidth();

    ArrayList<String> texts = new ArrayList<String>();
    int rows = height / blockSize;
    int columns = width / blockSize;

    Thread processing = new Thread() {
      @Override
      public void run() {
        BufferedImage image = null;
        try {


          for (int i = 0; i < rows; i++) {
            StringBuilder builder = new StringBuilder();
            for (int j = 0; j < columns; j++) {
              Block block =
                  new Block(
                      preimage.getSubimage(j * blockSize, i * blockSize, blockSize, blockSize));
              char selection;
              // expected value is 0-255, should give range of 0-7, with higher being brighter
              selection = PixelHelper.indexToChar8(block.getGrayIndex());
              // selection = indexToCharSimple(block.getGrayIndex());
              builder.append(selection);
              // System.out.print(selection);
            }

            texts.add(builder.toString());
          }

          Font font = new Font(FONT_NAME, Font.PLAIN, FONT_SIZE);
          FontMetrics metrics = getFontMetrics(font);
          int verticalHeight = metrics.getHeight();
          int height = rows * verticalHeight + 2 * PADDING;
          int width = columns * metrics.stringWidth(" ") + 2 * PADDING;
          image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
          Graphics2D g2D = image.createGraphics();
          g2D.setColor(Color.white);
          g2D.setFont(font);
          g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
          g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
              RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

          g2D.fill(new Rectangle2D.Double(0, 0, width, height));

          g2D.setColor(Color.black);
          g2D.setStroke(new BasicStroke(0.01f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
          for (int i = 0; i < texts.size(); i++) {
            g2D.drawString(texts.get(i), PADDING, (i * verticalHeight) + PADDING);
          }
          g2D.dispose();
        } catch (Exception e) {
          e.printStackTrace();
        }
        callback.accept(image);
      }
    };
    processing.start();



  }

  private FontMetrics getFontMetrics(Font font) {
    BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    return image.createGraphics().getFontMetrics(font);
  }


  public static AsciiOptionsSet createDefaultOptionSet() {
    AsciiOptionsSet set = new AsciiOptionsSet();
    set.addOption(AsciiOptionsSet.KEY_BLOCK_SIZE, AsciiOptionsSet.VALUE_BLOCK_MEDIUM);
    return set;
  }

}
