
package com.gustlogix.rickandmorty.dto.episode;

import java.util.ArrayList;
import java.util.List;

public class EpisodeResult {

    private Integer id;
    private String name;
    private String airDate;
    private String episode;
    private List<String> characters = null;
    private String url;
    private String created;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAirDate() {
        return airDate;
    }

    public void setAirDate(String airDate) {
        this.airDate = airDate;
    }

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public List<String> getCharacters() {
        return characters;
    }

    public List<Integer> getCharactersIds() {
        ArrayList<Integer> ids = new ArrayList<>();
        for (String url : characters) {
            if (url.endsWith("/")) {
                url = url.substring(0, url.length() - 1);
            }
            String stringID = url.substring(url.lastIndexOf('/') + 1);
            ids.add(Integer.parseInt(stringID));
        }
        return ids;
    }


    public void setCharacters(List<String> characters) {
        this.characters = characters;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

}
