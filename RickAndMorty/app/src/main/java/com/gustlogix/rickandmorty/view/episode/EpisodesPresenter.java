package com.gustlogix.rickandmorty.view.episode;

import com.gustlogix.rickandmorty.dto.episode.EpisodeResult;

public interface EpisodesPresenter {
    void onInit();
    void onNewPageOfDataRequested();
    void onEpisodeClicked(EpisodeResult episodeResult);
}
