package com.gustlogix.rickandmorty.repo.remote.character.deserializer;

import com.gustlogix.rickandmorty.dto.CharacterResult;
import com.gustlogix.rickandmorty.dto.Location;
import com.gustlogix.rickandmorty.dto.Origin;

import org.json.JSONException;
import org.json.JSONObject;

public class SingleCharacterDeserializerImpl implements SingleCharacterDeserializer {

    @Override
    public CharacterResult deserialize(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        return deserialize(jsonObject);
    }

    @Override
    public CharacterResult deserialize(JSONObject jsonObject) throws JSONException {
        CharacterResult characterResult = new CharacterResult();
        characterResult.setCreated(jsonObject.getString("created"));
        characterResult.setGender(jsonObject.getString("gender"));
        characterResult.setId(jsonObject.getInt("id"));
        characterResult.setName(jsonObject.getString("name"));
        characterResult.setType(jsonObject.getString("type"));
        characterResult.setUrl(jsonObject.getString("url"));
        characterResult.setSpecies(jsonObject.getString("species"));
        characterResult.setType(jsonObject.getString("type"));
        characterResult.setImage(jsonObject.getString("image"));
        characterResult.setStatus(jsonObject.getString("status"));

        JSONObject originJsonObject = jsonObject.getJSONObject("origin");
        characterResult.setOrigin(extractOrigin(originJsonObject));

        JSONObject locationJsonObject = jsonObject.getJSONObject("location");
        characterResult.setLocation(extractLocation(locationJsonObject));
        return characterResult;
    }

    Location extractLocation(JSONObject locationObject) throws JSONException {
        Location location = new Location();
        location.setName(locationObject.getString("name"));
        location.setUrl(locationObject.getString("url"));
        return location;
    }

    Origin extractOrigin(JSONObject originObject) throws JSONException {
        Origin origin = new Origin();
        origin.setName(originObject.getString("name"));
        origin.setUrl(originObject.getString("url"));
        return origin;
    }

}
