package com.gustlogix.rickandmorty.view.characters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.gustlogix.rickandmorty.R;
import com.gustlogix.rickandmorty.dto.character.CharacterResult;
import com.gustlogix.rickandmorty.repo.remote.character.CharacterRemoteServiceImpl;
import com.gustlogix.rickandmorty.repo.remote.character.deserializer.MultipleCharacterDeserializerImpl;
import com.gustlogix.rickandmorty.repo.remote.character.deserializer.SingleCharacterDeserializerImpl;
import com.gustlogix.rickandmorty.repo.remote.network.NetworkServiceImpl;
import com.gustlogix.rickandmorty.view.characterdetails.CharacterDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class CharactersActivity extends Activity implements CharactersView {

    public static final String IDS_LIST_ARG = "character_ids";
    LinearLayout llProgressBar;
    ListView lstCharacters;

    CharactersPresenter presenter = new CharactersPresenterImpl(this, new CharacterRemoteServiceImpl(new NetworkServiceImpl<CharacterResult>(new SingleCharacterDeserializerImpl()), new NetworkServiceImpl<List<CharacterResult>>(new MultipleCharacterDeserializerImpl(new SingleCharacterDeserializerImpl()))));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_characters);

        ArrayList<Integer> characterIds = getIntent().getIntegerArrayListExtra(IDS_LIST_ARG);

        llProgressBar = findViewById(R.id.llProgressbar);
        lstCharacters = findViewById(R.id.lstCharacters);
        presenter.onInit(characterIds);
    }

    @Override
    public void showProgress() {
        llProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        llProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showCharacters(List<CharacterResult> characters) {
        lstCharacters.setAdapter(new CharactersAdapter(CharactersActivity.this, characters, new CharactersAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(CharacterResult characterResult) {
                presenter.onCharacterClicked(characterResult);
            }
        }));
    }

    @Override
    public void navigateToCharacterDetailView(CharacterResult characterResult) {
        Intent intent = new Intent(CharactersActivity.this, CharacterDetailActivity.class);
        intent.putExtra(CharacterDetailActivity.CHARACTER_DETAILS_ARG, characterResult);
        startActivity(intent);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(CharactersActivity.this, message, Toast.LENGTH_LONG).show();
        Log.i("R&M", message);
    }
}
