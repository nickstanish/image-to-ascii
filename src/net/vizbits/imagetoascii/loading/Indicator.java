package net.vizbits.imagetoascii.loading;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;


public class Indicator {

  public static double DEFAULT_SPEED = 3;
  public static int DEFAULT_SECTORS = 7;
  public static Color DEFAULT_COLOR = Color.gray;

  private static double dradius = 20;

  private double ds = DEFAULT_SPEED;
  private Color color = DEFAULT_COLOR;
  private int n = DEFAULT_SECTORS;

  private double start = 0;
  private double min = 0;
  private double width = 0;
  private double height = 0;
  private double angle = 0; // angle length
  private double dangles = 0; // distance between next angle

  double band2start = 0;
  double band3start = 0;

  public void calculatePositions(int width, int height) {
    this.width = width;
    this.height = height;
    min = Math.min(width, height);

    angle = 2 * dangles / 3;
    start += ds;
    start %= 360;
    band2start -= ds;
    band2start %= 360;
    band3start += ds;
    band3start %= 360;
  }

  /**
   * Constructor with options
   * 
   * @param c: Color
   * @param sectors: int (0,50] of number of sectors
   * @param speed: double (0,5] movement speed (NOT REFRESH RATE: which is set to 40 FPS)
   */
  public Indicator(Color c, int sectors, double speed) {
    super();
    if (c != null)
      this.color = c;
    if (sectors > 0 && sectors <= 50)
      n = sectors;
    if (speed > 0 && speed <= 5)
      ds = speed;
    dangles = 360.0 / n;
  }


  public void draw(Graphics2D g, int width, int height) {
    calculatePositions(width, height);
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g.setColor(color);
    g.setStroke(new BasicStroke((int) min * 0.01f + 10, BasicStroke.CAP_BUTT,
        BasicStroke.JOIN_BEVEL));
    double left = (width - (min - (dradius * 4))) / 2.0;
    double top = (height - (min - (dradius * 4))) / 2.0;
    for (int i = 0; i < n; i++) {
      g.setColor(Color.blue);
      g.fill(new Arc2D.Double(left, top, min - (dradius * 4), min - dradius * 4,
          (int) (band3start + i * dangles), angle, Arc2D.PIE));
    }
    left = (width - (min - (dradius * 2))) / 2.0;
    top = (height - (min - (dradius * 2))) / 2.0;
    for (int i = 0; i < n; i++) {
      g.setColor(Color.DARK_GRAY);
      g.draw(new Arc2D.Double(left, top, min - dradius * 2, min - dradius * 2,
          (int) (band2start + i * dangles), angle, Arc2D.OPEN));
    }
    left = (width - (min - dradius)) / 2.0;
    top = (height - (min - dradius)) / 2.0;
    for (int i = 0; i < n; i++) {
      g.setColor(color);
      g.draw(new Arc2D.Double(left, top, min - dradius, min - dradius, (int) (start + i * dangles),
          angle, Arc2D.OPEN));
    }

  }

}
