package com.gustlogix.rickandmorty.repo.local.db;

import com.gustlogix.rickandmorty.dto.character.AllCharacterResponse;
import com.gustlogix.rickandmorty.dto.character.CharacterResult;
import com.gustlogix.rickandmorty.dto.episode.AllEpisodeResponse;
import com.gustlogix.rickandmorty.dto.episode.EpisodeResult;
import com.gustlogix.rickandmorty.repo.local.downloadcache.FileCacheEntry;

import java.util.List;

public interface DbHelper {

    void deleteAllCharacters();

    void deleteAllEpisodes();

    void deleteAllCacheEntries();

    CharacterResult fetchSingleCharacter(int id);

    List<CharacterResult> fetchMultipleCharacter(List<Integer> characterIds);

    AllCharacterResponse fetchAllCharacters(int page, int pageSize);

    EpisodeResult fetchSingleEpisode(int id);

    List<EpisodeResult> fetchMultipleEpisode(List<Integer> characterIds);

    AllEpisodeResponse fetchAllEpisodes(int page, int pageSize);

    long insertOrUpdateCharacter(CharacterResult characterResult);

    void insertOrUpdateCharacters(List<CharacterResult> characterResults);

    long insertOrUpdateEpisode(EpisodeResult episodeResult);

    void insertOrUpdateEpisodes(List<EpisodeResult> episodeResults);

    long insertOrUpdateFileCache(FileCacheEntry fileCacheEntry);

    long removeFileCache(FileCacheEntry fileCacheEntry);

    FileCacheEntry fetchFileCache(String url);

    List<FileCacheEntry> fetchFileCaches();
}
