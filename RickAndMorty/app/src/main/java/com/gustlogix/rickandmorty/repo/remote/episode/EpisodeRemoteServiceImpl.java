package com.gustlogix.rickandmorty.repo.remote.episode;

import com.gustlogix.rickandmorty.dto.episode.AllEpisodeResponse;
import com.gustlogix.rickandmorty.dto.episode.EpisodeResult;
import com.gustlogix.rickandmorty.repo.RepositoryCallback;
import com.gustlogix.rickandmorty.repo.remote.network.NetworkRequest;
import com.gustlogix.rickandmorty.repo.remote.network.NetworkService;
import com.gustlogix.rickandmorty.repo.remote.network.NetworkServiceCallback;

import java.util.List;

public class EpisodeRemoteServiceImpl implements EpisodeRemoteService {

    private NetworkService<EpisodeResult> singleNetworkService;
    private NetworkService<List<EpisodeResult>> multipleNetworkService;
    private NetworkService<AllEpisodeResponse> allNetworkService;

    public EpisodeRemoteServiceImpl(NetworkService<EpisodeResult> singleNetworkService,
                                    NetworkService<List<EpisodeResult>> multipleNetworkService,
                                    NetworkService<AllEpisodeResponse> allNetworkService) {
        this.singleNetworkService = singleNetworkService;
        this.multipleNetworkService = multipleNetworkService;
        this.allNetworkService = allNetworkService;
    }

    @Override
    public void fetchSingleEpisode(int id, final RepositoryCallback<EpisodeResult> callback) {
        NetworkRequest request = new NetworkRequest
                .Builder()
                .setUrl("https://rickandmortyapi.com/api/episode/" + id)
                .setMethod(NetworkService.GET)
                .build();
        singleNetworkService.call(request, new NetworkServiceCallback<EpisodeResult>() {
            @Override
            public void onResponse(EpisodeResult response) {
                callback.onSuccess(response);
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
            }
        });
    }

    @Override
    public void fetchMultipleEpisodes(List<Integer> episodeIds, final RepositoryCallback<List<EpisodeResult>> callback) {
        StringBuilder sb = new StringBuilder();
        sb.append("https://rickandmortyapi.com/api/episode/");
        for (int id : episodeIds) {
            sb.append(id);
            sb.append(",");
        }
        String url = sb.toString();
        NetworkRequest request = new NetworkRequest
                .Builder()
                .setUrl(url)
                .setMethod(NetworkService.GET)
                .build();
        multipleNetworkService.call(request, new NetworkServiceCallback<List<EpisodeResult>>() {
            @Override
            public void onResponse(List<EpisodeResult> response) {
                callback.onSuccess(response);
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
            }
        });
    }

    @Override
    public void fetchAllEpisodes(int page, final RepositoryCallback<AllEpisodeResponse> callback) {
        NetworkRequest request = new NetworkRequest
                .Builder()
                .setUrl("https://rickandmortyapi.com/api/episode/")
                .setQueryParameters(new NetworkRequest.QueryParameter("page", "" + page))
                .setMethod(NetworkService.GET)
                .build();
        allNetworkService.call(request, new NetworkServiceCallback<AllEpisodeResponse>() {
            @Override
            public void onResponse(AllEpisodeResponse response) {
                callback.onSuccess(response);
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
            }
        });
    }
}
