package com.gustlogix.rickandmorty.view.episode;

import com.gustlogix.rickandmorty.dto.episode.EpisodeResult;

import java.util.List;

public interface EpisodesView {
    void hideProgress();

    void showProgress();

    void addNewEpisodesToView(List<EpisodeResult> episodes);

    void navigateToCharactersView(List<Integer> characterIds);

    void showMessage(String message);
}
