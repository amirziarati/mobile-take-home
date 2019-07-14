package com.gustlogix.rickandmorty.repo.local.db;

import com.gustlogix.rickandmorty.dto.character.AllCharacterResponse;
import com.gustlogix.rickandmorty.dto.character.CharacterResult;
import com.gustlogix.rickandmorty.dto.episode.AllEpisodeResponse;
import com.gustlogix.rickandmorty.dto.episode.EpisodeResult;
import com.gustlogix.rickandmorty.repo.local.downloadcache.FileCacheEntry;

import java.util.List;

public interface DbHelper {

    CharacterResult fetchSingleCharacter(int id);

    List<CharacterResult> fetchMultipleCharacter(List<Integer> characterIds);

    AllCharacterResponse fetchAllCharacters(int page, int pageSize);

    EpisodeResult fetchSingleEpisode(int id);

    List<EpisodeResult> fetchMultipleEpisode(List<Integer> characterIds);

    AllEpisodeResponse fetchAllEpisodes(int page, int pageSize);

    long insertCharacter(CharacterResult characterResult);

    void insertCharacters(List<CharacterResult> characterResults);

    long insertEpisode(EpisodeResult episodeResult);

    void insertEpisodes(List<EpisodeResult> episodeResults);

    long insertFileCache(FileCacheEntry fileCacheEntry);

    long removeFileCache(FileCacheEntry fileCacheEntry);

    FileCacheEntry fetchFileCache(String url);

    List<FileCacheEntry> fetchFileCaches();
}
