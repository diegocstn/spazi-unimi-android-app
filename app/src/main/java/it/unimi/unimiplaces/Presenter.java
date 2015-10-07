package it.unimi.unimiplaces;

/**
 * Presenter, used to decoupling presenter and view
 */
public interface Presenter {
    void init();
    void showDetailAtIndex(int index);
}
