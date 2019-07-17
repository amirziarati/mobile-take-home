package com.gustlogix.rickandmorty.repo.local.episode;

import com.gustlogix.rickandmorty.dto.episode.AllEpisodeResponse;
import com.gustlogix.rickandmorty.dto.episode.EpisodeResult;
import com.gustlogix.rickandmorty.repo.local.LocalRepositoryCallback;
import com.gustlogix.rickandmorty.repo.local.OnLocalDataUpdateCallback;
import com.gustlogix.rickandmorty.repo.local.db.DbHelper;
import com.gustlogix.rickandmorty.thread.ApplicationThreadPool;
import com.gustlogix.rickandmorty.thread.Task;
import com.gustlogix.rickandmorty.thread.TaskCallback;

import java.util.List;

public class EpisodeLocalServiceImpl implements EpisodeLocalService {

    private DbHelper dbHelper;

    public EpisodeLocalServiceImpl(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public void fetchSingleEpisode(final int id, final LocalRepositoryCallback<EpisodeResult> callback) {
        ApplicationThreadPool.execute(new Task<EpisodeResult>() {
            @Override
            public EpisodeResult execute() {
                return dbHelper.fetchSingleEpisode(id);
            }
        }, new TaskCallback<EpisodeResult>() {
            @Override
            public void onResult(EpisodeResult response) {
                callback.onSuccess(response);
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
            }
        });
    }

    @Override
    public void fetchMultipleEpisodes(final List<Integer> episodeIds, final LocalRepositoryCallback<List<EpisodeResult>> callback) {
        ApplicationThreadPool.execute(new Task<List<EpisodeResult>>() {
            @Override
            public List<EpisodeResult> execute() {
                return dbHelper.fetchMultipleEpisode(episodeIds);
            }
        }, new TaskCallback<List<EpisodeResult>>() {
            @Override
            public void onResult(List<EpisodeResult> response) {
                callback.onSuccess(response);
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
            }
        });
    }

    @Override
    public void fetchAllEpisodes(final int page, final int pageSize, final LocalRepositoryCallback<AllEpisodeResponse> callback) {
        ApplicationThreadPool.execute(new Task<AllEpisodeResponse>() {
            @Override
            public AllEpisodeResponse execute() {
                return dbHelper.fetchAllEpisodes(page, pageSize);
            }
        }, new TaskCallback<AllEpisodeResponse>() {
            @Override
            public void onResult(AllEpisodeResponse response) {
                callback.onSuccess(response);
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
            }
        });

    }

    @Override
    public void insertOrUpdateEpisode(final EpisodeResult data, OnLocalDataUpdateCallback onLocalDataUpdateCallback) {
        ApplicationThreadPool.execute(new Task<Void>() {
            @Override
            public Void execute() {
                dbHelper.insertOrUpdateEpisode(data);
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
    public void insertOrUpdateEpisodes(final List<EpisodeResult> episodes, final OnLocalDataUpdateCallback onLocalDataUpdateCallback) {
        ApplicationThreadPool.execute(new Task<Void>() {
            @Override
            public Void execute() {
                dbHelper.insertOrUpdateEpisodes(episodes);
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
