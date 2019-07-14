package com.gustlogix.rickandmorty.repo.remote.episode;

import com.gustlogix.rickandmorty.dto.episode.AllEpisodeResponse;
import com.gustlogix.rickandmorty.dto.episode.EpisodeResult;
import com.gustlogix.rickandmorty.repo.remote.RemoteRepositoryCallback;

import java.util.List;

public interface EpisodeRemoteService {

    void fetchSingleEpisode(int id, RemoteRepositoryCallback<EpisodeResult> callback);

    void fetchMultipleEpisodes(List<Integer> episodeIds, RemoteRepositoryCallback<List<EpisodeResult>> callback);

    void fetchAllEpisodes(int page, RemoteRepositoryCallback<AllEpisodeResponse> callback);
}
