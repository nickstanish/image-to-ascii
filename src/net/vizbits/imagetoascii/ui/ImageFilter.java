package net.vizbits.imagetoascii.ui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ImageFilter extends FileFilter {

  private static String[] accepted = {"gif", "jpeg", "jpg", "png", "bmp", "wbmp"};

  @Override
  public boolean accept(File file) {
    if (file.isDirectory()) {
      return true;
    }
    String extension = getExtension(file);
    if (extension != null) {
      for (String accept : accepted) {
        if (extension.equalsIgnoreCase(accept)) {
          return true;
        }

      }
    }
    return false;
  }

  @Override
  public String getDescription() {
    return "GIF, JPEG, PNG, BMP, WBMP Images Only";
  }

  public static String getExtension(File f) {
    String ext = null;
    String s = f.getName();
    int i = s.lastIndexOf('.');

    if (i > 0 && i < s.length() - 1) {
      ext = s.substring(i + 1).toLowerCase();
    }
    return ext;
  }

}
