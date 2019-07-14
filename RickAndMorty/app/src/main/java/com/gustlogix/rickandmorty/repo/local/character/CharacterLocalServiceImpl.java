package com.gustlogix.rickandmorty.repo.local.character;

import com.gustlogix.rickandmorty.dto.character.CharacterResult;
import com.gustlogix.rickandmorty.repo.local.LocalRepositoryCallback;
import com.gustlogix.rickandmorty.repo.local.OnLocalDataUpdateListener;
import com.gustlogix.rickandmorty.repo.local.db.DbHelper;
import com.gustlogix.rickandmorty.thread.ApplicationThreadPool;
import com.gustlogix.rickandmorty.thread.Task;
import com.gustlogix.rickandmorty.thread.TaskCallback;

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
    public void updateCharacter(final CharacterResult characterResult) {
        ApplicationThreadPool.execute(new Task<Void>() {
            @Override
            public Void execute() {
                dbHelper.insertCharacter(characterResult);
                return null;
            }
        }, new TaskCallback<Void>() {
            @Override
            public void onResult(Void result) {

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    @Override
    public void updateCharacters(final List<CharacterResult> characterResults, final OnLocalDataUpdateListener onLocalDataUpdateListener) {
        ApplicationThreadPool.execute(new Task<Void>() {

            @Override
            public Void execute() {
                dbHelper.insertCharacters(characterResults);
                return null;
            }
        }, new TaskCallback<Void>() {
            @Override
            public void onResult(Void response) {
                onLocalDataUpdateListener.onUpdateDone();
            }

            @Override
            public void onError(Exception e) {
                onLocalDataUpdateListener.onError(e);
            }
        });
    }
}
