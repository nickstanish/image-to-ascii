package net.vizbits.imagetoascii;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.Timer;

import net.vizbits.imagetoascii.loading.Indicator;

public class Canvas extends JPanel {
  private BufferedImage currentImage;
  private double scale = 1.0;
  private double oldScale;
  private static final double minscale = 0.05;
  private static final double scale_inc = 0.10;
  private Indicator indicator;
  private boolean isLoading = false;

  private Timer timer = new Timer((int) (1.0 / 30.0 * 1000), event -> repaintTimer(event));

  private void repaintTimer(ActionEvent event) {
    repaint();
  }

  public void setLoading(boolean loading) {
    this.isLoading = loading;
    if (!loading) {
      resetSize();
    }

    repaint();
  }

  public Canvas() {
    this(null);
  }

  public Canvas(BufferedImage image) {
    this.indicator = new Indicator(Color.black, 10, 3);
    this.currentImage = image;
    if (this.currentImage == null) {
      this.currentImage =
          new BufferedImage(Constants.DEFAULT_WIDTH, Constants.DEFAULT_HEIGHT,
              BufferedImage.TYPE_INT_ARGB);
      Graphics2D g = (Graphics2D) currentImage.createGraphics();
      g.setColor(Color.white);
      g.fillRect(0, 0, this.currentImage.getWidth(), this.currentImage.getHeight());
    }
    resetSize();

    timer.start();
  }


  public void setImage(BufferedImage image) {
    this.currentImage = image;
    resetSize();
  }

  public BufferedImage getImage() {
    return currentImage;
  }

  public void paintComponent(Graphics g1) {
    Graphics2D g = (Graphics2D) g1;
    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g.setColor(Color.lightGray);
    g.fillRect(0, 0, getWidth(), getHeight());
    Rectangle2D drawingArea =
        new Rectangle2D.Double(Constants.PADDING - 1, Constants.PADDING - 1,
            getScaledWidth(currentImage) + 2, getScaledHeight(currentImage) + 2);
    g.setColor(Color.black);

    if (!isLoading) {
      g.fill(drawingArea);
      if (this.currentImage != null) {
        g.drawImage(currentImage, Constants.PADDING, Constants.PADDING,
            getScaledWidth(currentImage), getScaledHeight(currentImage), null);
      }
    } else {

      setPreferredSize(new Dimension(getRootPane().getWidth() - 100,
          getRootPane().getHeight() - 100));
      this.revalidate();
      indicator.draw(g, getWidth(), getHeight());

    }
  }

  public double getScale() {
    return this.scale;
  }

  public void setScale(double scale) {
    this.scale = scale;
  }

  private int getScaledWidth(BufferedImage image) {
    return (int) (image.getWidth() * scale);
  }

  private int getScaledHeight(BufferedImage image) {
    return (int) (image.getHeight() * scale);
  }

  private void resetSize() {
    int width = (int) (currentImage.getWidth() * scale) + Constants.PADDING * 2;
    int height = (int) (currentImage.getHeight() * scale) + Constants.PADDING * 2;
    this.setPreferredSize(new Dimension(width, height));
    this.revalidate();
  }

  public void zoomIn() {
    scale += scale_inc;
    System.out.println("plus");
    resetSize();
  }

  public void zoomOut() {
    scale -= scale_inc;
    System.out.println("minus");
    if (scale <= minscale)
      scale = minscale;
    resetSize();
  }
}
