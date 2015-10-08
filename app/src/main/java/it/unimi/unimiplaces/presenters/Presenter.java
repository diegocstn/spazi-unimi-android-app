package it.unimi.unimiplaces.presenters;

import it.unimi.unimiplaces.core.model.BaseEntity;

/**
 * Presenter, used to decoupling presenter and view
 */
public interface Presenter {
    void init(String lang);
    void filterModelWithFilterAtIndex(int index);
    BaseEntity payloadForDetailAtIndex(int index);
}
