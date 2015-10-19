package it.unimi.unimiplaces.views;

import java.util.List;

import it.unimi.unimiplaces.LookupTableEntry;

/**
 * PlacesViewInterface
 */
public interface PlacesViewInterface {
    void showNoResults();
    void setResults(List<LookupTableEntry> results);
}
