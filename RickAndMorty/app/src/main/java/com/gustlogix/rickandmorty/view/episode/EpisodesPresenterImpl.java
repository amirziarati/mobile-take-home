package com.gustlogix.rickandmorty.view.episode;

import android.util.Log;

import com.gustlogix.rickandmorty.dto.Response;
import com.gustlogix.rickandmorty.dto.episode.AllEpisodeResponse;
import com.gustlogix.rickandmorty.dto.episode.EpisodeResult;
import com.gustlogix.rickandmorty.repo.EpisodeRepository;
import com.gustlogix.rickandmorty.repo.RepositoryCallback;
import java.io.FileNotFoundException;

public class EpisodesPresenterImpl implements EpisodesPresenter {

    private EpisodeRepository episodeRepository;
    private EpisodesView view;
    private boolean isMoreDataAvailable = true;
    private int page = 1;

    public EpisodesPresenterImpl(EpisodesView episodesView, EpisodeRepository episodeRepository) {
        this.episodeRepository = episodeRepository;
        this.view = episodesView;
    }

    @Override
    public void onInit() {
    }

    @Override
    public void onNewPageOfDataRequested() {
        if (!isMoreDataAvailable) {
            view.showMessage("no more data available");
            return;
        }

        view.showProgress();

        episodeRepository.getAllEpisodes(page, new RepositoryCallback<AllEpisodeResponse>() {

            @Override
            public void onSuccess(Response<AllEpisodeResponse> response) {
                AllEpisodeResponse episodes = response.getResult();
                isMoreDataAvailable = episodes.getInfo().getNext() != null && !episodes.getInfo().getNext().trim().equals("");
                page++;
                view.hideProgress();
                view.addNewEpisodesToView(episodes.getResults());
            }

            @Override
            public void onError(Exception e) {
                view.hideProgress();
                view.showMessage("there was a problem fetching data !!!");
                if (e instanceof FileNotFoundException) {
                    view.showMessage("no more data available");
                } else {
                    Log.e("R&M", e.toString());
                }
            }

        });
    }

    @Override
    public void onEpisodeClicked(EpisodeResult episodeResult) {
        view.navigateToCharactersView(episodeResult.getCharactersIds());
    }
}
