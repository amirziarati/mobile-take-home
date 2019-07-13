
package com.gustlogix.rickandmorty.dto.episode;

import java.util.List;

public class AllEpisodeResponse {

    private Info info;
    private List<EpisodeResult> results = null;

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public List<EpisodeResult> getResults() {
        return results;
    }

    public void setResults(List<EpisodeResult> results) {
        this.results = results;
    }

}
