package com.gustlogix.rickandmorty.repo.remote.character.deserializer;

import com.gustlogix.rickandmorty.dto.character.CharacterResult;
import com.gustlogix.rickandmorty.dto.character.Location;
import com.gustlogix.rickandmorty.dto.character.Origin;
import com.gustlogix.rickandmorty.repo.remote.network.JsonDeserializer;

import org.json.JSONException;
import org.json.JSONObject;

public class SingleCharacterDeserializerImpl implements JsonDeserializer<CharacterResult> {

    @Override
    public CharacterResult deserialize(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);

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

    private Location extractLocation(JSONObject locationObject) throws JSONException {
        Location location = new Location();
        location.setName(locationObject.getString("name"));
        location.setUrl(locationObject.getString("url"));
        return location;
    }

    private Origin extractOrigin(JSONObject originObject) throws JSONException {
        Origin origin = new Origin();
        origin.setName(originObject.getString("name"));
        origin.setUrl(originObject.getString("url"));
        return origin;
    }

}
