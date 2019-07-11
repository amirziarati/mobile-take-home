package com.gustlogix.rickandmorty.repo.remote.character;


import com.gustlogix.rickandmorty.dto.CharacterResponse;
import com.gustlogix.rickandmorty.dto.Info;
import com.gustlogix.rickandmorty.dto.Location;
import com.gustlogix.rickandmorty.dto.Origin;
import com.gustlogix.rickandmorty.dto.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CharacterJsonDeserializerImpl implements CharacterJsonDeserializer {

    @Override
    public CharacterResponse deserialize(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONObject infoObject = jsonObject.getJSONObject("info");
        CharacterResponse locationResponse = new CharacterResponse();
        Info info = new Info();
        info.setCount(infoObject.getInt("count"));
        info.setNext(infoObject.getString("next"));
        info.setPages(infoObject.getInt("pages"));
        info.setPrev(infoObject.getString("prev"));
        locationResponse.setInfo(info);
        JSONArray resultArray = jsonObject.getJSONArray("results");
        List<Result> resultList = new ArrayList<>();
        for (int i = 0; i < resultArray.length(); i++) {
            Result result = new Result();
            result.setCreated(resultArray.getJSONObject(i).getString("created"));
            result.setGender(resultArray.getJSONObject(i).getString("gender"));
            result.setId(resultArray.getJSONObject(i).getInt("id"));
            result.setName(resultArray.getJSONObject(i).getString("name"));
            result.setType(resultArray.getJSONObject(i).getString("type"));
            result.setUrl(resultArray.getJSONObject(i).getString("url"));
            result.setSpecies(resultArray.getJSONObject(i).getString("species"));
            result.setType(resultArray.getJSONObject(i).getString("type"));
            result.setImage(resultArray.getJSONObject(i).getString("image"));
            result.setStatus(resultArray.getJSONObject(i).getString("status"));

            JSONObject originJsonObject = resultArray.getJSONObject(i).getJSONObject("origin");
            Origin origin = new Origin();
            origin.setName(originJsonObject.getString("name"));
            origin.setUrl(originJsonObject.getString("url"));
            result.setOrigin(origin);

            JSONObject locationJsonObject = resultArray.getJSONObject(i).getJSONObject("location");
            Location location = new Location();
            location.setName(locationJsonObject.getString("name"));
            location.setUrl(locationJsonObject.getString("url"));
            result.setLocation(location);

            resultList.add(result);
        }
        locationResponse.setResults(resultList);
        return locationResponse;
    }
}
