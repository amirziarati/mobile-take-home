package com.gustlogix.rickandmorty.repo.local.character;

import com.gustlogix.rickandmorty.dto.character.CharacterResult;
import com.gustlogix.rickandmorty.repo.local.LocalRepositoryCallback;
import com.gustlogix.rickandmorty.repo.local.OnLocalDataUpdateCallback;

import java.util.HashMap;
import java.util.List;

public interface CharacterLocalService {

    void fetchSingleCharacter(int id, LocalRepositoryCallback<CharacterResult> callback);

    void fetchMultipleCharacter(List<Integer> characterIds, LocalRepositoryCallback<List<CharacterResult>> callback);

    void insertOrUpdateCharacter(CharacterResult data, final OnLocalDataUpdateCallback onLocalDataUpdateCallback);

    void insertOrUpdateCharacters(List<CharacterResult> data, OnLocalDataUpdateCallback onLocalDataUpdateCallback);

    void fetchKilledByUserCharactersStatusByIds(List<Integer> charactersIds, LocalRepositoryCallback<HashMap<Integer, Boolean>> localRepositoryCallback);
}
