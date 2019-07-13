package com.gustlogix.rickandmorty.repo.remote.character.deserializer;

import com.gustlogix.rickandmorty.dto.character.CharacterResult;
import com.gustlogix.rickandmorty.repo.remote.network.JsonDeserializer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MultipleCharacterDeserializerImpl implements JsonDeserializer<List<CharacterResult>> {

    private JsonDeserializer<CharacterResult> singleCharacterDeserializer;

    public MultipleCharacterDeserializerImpl(JsonDeserializer<CharacterResult> singleCharacterDeserializer) {
        this.singleCharacterDeserializer = singleCharacterDeserializer;
    }

    @Override
    public List<CharacterResult> deserialize(String jsonString) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonString);
        List<CharacterResult> characterResultList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject wholeResultObject = jsonArray.getJSONObject(i);
            characterResultList.add(singleCharacterDeserializer.deserialize(wholeResultObject.toString()));
        }
        return characterResultList;
    }
}
