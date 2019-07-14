package com.gustlogix.rickandmorty.repo;

import com.gustlogix.rickandmorty.dto.episode.AllEpisodeResponse;
import com.gustlogix.rickandmorty.dto.episode.EpisodeResult;

import java.util.List;

public interface EpisodeRepository {

    void getSingleEpisode(int id, RepositoryCallback<EpisodeResult> callback);

    void getMultipleEpisodes(List<Integer> ids, RepositoryCallback<List<EpisodeResult>> callback);

    void getAllEpisodes(int page, RepositoryCallback<AllEpisodeResponse> callback);
}
