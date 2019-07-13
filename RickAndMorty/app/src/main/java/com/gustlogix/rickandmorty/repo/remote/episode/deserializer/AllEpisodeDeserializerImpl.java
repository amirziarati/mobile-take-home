package com.gustlogix.rickandmorty.repo.remote.episode.deserializer;

import com.gustlogix.rickandmorty.dto.episode.AllEpisodeResponse;
import com.gustlogix.rickandmorty.dto.episode.EpisodeResult;
import com.gustlogix.rickandmorty.dto.episode.Info;
import com.gustlogix.rickandmorty.repo.remote.network.JsonDeserializer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class AllEpisodeDeserializerImpl implements JsonDeserializer<AllEpisodeResponse> {

    private JsonDeserializer<List<EpisodeResult>> multipleEpisodeDeserializer;

    public AllEpisodeDeserializerImpl(JsonDeserializer<List<EpisodeResult>> multipleEpisodeDeserializer) {
        this.multipleEpisodeDeserializer = multipleEpisodeDeserializer;
    }

    @Override
    public AllEpisodeResponse deserialize(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        AllEpisodeResponse response = new AllEpisodeResponse();
        response.setInfo(extractInfo(jsonObject.getJSONObject("info")));
        response.setResults(multipleEpisodeDeserializer.deserialize(jsonObject.getJSONArray("results").toString()));
        return response;
    }

    private Info extractInfo(JSONObject jsonObject) throws JSONException {
        Info info = new Info();
        int count = jsonObject.getInt("count");
        int pages = jsonObject.getInt("pages");
        String next = jsonObject.getString("next");
        String prev = jsonObject.getString("prev");
        info.setCount(count);
        info.setPages(pages);
        info.setNext(next);
        info.setPrev(prev);
        return info;
    }
}
