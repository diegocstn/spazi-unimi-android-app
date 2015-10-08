package it.unimi.unimiplaces.presenters;

import java.util.List;

import it.unimi.unimiplaces.core.model.BaseEntity;

/**
 * PresenterViewInterface
 * Defines the API for presenter view (fragment or activities)
 */
public interface PresenterViewInterface {
    void setModel(List<BaseEntity> model);
    void setDetailActionListener(PresenterViewInterface listener);
    void onDetailActionListener(int index);
    void clearListeners();
}
