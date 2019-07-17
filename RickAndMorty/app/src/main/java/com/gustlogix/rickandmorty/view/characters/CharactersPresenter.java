package com.gustlogix.rickandmorty.view.characters;

import com.gustlogix.rickandmorty.dto.character.CharacterResult;

import java.util.List;

public interface CharactersPresenter {
    void onInit(List<Integer> characterIds);
    void onCharacterClicked(CharacterResult characterResult);
    void onKillCharacterClicked(CharacterResult characterResult);
}
