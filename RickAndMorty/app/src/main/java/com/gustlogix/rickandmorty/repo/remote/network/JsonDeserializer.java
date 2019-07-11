package com.gustlogix.rickandmorty.repo.remote.network;

import org.json.JSONException;

public interface JsonDeserializer<T> {

    T deserialize(String jsonString) throws JSONException;
}
