package it.unimi.unimiplaces.core.api;

import java.util.List;

import it.unimi.unimiplaces.core.model.BaseEntity;

public interface APIDelegateInterface {
    void apiRequestStart();
    void apiRequestEnd(List<BaseEntity> results);
}
