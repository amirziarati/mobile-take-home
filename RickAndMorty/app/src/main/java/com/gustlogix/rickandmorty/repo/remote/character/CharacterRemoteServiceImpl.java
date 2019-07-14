package com.gustlogix.rickandmorty.repo.remote.character;

import com.gustlogix.rickandmorty.dto.character.CharacterResult;
import com.gustlogix.rickandmorty.repo.remote.RemoteRepositoryCallback;
import com.gustlogix.rickandmorty.repo.remote.network.NetworkRequest;
import com.gustlogix.rickandmorty.repo.remote.network.NetworkService;
import com.gustlogix.rickandmorty.repo.remote.network.NetworkServiceCallback;

import java.util.List;

public class CharacterRemoteServiceImpl implements CharacterRemoteService {

    private NetworkService<CharacterResult> singleNetworkService;
    private NetworkService<List<CharacterResult>> multipleNetworkService;

    public CharacterRemoteServiceImpl(NetworkService<CharacterResult> singleNetworkService, NetworkService<List<CharacterResult>> multipleNetworkService) {
        this.singleNetworkService = singleNetworkService;
        this.multipleNetworkService = multipleNetworkService;
    }

    @Override
    public void fetchSingleCharacter(int id, final RemoteRepositoryCallback<CharacterResult> callback) {
        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .setUrl("https://rickandmortyapi.com/api/character/" + id)
                .setMethod(NetworkService.GET)
                .build();

        singleNetworkService.call(networkRequest, new NetworkServiceCallback<CharacterResult>() {

            @Override
            public void onResponse(CharacterResult response) {
                callback.onSuccess(response);
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
            }
        });
    }

    @Override
    public void fetchMultipleCharacter(List<Integer> characterIds, final RemoteRepositoryCallback<List<CharacterResult>> callback) {
        StringBuilder sb = new StringBuilder();
        sb.append("https://rickandmortyapi.com/api/character/");
        for (int id : characterIds) {
            sb.append(id);
            sb.append(",");
        }
        String url = sb.toString();
        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .setUrl(url)
                .setMethod(NetworkService.GET)
                .build();

        multipleNetworkService.call(networkRequest, new NetworkServiceCallback<List<CharacterResult>>() {

            @Override
            public void onResponse(List<CharacterResult> response) {
                callback.onSuccess(response);
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
            }
        });
    }
}
