package com.gustlogix.rickandmorty.repo.local.character;

import com.gustlogix.rickandmorty.dto.character.CharacterResult;
import com.gustlogix.rickandmorty.repo.local.LocalRepositoryCallback;
import com.gustlogix.rickandmorty.repo.local.OnLocalDataUpdateListener;

import java.util.List;

public interface CharacterLocalService {

    void fetchSingleCharacter(int id, LocalRepositoryCallback<CharacterResult> callback);

    void fetchMultipleCharacter(List<Integer> characterIds, LocalRepositoryCallback<List<CharacterResult>> callback);

    void updateCharacter(CharacterResult data);

    void updateCharacters(List<CharacterResult> data, OnLocalDataUpdateListener onLocalDataUpdateListener);
}
