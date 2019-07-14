package com.gustlogix.rickandmorty.repo.remote.character;

import com.gustlogix.rickandmorty.dto.character.CharacterResult;
import com.gustlogix.rickandmorty.repo.remote.RemoteRepositoryCallback;

import java.util.List;

public interface CharacterRemoteService {

    void fetchSingleCharacter(int id, RemoteRepositoryCallback<CharacterResult> callback);

    void fetchMultipleCharacter(List<Integer> characterIds, RemoteRepositoryCallback<List<CharacterResult>> callback);
}
