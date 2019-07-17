package com.gustlogix.rickandmorty.view.characters;

import com.gustlogix.rickandmorty.dto.character.CharacterResult;

import java.util.List;

public interface CharactersView {
    void showProgress();
    void hideProgress();
    void showCharacters(List<CharacterResult> characters);
    void showMessage(String message);
    void navigateToCharacterDetailView(CharacterResult characterResult);
    void updateCharacter(CharacterResult characterResult);
}
