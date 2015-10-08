package it.unimi.unimiplaces.views;

import it.unimi.unimiplaces.presenters.PresenterViewInterface;

/**
 * PresenterViewInterface
 * Defines the API for presenter view available services
 */
public interface BuildingsViewInterface extends PresenterViewInterface {
    void setAvailableServices(String[] services);
}
