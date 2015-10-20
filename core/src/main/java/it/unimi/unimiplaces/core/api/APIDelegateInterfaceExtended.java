package it.unimi.unimiplaces.core.api;

import java.util.List;

import it.unimi.unimiplaces.core.model.BaseEntity;

/**
 * APIDelegateInterface with additional methods for API that
 * not return BaseEntity objects
 */
public interface APIDelegateInterfaceExtended extends APIDelegateInterface {
    void apiServiceAvailableRequestEnd(List<BaseEntity> results);
    void apiFloorMapAtURLEnd(String floormap);
    void apiRoomTimeTableEnd(List<BaseEntity> events);
}
