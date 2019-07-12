package com.gustlogix.rickandmorty.repo.remote.character.deserializer;

import com.gustlogix.rickandmorty.dto.CharacterResult;
import com.gustlogix.rickandmorty.repo.remote.network.JsonDeserializer;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public interface MultipleCharacterDeserializer extends JsonDeserializer<List<CharacterResult>> {
    List<CharacterResult> deserialize(JSONArray jsonArray) throws JSONException;
}
