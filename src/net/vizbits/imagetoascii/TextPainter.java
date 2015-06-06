package net.vizbits.imagetoascii;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class TextPainter extends JFrame {
  public TextPainter(List<String> texts, int rows, int columns) {
    super("Image Preview");
    TextPanel panel = new TextPanel(texts, rows, columns);
    JScrollPane scroller = new JScrollPane(panel);
    this.getContentPane().add(scroller);
    this.pack();
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setVisible(true);
    this.setSize(500, 500);
  }

  public static void main(String[] args) {
    ArrayList<String> texts = new ArrayList<String>();
    for (int i = 0; i < 100; i++) {
      texts
          .add("Stringasdfsdf sdjfaskjf asjfg sajfhg safghjksa fkjasgdf .........////////askdfjsgadfhgfdhjga fksa");
    }

    new TextPainter(texts, 100, texts.get(0).length());
  }

}
