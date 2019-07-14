package com.gustlogix.rickandmorty.view.characterdetails;

import com.gustlogix.rickandmorty.dto.character.CharacterResult;

public interface CharacterDetailsView {
    void showProgress();

    void hideProgress();

    void showMessage(String message);

    void showCharacterDetails(CharacterResult characterResult);
}
