package com.gustlogix.rickandmorty.view.characterdetails;

import com.gustlogix.rickandmorty.dto.character.CharacterResult;

public interface CharacterDetailsPresenter {
    void onInit(CharacterResult characterResult);
}
