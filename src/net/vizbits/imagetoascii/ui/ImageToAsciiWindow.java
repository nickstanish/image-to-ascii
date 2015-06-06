package net.vizbits.imagetoascii.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import net.vizbits.imagetoascii.Canvas;
import net.vizbits.imagetoascii.loading.LoadingService;
import net.vizbits.imagetoascii.service.AsciiService;

public class ImageToAsciiWindow extends JFrame {

  private JToolBar toolbar;
  private Canvas canvas;
  private AsciiService asciiService;
  private JFileChooser fileChooser;
  private File selectedFile;
  private LoadingService loadingService;
  private BufferedImage currentImage;


  public ImageToAsciiWindow(AsciiService asciiService) {
    super("Image To Ascii");
    this.asciiService = asciiService;
    asciiService.setCallback(image -> asciiFinishedProcessing(image));
    initialize();
    pack();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
    setExtendedState(JFrame.MAXIMIZED_BOTH);
  }

  private JMenuBar createMenuBar() {
    JMenuBar menuBar = new JMenuBar();
    JMenu fileMenu = new JMenu("File");
    JMenuItem openItem = new JMenuItem("Open");
    JMenuItem saveItem = new JMenuItem("Save");
    JMenuItem zoomInItem = new JMenuItem("Zoom In");
    JMenuItem zoomOutItem = new JMenuItem("Zoom Out");
    openItem.addActionListener(event -> open(event));
    saveItem.addActionListener(event -> save(event));
    zoomInItem.addActionListener(event -> zoomIn(event));
    zoomOutItem.addActionListener(event -> zoomOut(event));
    fileMenu.add(openItem);
    fileMenu.add(saveItem);
    fileMenu.add(zoomInItem);
    fileMenu.add(zoomOutItem);
    menuBar.add(fileMenu);

    InputMap map = menuBar.getInputMap(JMenuBar.WHEN_IN_FOCUSED_WINDOW);
    ActionMap actionMap = menuBar.getActionMap();
    map.put(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK), "open");
    map.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK), "save");

    map.put(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, InputEvent.CTRL_DOWN_MASK), "plus");
    map.put(
        KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, KeyEvent.CTRL_DOWN_MASK
            | KeyEvent.SHIFT_DOWN_MASK), "plus");
    map.put(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, InputEvent.CTRL_DOWN_MASK), "minus");
    map.put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, InputEvent.CTRL_DOWN_MASK), "minus");
    AbstractAction openAction = new AbstractAction() {

      @Override
      public void actionPerformed(ActionEvent e) {
        open(e);

      }
    };
    AbstractAction saveAction = new AbstractAction() {

      @Override
      public void actionPerformed(ActionEvent e) {
        save(e);

      }
    };
    AbstractAction plusAction = new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        zoomIn(e);
      }
    };
    AbstractAction minusAction = new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        zoomOut(e);
      }
    };

    actionMap.put("open", openAction);
    actionMap.put("save", saveAction);
    actionMap.put("plus", plusAction);
    actionMap.put("minus", minusAction);
    return menuBar;
  }

  private void initialize() {
    setJMenuBar(createMenuBar());


    loadingService = new LoadingService();
    loadingService.addIndicatorCallback(isLoading -> loadingIndicatorChanged(isLoading));

    fileChooser = new JFileChooser("");
    fileChooser.setAcceptAllFileFilterUsed(false);
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    fileChooser.setFileFilter(new ImageFilter());

    toolbar = new JToolBar(JToolBar.HORIZONTAL);
    toolbar.setFloatable(false);

    JButton openButton = new JButton("Open");
    JButton saveButton = new JButton("Save");
    JButton zoomInButton = new JButton("Zoom In");
    JButton zoomOutButton = new JButton("Zoom Out");
    zoomInButton.addActionListener(event -> zoomIn(event));
    zoomOutButton.addActionListener(event -> zoomOut(event));
    openButton.addActionListener(event -> open(event));
    saveButton.addActionListener(event -> save(event));
    addComponents(toolbar, openButton, saveButton, zoomInButton, zoomOutButton);

    canvas = new Canvas();
    JScrollPane scroller = new JScrollPane(canvas);

    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());
    contentPane.add(toolbar, BorderLayout.NORTH);
    contentPane.add(scroller, BorderLayout.CENTER);
  }

  private void zoomOut(ActionEvent event) {
    canvas.zoomOut();
  }

  private void zoomIn(ActionEvent event) {
    canvas.zoomIn();
  }

  private static void addComponents(Container parent, Container... children) {
    for (Component child : children) {
      parent.add(child);
    }
  }

  private void open(ActionEvent event) {
    loadingService.showLoadingIndicator();
    int result = fileChooser.showOpenDialog(this);
    if (result == JFileChooser.APPROVE_OPTION) {
      selectedFile = fileChooser.getSelectedFile();
      BufferedImage image;
      try {
        image = ImageIO.read(selectedFile);
      } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this,
            "Unable to open this image, please try a different file");
        loadingService.hideLoadingIndicator();
        return;
      }

      asciiService.process(image, AsciiService.createDefaultOptionSet());
    } else {
      loadingService.hideLoadingIndicator();
    }
  }

  private void save(ActionEvent event) {
    if (currentImage == null) {
      JOptionPane.showMessageDialog(this, "There is no image to save...");
      return;
    }
    loadingService.showLoadingIndicator();
    int result = fileChooser.showSaveDialog(this);
    if (result == JFileChooser.APPROVE_OPTION) {
      final File selectedFile = fileChooser.getSelectedFile();
      selectedFile.mkdirs();

      new Thread() {
        public void run() {
          try {
            File file = addFileExtension(selectedFile, ".png");
            if (file.exists()) {
              int overwrite =
                  JOptionPane.showConfirmDialog(null,
                      "Are you sure you want to overwrite this file?");
              if (overwrite != JOptionPane.OK_OPTION) {
                loadingService.hideLoadingIndicator();
                return;
              }
            }
            ImageIO.write(currentImage, "PNG", file);
            loadingService.hideLoadingIndicator();
            JOptionPane.showMessageDialog(null, "Saved!");
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occured while saving your image");

          }
        }
      }.start();

    }

  }

  private void asciiFinishedProcessing(BufferedImage image) {
    loadingService.hideLoadingIndicator();
    if (image != null) {
      canvas.setImage(image);
      currentImage = image;
    }

  }

  private void loadingIndicatorChanged(Boolean isLoading) {
    System.out.println("Loading: " + isLoading);
    canvas.setLoading(isLoading);
  }

  public static File addFileExtension(File file, String extension) throws IOException {
    String name = file.getCanonicalPath();
    if (!name.endsWith(extension)) {
      int index = name.lastIndexOf('.');
      if (index > 0 && index > name.lastIndexOf(File.separator)) {
        // existing extension
        name = name.substring(0, index);
      }
      name = name.concat(extension);
    }
    return new File(name);
  }

}
