package com.gustlogix.rickandmorty.repo.remote.episode.deserializer;

import com.gustlogix.rickandmorty.dto.episode.EpisodeResult;
import com.gustlogix.rickandmorty.repo.remote.network.JsonDeserializer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SingleEpisodeDeserializerImpl implements JsonDeserializer<EpisodeResult> {

    @Override
    public EpisodeResult deserialize(String jsonString) throws JSONException {
        EpisodeResult result = new EpisodeResult();
        JSONObject jsonObject = new JSONObject(jsonString);
        result.setId(jsonObject.getInt("id"));
        result.setName(jsonObject.getString("name"));
        result.setAirDate(jsonObject.getString("air_date"));
        result.setCreated(jsonObject.getString("created"));
        result.setEpisode(jsonObject.getString("episode"));
        result.setUrl(jsonObject.getString("url"));

        JSONArray characters = jsonObject.getJSONArray("characters");
        List<String> characterList = new ArrayList<>();
        for (int i = 0; i < characters.length(); i++) {
            characterList.add(characters.getString(i));
        }
        result.setCharacters(characterList);
        return result;
    }
}
