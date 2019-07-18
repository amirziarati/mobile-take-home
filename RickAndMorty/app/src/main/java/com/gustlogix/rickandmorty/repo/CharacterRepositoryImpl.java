package com.gustlogix.rickandmorty.repo;

import android.util.Log;

import com.gustlogix.rickandmorty.dto.Response;
import com.gustlogix.rickandmorty.dto.character.CharacterResult;
import com.gustlogix.rickandmorty.repo.local.LocalRepositoryCallback;
import com.gustlogix.rickandmorty.repo.local.OnLocalDataUpdateCallback;
import com.gustlogix.rickandmorty.repo.local.character.CharacterLocalService;
import com.gustlogix.rickandmorty.repo.remote.RemoteRepositoryCallback;
import com.gustlogix.rickandmorty.repo.remote.character.CharacterRemoteService;

import java.util.ArrayList;
import java.util.HashMap;
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
            public void onSuccess(CharacterResult character) {

                updateKilledByUserStatusAndReturn(character);

                syncLocalData(character);
            }

            @Override
            public void onError(Exception e) {
                characterLocalService.fetchSingleCharacter(id, new LocalRepositoryCallback<CharacterResult>() {
                    @Override
                    public void onSuccess(CharacterResult data) {
                        Response<CharacterResult> response = new Response<CharacterResult>();
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

            private void syncLocalData(CharacterResult character) {
                characterLocalService.insertOrUpdateCharacter(character, new OnLocalDataUpdateCallback() {
                    @Override
                    public void onUpdateDone() {
                        Log.i("R&M", "character from server data was cached to database successfully");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.i("R&M", "error while caching character to database : " + e.toString());
                    }
                });
            }

            private void updateKilledByUserStatusAndReturn(final CharacterResult character) {
                final ArrayList<Integer> characterIds = new ArrayList<>();
                characterIds.add(character.getId());
                characterLocalService.fetchKilledByUserCharactersStatusByIds(characterIds, new LocalRepositoryCallback<HashMap<Integer, Boolean>>() {
                    @Override
                    public void onSuccess(HashMap<Integer, Boolean> mapCharacterKilledStatus) {
                        character.setIsKilledByUser(mapCharacterKilledStatus.get(character.getId()));
                        Response<CharacterResult> response = new Response<CharacterResult>();
                        response.setOnline(true);
                        response.setResult(character);
                        callback.onSuccess(response);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.i("R&M", "could not load killedByUserStatus from DB:" + e.getMessage());
                    }
                });
            }
        });
    }

    @Override
    public void getMultipleCharacters(final List<Integer> ids,
                                      final RepositoryCallback<List<CharacterResult>> callback) {
        characterRemoteService.fetchMultipleCharacter(ids, new RemoteRepositoryCallback<List<CharacterResult>>() {
            @Override
            public void onSuccess(final List<CharacterResult> characterResults) {

                updateKilledByUserStatusAndReturn(characterResults, ids);

                syncLocalData(characterResults);
            }

            @Override
            public void onError(Exception e) {
                characterLocalService.fetchMultipleCharacter(ids, new LocalRepositoryCallback<List<CharacterResult>>() {
                    @Override
                    public void onSuccess(List<CharacterResult> data) {
                        Response<List<CharacterResult>> response = new Response<List<CharacterResult>>();
                        response.setOnline(false);
                        if (data.size() == ids.size()) {//if we dont have the complete list of characters offline dont return any of them
                            response.setResult(data);
                        } else {
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

            private void syncLocalData(List<CharacterResult> characterResults) {
                characterLocalService.insertOrUpdateCharacters(characterResults, new OnLocalDataUpdateCallback() {
                    @Override
                    public void onUpdateDone() {
                        Log.i("R&M", "characters server data was cached to database successfully");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.i("R&M", "error while caching characters to database : " + e.toString());
                    }
                });
            }

            private void updateKilledByUserStatusAndReturn(final List<CharacterResult> characterResults, final List<Integer> characterIds) {
                characterLocalService.fetchKilledByUserCharactersStatusByIds(characterIds, new LocalRepositoryCallback<HashMap<Integer, Boolean>>() {
                    @Override
                    public void onSuccess(HashMap<Integer, Boolean> mapCharacterKilledStatus) {
                        for (CharacterResult character : characterResults) {
                            character.setIsKilledByUser(mapCharacterKilledStatus.get(character.getId()));
                        }

                        Response<List<CharacterResult>> response = new Response<List<CharacterResult>>();
                        response.setOnline(true);
                        response.setResult(characterResults);
                        callback.onSuccess(response);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.i("R&M", "could not load killedByUserStatus from DB:" + e.getMessage());
                    }
                });
            }
        });
    }

    public void killCharacter(final CharacterResult characterResult,
                              final RepositoryCallback<CharacterResult> callback) {
        characterResult.setIsKilledByUser(true);
        characterLocalService.insertOrUpdateCharacter(characterResult, new OnLocalDataUpdateCallback() {
            @Override
            public void onUpdateDone() {
                Response<CharacterResult> resultResponse = new Response<CharacterResult>();
                resultResponse.setResult(characterResult);
                callback.onSuccess(resultResponse);
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
            }
        });
    }
}
