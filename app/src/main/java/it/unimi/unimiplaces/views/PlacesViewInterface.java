package it.unimi.unimiplaces.views;

import java.util.List;

import it.unimi.unimiplaces.LookupTableEntry;

/**
 * PlacesViewInterface
 */
public interface PlacesViewInterface {
    void showNoResults();
    void clearResults();
    void setResults(List<LookupTableEntry> results);
}
