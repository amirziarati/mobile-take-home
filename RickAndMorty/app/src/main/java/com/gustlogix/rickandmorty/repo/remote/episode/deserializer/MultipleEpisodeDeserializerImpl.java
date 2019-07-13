package com.gustlogix.rickandmorty.repo.remote.episode.deserializer;

import com.gustlogix.rickandmorty.dto.episode.EpisodeResult;
import com.gustlogix.rickandmorty.repo.remote.network.JsonDeserializer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MultipleEpisodeDeserializerImpl implements JsonDeserializer<List<EpisodeResult>> {

    private JsonDeserializer<EpisodeResult> singleEpisodeDeserializer;

    public MultipleEpisodeDeserializerImpl(JsonDeserializer<EpisodeResult> singleEpisodeDeserializer) {
        this.singleEpisodeDeserializer = singleEpisodeDeserializer;
    }

    @Override
    public List<EpisodeResult> deserialize(String jsonString) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonString);
        List<EpisodeResult> episodeList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject episode = jsonArray.getJSONObject(i);
            episodeList.add(singleEpisodeDeserializer.deserialize(episode.toString()));
        }
        return episodeList;
    }
}
