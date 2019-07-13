package com.gustlogix.rickandmorty.view.characters;

import android.util.Log;

import com.gustlogix.rickandmorty.dto.character.CharacterResult;
import com.gustlogix.rickandmorty.repo.RepositoryCallback;
import com.gustlogix.rickandmorty.repo.remote.character.CharacterRemoteService;

import java.util.List;

public class CharactersPresenterImpl implements CharactersPresenter {

    private CharacterRemoteService characterRemoteService;
    private CharactersView view;

    CharactersPresenterImpl(CharactersView charactersView, CharacterRemoteService characterRemoteService) {
        this.characterRemoteService = characterRemoteService;
        this.view = charactersView;
    }

    @Override
    public void onInit(List<Integer> characterIds) {
        view.showProgress();

        characterRemoteService.fetchMultipleCharacter(characterIds, new RepositoryCallback<List<CharacterResult>>() {
            @Override
            public void onSuccess(List<CharacterResult> data) {
                view.hideProgress();

                view.showCharacters(data);
            }

            @Override
            public void onError(Exception e) {
                view.hideProgress();
                view.showMessage("there was a problem fetching data !!!");
                Log.e("R&M", e.toString());
            }
        });
    }

    @Override
    public void onCharacterClicked(CharacterResult characterResult) {
        view.navigateToCharacterDetailView(characterResult);
    }
}
