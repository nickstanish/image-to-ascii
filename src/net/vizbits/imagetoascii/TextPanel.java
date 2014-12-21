package net.vizbits.imagetoascii;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class TextPanel extends JPanel {
  /**
     * 
     */
  private List<String> texts;
  private static final long serialVersionUID = -5389907638440941450L;
  private int rows, columns;
  private static String FONT_NAME = "Monospaced";
  private static Integer FONT_SIZE = 16;
  private static Integer PADDING = 20;
  private BufferedImage image = null;

  public TextPanel(List<String> texts, int rows, int columns) {
    setDoubleBuffered(true);
    this.texts = texts;
    this.rows = rows;
    this.columns = columns;
    Thread processing = new Thread() {
      @Override
      public void run() {
        doInThread();
      }
    };
    processing.start();

  }

  private File file;
  private BufferedImage bi;
  Random rdm = new Random();

  private void doInThread() {
    try {
      Font font = new Font(FONT_NAME, Font.PLAIN, FONT_SIZE);
      file = new File("media/output/img_" + System.currentTimeMillis() + ".png");
      FontMetrics metrics = getFontMetrics(font);
      int verticalHeight = metrics.getHeight();
      int height = rows * verticalHeight + 2 * PADDING;
      int width = columns * metrics.stringWidth(" ") + 2 * PADDING;
      setPreferredSize(new Dimension(width, height));
      bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2D = bi.createGraphics();
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

      image = bi;
      repaint();
      file.mkdirs();
      ImageIO.write(bi, "PNG", file);

    } catch (Exception ie) {
      ie.printStackTrace();
    }
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    if (image != null) {
      g2.drawImage(image, 0, 0, null);
    }
  }

}
