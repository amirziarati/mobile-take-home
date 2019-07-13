package com.gustlogix.rickandmorty.view.episode;

import android.util.Log;

import com.gustlogix.rickandmorty.dto.episode.AllEpisodeResponse;
import com.gustlogix.rickandmorty.dto.episode.EpisodeResult;
import com.gustlogix.rickandmorty.repo.RepositoryCallback;
import com.gustlogix.rickandmorty.repo.remote.episode.EpisodeRemoteService;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class EpisodesPresenterImpl implements EpisodesPresenter {

    private EpisodeRemoteService episodeRemoteService;
    private EpisodesView view;
    private boolean isMoreDataAvailable = true;
    private int page = 1;

    EpisodesPresenterImpl(EpisodesView episodesView, EpisodeRemoteService episodeRemoteService) {
        this.episodeRemoteService = episodeRemoteService;
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

        episodeRemoteService.fetchAllEpisodes(page, new RepositoryCallback<AllEpisodeResponse>() {
            @Override
            public void onSuccess(AllEpisodeResponse data) {
                isMoreDataAvailable = data.getInfo().getNext() != null && !data.getInfo().getNext().trim().equals("");
                page++;
                view.hideProgress();
                view.addNewEpisodesToView(data.getResults());
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
        view.navigateToCharactersView(getIdsFromUrls(episodeResult.getCharacters()));
    }

    private ArrayList<Integer> getIdsFromUrls(List<String> characters) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (String url : characters) {
            if (url.endsWith("/")) {
                url = url.substring(0, url.length() - 1);
            }
            String stringID = url.substring(url.lastIndexOf('/') + 1);
            ids.add(Integer.parseInt(stringID));
        }
        return ids;
    }
}
