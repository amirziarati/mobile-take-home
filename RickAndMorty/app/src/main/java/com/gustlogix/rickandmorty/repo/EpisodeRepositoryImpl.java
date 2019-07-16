package com.gustlogix.rickandmorty.repo;

import com.gustlogix.rickandmorty.dto.Response;
import com.gustlogix.rickandmorty.dto.episode.AllEpisodeResponse;
import com.gustlogix.rickandmorty.dto.episode.EpisodeResult;
import com.gustlogix.rickandmorty.repo.local.LocalRepositoryCallback;
import com.gustlogix.rickandmorty.repo.local.OnLocalDataUpdateCallback;
import com.gustlogix.rickandmorty.repo.local.episode.EpisodeLocalService;
import com.gustlogix.rickandmorty.repo.remote.RemoteRepositoryCallback;
import com.gustlogix.rickandmorty.repo.remote.episode.EpisodeRemoteService;

import java.util.List;

public class EpisodeRepositoryImpl implements EpisodeRepository {

    private EpisodeRemoteService remoteService;
    private EpisodeLocalService localService;

    public EpisodeRepositoryImpl(EpisodeRemoteService remoteService, EpisodeLocalService localService) {
        this.remoteService = remoteService;
        this.localService = localService;
    }

    @Override
    public void getSingleEpisode(final int id, final RepositoryCallback<EpisodeResult> callback) {
        remoteService.fetchSingleEpisode(id, new RemoteRepositoryCallback<EpisodeResult>() {
            @Override
            public void onSuccess(EpisodeResult episode) {
                localService.updateEpisode(episode);
                Response response = new Response();
                response.setOnline(true);
                response.setResult(episode);
                callback.onSuccess(response);
            }

            @Override
            public void onError(Exception e) {
                localService.fetchSingleEpisode(id, new LocalRepositoryCallback<EpisodeResult>() {
                    @Override
                    public void onSuccess(EpisodeResult data) {
                        Response response = new Response();
                        response.setOnline(false);
                        response.setResult(data);
                        callback.onSuccess(response);
                    }

                    @Override
                    public void onError(Exception e) {
                        callback.onError(e);
                    }
                });
            }
        });
    }

    @Override
    public void getMultipleEpisodes(final List<Integer> ids, final RepositoryCallback<List<EpisodeResult>> callback) {
        remoteService.fetchMultipleEpisodes(ids, new RemoteRepositoryCallback<List<EpisodeResult>>() {
            @Override
            public void onSuccess(final List<EpisodeResult> data) {
                localService.updateEpisodes(data, new OnLocalDataUpdateCallback() {
                    @Override
                    public void onUpdateDone() {
                        Response response = new Response();
                        response.setOnline(true);
                        response.setResult(data);
                        callback.onSuccess(response);
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                        Response response = new Response();
                        response.setOnline(true);
                        response.setResult(data);
                        callback.onSuccess(response);
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                localService.fetchMultipleEpisodes(ids, new LocalRepositoryCallback<List<EpisodeResult>>() {
                    @Override
                    public void onSuccess(List<EpisodeResult> data) {
                        Response response = new Response();
                        response.setOnline(false);
                        response.setResult(data);
                        callback.onSuccess(response);
                    }

                    @Override
                    public void onError(Exception e) {
                        callback.onError(e);
                    }
                });
            }
        });
    }

    @Override
    public void getAllEpisodes(final int page, final RepositoryCallback<AllEpisodeResponse> callback) {
        remoteService.fetchAllEpisodes(page, new RemoteRepositoryCallback<AllEpisodeResponse>() {
            @Override
            public void onSuccess(final AllEpisodeResponse data) {
                localService.updateEpisodes(data.getResults(), new OnLocalDataUpdateCallback() {
                    @Override
                    public void onUpdateDone() {
                        Response response = new Response();
                        response.setOnline(true);
                        response.setResult(data);
                        callback.onSuccess(response);
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                        Response response = new Response();
                        response.setOnline(true);
                        response.setResult(data);
                        callback.onSuccess(response);
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                localService.fetchAllEpisodes(page, 20, new LocalRepositoryCallback<AllEpisodeResponse>() {
                    @Override
                    public void onSuccess(AllEpisodeResponse data) {
                        Response response = new Response();
                        response.setOnline(false);
                        response.setResult(data);
                        callback.onSuccess(response);
                    }

                    @Override
                    public void onError(Exception e) {
                        callback.onError(e);
                    }
                });
            }
        });
    }
}
