package com.gustlogix.rickandmorty.repo;

import com.gustlogix.rickandmorty.dto.character.CharacterResult;

import java.util.List;

public interface CharacterRepository {

    void getSingleCharacter(int id, RepositoryCallback<CharacterResult> callback);

    void getMultipleCharacters(List<Integer> ids, RepositoryCallback<List<CharacterResult>> callback);

    void killCharacter(CharacterResult characterResult, RepositoryCallback<CharacterResult> callback);
}
