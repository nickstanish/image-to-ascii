package net.vizbits.imagetoascii.service.helpers;

import java.util.HashMap;
import java.util.Map;

public class AsciiOptionsSet {
  public static final String KEY_BLOCK_SIZE = "key_block_size";
  public static final Integer VALUE_BLOCK_SMALL = 1;
  public static final Integer VALUE_BLOCK_MEDIUM = 4;
  public static final Integer VALUE_BLOCK_LARGE = 8;



  private Map<String, Integer> optionMap;

  public AsciiOptionsSet() {
    optionMap = new HashMap<String, Integer>();
  }

  public void addOption(String key, Integer value) {
    optionMap.put(key, value);
  }

  public Integer getOption(String key) {
    return optionMap.get(key);
  }
}
