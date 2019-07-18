package com.gustlogix.rickandmorty.view.episode;

import android.util.Log;

import com.gustlogix.rickandmorty.R;
import com.gustlogix.rickandmorty.dto.Response;
import com.gustlogix.rickandmorty.dto.episode.AllEpisodeResponse;
import com.gustlogix.rickandmorty.dto.episode.EpisodeResult;
import com.gustlogix.rickandmorty.repo.EpisodeRepository;
import com.gustlogix.rickandmorty.repo.RepositoryCallback;
import com.gustlogix.rickandmorty.view.ResourceProvider;

import java.io.FileNotFoundException;

public class EpisodesPresenterImpl implements EpisodesPresenter {

    private EpisodeRepository episodeRepository;
    private EpisodesView view;
    private boolean isMoreDataAvailable = true;
    private int page = 1;
    private ResourceProvider resourceProvider;

    public EpisodesPresenterImpl(EpisodesView view, EpisodeRepository episodeRepository, ResourceProvider resourceProvider) {
        this.episodeRepository = episodeRepository;
        this.view = view;
        this.resourceProvider = resourceProvider;
    }

    @Override
    public void onInit() {
    }

    @Override
    public void onNewPageOfDataRequested() {
        if (!isMoreDataAvailable) {
            view.showMessage(resourceProvider.getString(R.string.no_more_data_warning));
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
                view.showMessage(resourceProvider.getString(R.string.fetch_problem_error));
                if (e instanceof FileNotFoundException) {
                    view.showMessage(resourceProvider.getString(R.string.no_more_data_warning));
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
