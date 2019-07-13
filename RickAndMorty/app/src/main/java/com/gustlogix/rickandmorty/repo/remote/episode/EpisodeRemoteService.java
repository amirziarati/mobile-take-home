package com.gustlogix.rickandmorty.repo.remote.episode;

import com.gustlogix.rickandmorty.dto.episode.AllEpisodeResponse;
import com.gustlogix.rickandmorty.dto.episode.EpisodeResult;
import com.gustlogix.rickandmorty.repo.RepositoryCallback;

import java.util.List;

public interface EpisodeRemoteService {

    void fetchSingleEpisode(int id, RepositoryCallback<EpisodeResult> callback);

    void fetchMultipleEpisodes(List<Integer> episodeIds, RepositoryCallback<List<EpisodeResult>> callback);

    void fetchAllEpisodes(int page, RepositoryCallback<AllEpisodeResponse> callback);
}
