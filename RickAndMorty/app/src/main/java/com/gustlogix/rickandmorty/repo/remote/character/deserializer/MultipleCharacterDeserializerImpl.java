package com.gustlogix.rickandmorty.repo.remote.character.deserializer;

import com.gustlogix.rickandmorty.dto.CharacterResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MultipleCharacterDeserializerImpl implements MultipleCharacterDeserializer {

    SingleCharacterDeserializer singleCharacterDeserializer;

    public MultipleCharacterDeserializerImpl(SingleCharacterDeserializer singleCharacterDeserializer) {
        this.singleCharacterDeserializer = singleCharacterDeserializer;
    }

    @Override
    public List<CharacterResult> deserialize(String jsonString) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonString);
        return deserialize(jsonArray);
    }

    @Override
    public List<CharacterResult> deserialize(JSONArray jsonArray) throws JSONException {
        List<CharacterResult> characterResultList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject wholeResultObject = jsonArray.getJSONObject(i);
            characterResultList.add(singleCharacterDeserializer.deserialize(wholeResultObject));
        }
        return characterResultList;
    }
}
