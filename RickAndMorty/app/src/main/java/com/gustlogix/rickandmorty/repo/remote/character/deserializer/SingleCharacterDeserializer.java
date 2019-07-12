package com.gustlogix.rickandmorty.repo.remote.character.deserializer;

import com.gustlogix.rickandmorty.dto.CharacterResult;
import com.gustlogix.rickandmorty.repo.remote.network.JsonDeserializer;

import org.json.JSONException;
import org.json.JSONObject;

public interface SingleCharacterDeserializer extends JsonDeserializer<CharacterResult> {
    CharacterResult deserialize(JSONObject jsonString) throws JSONException;
}
