package com.gustlogix.rickandmorty.dto.character;

import java.util.List;

public class AllCharacterResponse {
    private Info info;
    private List<CharacterResult> results;

    public AllCharacterResponse(){}

    public AllCharacterResponse(Info info, List<CharacterResult> results) {
        this.info = info;
        this.results = results;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public List<CharacterResult> getResults() {
        return results;
    }

    public void setResults(List<CharacterResult> results) {
        this.results = results;
    }
}
