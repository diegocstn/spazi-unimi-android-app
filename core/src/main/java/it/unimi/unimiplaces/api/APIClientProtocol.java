package it.unimi.unimiplaces.api;

import java.util.ArrayList;

import it.unimi.unimiplaces.model.BaseEntity;

public interface APIClientProtocol {
    void apiRequestStart();
    void apiRequestEnd(ArrayList<BaseEntity> results);
}
