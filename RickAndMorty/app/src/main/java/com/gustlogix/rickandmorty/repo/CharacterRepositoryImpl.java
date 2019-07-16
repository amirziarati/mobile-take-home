package com.gustlogix.rickandmorty.repo;

import com.gustlogix.rickandmorty.dto.Response;
import com.gustlogix.rickandmorty.dto.character.CharacterResult;
import com.gustlogix.rickandmorty.repo.local.LocalRepositoryCallback;
import com.gustlogix.rickandmorty.repo.local.OnLocalDataUpdateCallback;
import com.gustlogix.rickandmorty.repo.local.character.CharacterLocalService;
import com.gustlogix.rickandmorty.repo.remote.RemoteRepositoryCallback;
import com.gustlogix.rickandmorty.repo.remote.character.CharacterRemoteService;

import java.util.ArrayList;
import java.util.List;

public class CharacterRepositoryImpl implements CharacterRepository {

    private CharacterRemoteService characterRemoteService;
    private CharacterLocalService characterLocalService;

    public CharacterRepositoryImpl(CharacterRemoteService characterRemoteService, CharacterLocalService characterLocalService) {
        this.characterRemoteService = characterRemoteService;
        this.characterLocalService = characterLocalService;
    }

    @Override
    public void getSingleCharacter(final int id, final RepositoryCallback<CharacterResult> callback) {
        characterRemoteService.fetchSingleCharacter(id, new RemoteRepositoryCallback<CharacterResult>() {
            @Override
            public void onSuccess(CharacterResult data) {
                characterLocalService.updateCharacter(data);
                Response response = new Response();
                response.setOnline(true);
                response.setResult(data);
                callback.onSuccess(response);
            }

            @Override
            public void onError(Exception e) {
                characterLocalService.fetchSingleCharacter(id, new LocalRepositoryCallback<CharacterResult>() {
                    @Override
                    public void onSuccess(CharacterResult data) {
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
    public void getMultipleCharacters(final List<Integer> ids, final RepositoryCallback<List<CharacterResult>> callback) {
        characterRemoteService.fetchMultipleCharacter(ids, new RemoteRepositoryCallback<List<CharacterResult>>() {
            @Override
            public void onSuccess(final List<CharacterResult> data) {
                characterLocalService.updateCharacters(data, new OnLocalDataUpdateCallback() {
                    @Override
                    public void onUpdateDone() {
                        Response response = new Response();
                        response.setOnline(true);
                        response.setResult(data);
                        callback.onSuccess(response);
                    }

                    @Override
                    public void onError(Exception e) {
                        Response response = new Response();
                        response.setOnline(true);
                        response.setResult(data);
                        callback.onSuccess(response);
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                characterLocalService.fetchMultipleCharacter(ids, new LocalRepositoryCallback<List<CharacterResult>>() {
                    @Override
                    public void onSuccess(List<CharacterResult> data) {
                        Response response = new Response();
                        response.setOnline(false);
                        if(data.size() == ids.size()) {//if we dont have the complete list of haracters offline dont return any of them
                            response.setResult(data);
                        }
                        else
                        {
                            response.setResult(new ArrayList<CharacterResult>());
                        }
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
