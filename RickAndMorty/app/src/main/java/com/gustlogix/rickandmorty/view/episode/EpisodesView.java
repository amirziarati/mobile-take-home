package com.gustlogix.rickandmorty.view.episode;

import com.gustlogix.rickandmorty.dto.episode.EpisodeResult;

import java.util.ArrayList;
import java.util.List;

public interface EpisodesView {
    void hideProgress();

    void showProgress();

    void addNewEpisodesToView(List<EpisodeResult> episodes);

    void navigateToCharactersView(ArrayList<Integer> characterIds);

    void showMessage(String message);
}
