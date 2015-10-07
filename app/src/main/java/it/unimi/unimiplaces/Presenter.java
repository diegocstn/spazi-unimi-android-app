package it.unimi.unimiplaces;

/**
 * Presenter, used to decoupling presenter and view
 */
public interface Presenter {
    void init(String lang);
    void filterModelWithFilterAtIndex(int index);
    void showDetailAtIndex(int index);
}
