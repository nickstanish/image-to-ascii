package net.vizbits.imagetoascii;



public class PixelHelper {

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
