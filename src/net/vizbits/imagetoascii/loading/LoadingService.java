package net.vizbits.imagetoascii.loading;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class LoadingService {

  private Integer loading;
  private List<Consumer<Boolean>> callbacks;

  public LoadingService() {
    loading = new Integer(0);
    callbacks = new ArrayList<Consumer<Boolean>>();
  }

  public void showLoadingIndicator() {
    synchronized (loading) {
      loading++;
      callCallbacks();
    }
  }

  public void hideLoadingIndicator() {
    synchronized (loading) {
      loading--;
      callCallbacks();
    }
  }

  private void callCallbacks() {
    for (Consumer<Boolean> callback : callbacks) {
      callback.accept(isLoading());
    }
  }

  public boolean isLoading() {
    return loading > 0;
  }

  public void addIndicatorCallback(Consumer<Boolean> callback) {
    this.callbacks.add(callback);
  }


}
