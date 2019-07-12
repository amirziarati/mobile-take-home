package com.gustlogix.rickandmorty.repo.remote.character;

import com.gustlogix.rickandmorty.dto.CharacterResult;
import com.gustlogix.rickandmorty.repo.RepositoryCallback;
import com.gustlogix.rickandmorty.repo.remote.character.deserializer.MultipleCharacterDeserializer;
import com.gustlogix.rickandmorty.repo.remote.character.deserializer.SingleCharacterDeserializer;
import com.gustlogix.rickandmorty.repo.remote.network.NetworkRequest;
import com.gustlogix.rickandmorty.repo.remote.network.NetworkService;
import com.gustlogix.rickandmorty.repo.remote.network.NetworkServiceCallback;
import com.gustlogix.rickandmorty.repo.remote.network.NetworkServiceImpl;

import java.util.List;

public class CharacterRemoteServiceImpl implements CharacterRemoteService {

    SingleCharacterDeserializer singleCharacterDeserializer;
    MultipleCharacterDeserializer multipleCharacterDeserializer;

    public CharacterRemoteServiceImpl(SingleCharacterDeserializer singleCharacterDeserializer,
                                      MultipleCharacterDeserializer multipleCharacterDeserializer)
    {
        this.multipleCharacterDeserializer = multipleCharacterDeserializer;
        this.singleCharacterDeserializer = singleCharacterDeserializer;
    }

    @Override
    public void fetchSingleCharacter(int id, final RepositoryCallback<CharacterResult> callback) {
        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .setUrl("https://rickandmortyapi.com/api/character/" + id)
                .setMethod(NetworkService.GET)
                .build();

        new NetworkServiceImpl(singleCharacterDeserializer).call(networkRequest, new NetworkServiceCallback<CharacterResult>() {

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
    public void fetchMultipleCharacter(List<Integer> characterIds, final RepositoryCallback<List<CharacterResult>> callback) {
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

        new NetworkServiceImpl(multipleCharacterDeserializer).call(networkRequest, new NetworkServiceCallback<List<CharacterResult>>() {

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
