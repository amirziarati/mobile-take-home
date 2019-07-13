package com.gustlogix.rickandmorty.dto.character;

import java.util.List;

public class CharacterResponse {
    private Info info;
    private List<Result> results;

    public CharacterResponse(){}

    public CharacterResponse(Info info, List<Result> results) {
        this.info = info;
        this.results = results;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }
}
