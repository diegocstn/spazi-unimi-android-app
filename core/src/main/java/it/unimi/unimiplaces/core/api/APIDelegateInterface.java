package it.unimi.unimiplaces.core.api;

import java.util.ArrayList;

import it.unimi.unimiplaces.core.model.BaseEntity;

public interface APIDelegateInterface {
    void apiRequestStart();
    void apiRequestEnd(ArrayList<BaseEntity> results);
}
