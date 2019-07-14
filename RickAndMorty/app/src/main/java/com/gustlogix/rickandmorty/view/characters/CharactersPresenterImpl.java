package com.gustlogix.rickandmorty.view.characters;

import android.util.Log;

import com.gustlogix.rickandmorty.dto.Response;
import com.gustlogix.rickandmorty.dto.character.CharacterResult;
import com.gustlogix.rickandmorty.repo.CharacterRepository;
import com.gustlogix.rickandmorty.repo.RepositoryCallback;

import java.util.List;

public class CharactersPresenterImpl implements CharactersPresenter {

    private CharacterRepository characterRepository;
    private CharactersView view;

    CharactersPresenterImpl(CharactersView charactersView, CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
        this.view = charactersView;
    }

    @Override
    public void onInit(List<Integer> characterIds) {
        view.showProgress();

        characterRepository.getMultipleCharacters(characterIds, new RepositoryCallback<List<CharacterResult>>() {
            @Override
            public void onSuccess(Response<List<CharacterResult>> response) {
                view.hideProgress();

                view.showCharacters(response.getResult());
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
