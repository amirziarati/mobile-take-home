package com.gustlogix.rickandmorty.repo.remote.character;

import com.gustlogix.rickandmorty.dto.character.CharacterResult;
import com.gustlogix.rickandmorty.repo.RepositoryCallback;

import java.util.List;

public interface CharacterRemoteService {

    void fetchSingleCharacter(int id, RepositoryCallback<CharacterResult> callback);

    void fetchMultipleCharacter(List<Integer> characterIds, RepositoryCallback<List<CharacterResult>> callback);
}
