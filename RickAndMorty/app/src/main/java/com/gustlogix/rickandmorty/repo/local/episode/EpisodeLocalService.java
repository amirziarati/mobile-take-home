package com.gustlogix.rickandmorty.repo.local.episode;

import com.gustlogix.rickandmorty.dto.episode.AllEpisodeResponse;
import com.gustlogix.rickandmorty.dto.episode.EpisodeResult;
import com.gustlogix.rickandmorty.repo.local.LocalRepositoryCallback;
import com.gustlogix.rickandmorty.repo.local.OnLocalDataUpdateListener;

import java.util.List;

public interface EpisodeLocalService {

    void fetchSingleEpisode(int id, LocalRepositoryCallback<EpisodeResult> callback);

    void fetchMultipleEpisodes(List<Integer> characterIds, LocalRepositoryCallback<List<EpisodeResult>> callback);

    void fetchAllEpisodes(int page, int pageSize, LocalRepositoryCallback<AllEpisodeResponse> callback);

    void updateEpisode(EpisodeResult data);

    void updateEpisodes(List<EpisodeResult> data, OnLocalDataUpdateListener onLocalDataUpdateListener);
}
