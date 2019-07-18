package com.gustlogix.rickandmorty.view.characters;

import android.util.Log;

import com.gustlogix.rickandmorty.R;
import com.gustlogix.rickandmorty.dto.Response;
import com.gustlogix.rickandmorty.dto.character.CharacterResult;
import com.gustlogix.rickandmorty.repo.CharacterRepository;
import com.gustlogix.rickandmorty.repo.RepositoryCallback;
import com.gustlogix.rickandmorty.view.ResourceProvider;

import java.util.List;

public class CharactersPresenterImpl implements CharactersPresenter {

    private CharacterRepository characterRepository;
    private CharactersView view;
    private ResourceProvider resourceProvider;

    public CharactersPresenterImpl(CharactersView view, CharacterRepository characterRepository, ResourceProvider resourceProvider) {
        this.characterRepository = characterRepository;
        this.view = view;
        this.resourceProvider = resourceProvider;
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
                view.showMessage(resourceProvider.getString(R.string.fetch_problem_error));
                Log.e("R&M", e.toString());
            }
        });
    }

    @Override
    public void onCharacterClicked(CharacterResult characterResult) {
        view.navigateToCharacterDetailView(characterResult);
    }

    @Override
    public void onKillCharacterClicked(CharacterResult characterResult) {
        characterRepository.killCharacter(characterResult, new RepositoryCallback<CharacterResult>() {
            @Override
            public void onSuccess(Response<CharacterResult> response) {
                view.updateCharacter(response.getResult());
            }

            @Override
            public void onError(Exception e) {
                view.showMessage(resourceProvider.getString(R.string.character_kill_error));
                Log.e("R&M", "error killing the character : " + e.getMessage());
            }
        });
    }
}
