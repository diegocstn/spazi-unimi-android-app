package it.unimi.unimiplaces;

import java.util.List;

import it.unimi.unimiplaces.core.model.BaseEntity;

/**
 * PresenterViewInterface
 * Defines the API for presenter (fragment or activities)
 */
public interface PresenterViewInterface {
    void setModel(List<BaseEntity> model);
}
