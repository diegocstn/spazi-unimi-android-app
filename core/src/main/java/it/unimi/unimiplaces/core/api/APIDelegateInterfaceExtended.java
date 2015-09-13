package it.unimi.unimiplaces.core.api;

import java.util.List;

/**
 * APIDelegateInterface with additional methods for API that
 * not return BaseEntity objects
 */
public interface APIDelegateInterfaceExtended extends APIDelegateInterface {
    void apiServiceAvailableRequestEnd(List<String> results);
}
