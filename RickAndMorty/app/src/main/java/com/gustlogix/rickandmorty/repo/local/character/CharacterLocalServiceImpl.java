package com.gustlogix.rickandmorty.repo.local.character;

import com.gustlogix.rickandmorty.dto.character.CharacterResult;
import com.gustlogix.rickandmorty.repo.local.LocalRepositoryCallback;
import com.gustlogix.rickandmorty.repo.local.OnLocalDataUpdateCallback;
import com.gustlogix.rickandmorty.repo.local.db.DbHelper;
import com.gustlogix.rickandmorty.thread.ApplicationThreadPool;
import com.gustlogix.rickandmorty.thread.Task;
import com.gustlogix.rickandmorty.thread.TaskCallback;

import java.util.HashMap;
import java.util.List;

public class CharacterLocalServiceImpl implements CharacterLocalService {

    private DbHelper dbHelper;

    public CharacterLocalServiceImpl(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public void fetchSingleCharacter(final int id, final LocalRepositoryCallback<CharacterResult> callback) {
        ApplicationThreadPool.execute(new Task<CharacterResult>() {
            @Override
            public CharacterResult execute() {
                return dbHelper.fetchSingleCharacter(id);
            }
        }, new TaskCallback<CharacterResult>() {
            @Override
            public void onResult(CharacterResult response) {
                callback.onSuccess(response);
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
            }
        });
    }

    @Override
    public void fetchMultipleCharacter(final List<Integer> characterIds, final LocalRepositoryCallback<List<CharacterResult>> callback) {

        ApplicationThreadPool.execute(new Task<List<CharacterResult>>() {
            @Override
            public List<CharacterResult> execute() {
                return dbHelper.fetchMultipleCharacter(characterIds);
            }
        }, new TaskCallback<List<CharacterResult>>() {
            @Override
            public void onResult(List<CharacterResult> response) {
                callback.onSuccess(response);
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
            }
        });
    }

    @Override
    public void fetchKilledByUserCharactersStatusByIds(final List<Integer> charactersIds, final LocalRepositoryCallback<HashMap<Integer, Boolean>> callback) {
        ApplicationThreadPool.execute(new Task<HashMap<Integer, Boolean>>() {
            @Override
            public HashMap<Integer, Boolean> execute() {
                List<CharacterResult> characterResults = dbHelper.fetchMultipleCharacter(charactersIds);
                HashMap<Integer, Boolean> mapKilledByUserStatus = new HashMap<>();
                for (CharacterResult character : characterResults) {
                    mapKilledByUserStatus.put(character.getId(), character.getIsKilledByUser());
                }

                return mapKilledByUserStatus;
            }
        }, new TaskCallback<HashMap<Integer, Boolean>>() {
            @Override
            public void onResult(HashMap<Integer, Boolean> response) {
                callback.onSuccess(response);
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
            }
        });
    }

    @Override
    public void insertOrUpdateCharacter(final CharacterResult characterResult, final OnLocalDataUpdateCallback onLocalDataUpdateCallback) {
        ApplicationThreadPool.execute(new Task<Void>() {
            @Override
            public Void execute() {
                dbHelper.insertOrUpdateCharacter(characterResult);
                return null;
            }
        }, new TaskCallback<Void>() {
            @Override
            public void onResult(Void result) {
                onLocalDataUpdateCallback.onUpdateDone();
            }

            @Override
            public void onError(Exception e) {
                onLocalDataUpdateCallback.onError(e);
            }
        });
    }

    @Override
    public void insertOrUpdateCharacters(final List<CharacterResult> characterResults, final OnLocalDataUpdateCallback onLocalDataUpdateCallback) {
        ApplicationThreadPool.execute(new Task<Void>() {

            @Override
            public Void execute() {
                dbHelper.insertOrUpdateCharacters(characterResults);
                return null;
            }
        }, new TaskCallback<Void>() {
            @Override
            public void onResult(Void response) {
                onLocalDataUpdateCallback.onUpdateDone();
            }

            @Override
            public void onError(Exception e) {
                onLocalDataUpdateCallback.onError(e);
            }
        });
    }
}
