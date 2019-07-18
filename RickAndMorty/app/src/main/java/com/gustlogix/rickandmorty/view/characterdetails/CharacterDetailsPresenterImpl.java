package com.gustlogix.rickandmorty.view.characterdetails;

import com.gustlogix.rickandmorty.dto.character.CharacterResult;

public class CharacterDetailsPresenterImpl implements CharacterDetailsPresenter {

    private CharacterDetailsView view;

    public CharacterDetailsPresenterImpl(CharacterDetailsView view) {
        this.view = view;
    }

    @Override
    public void onInit(CharacterResult characterResult) {
        view.showCharacterDetails(characterResult);
    }
}
