package com.gustlogix.rickandmorty.view.characterdetails;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gustlogix.rickandmorty.R;
import com.gustlogix.rickandmorty.SimpleServiceLocator;
import com.gustlogix.rickandmorty.dto.character.CharacterResult;
import com.gustlogix.rickandmorty.repo.remote.imagedownloader.ImageDownloader;

public class CharacterDetailActivity extends Activity implements CharacterDetailsView {

    public static final String CHARACTER_DETAILS_ARG = "character_details";
    ImageView imgCharacter;
    ImageView imgDeadStamp;
    TextView tvName;
    TextView tvSpecies;
    TextView tvStatus;
    TextView tvGender;
    TextView tvLocation;

    ImageDownloader imageDownloader;
    CharacterDetailsPresenter presenter = SimpleServiceLocator.getInstance().getCharacterDetailsPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_details);


        imgCharacter = findViewById(R.id.imgCharacter);
        imgDeadStamp = findViewById(R.id.imgDeadStamp);
        tvName = findViewById(R.id.tvName);
        tvSpecies = findViewById(R.id.tvSpecies);
        tvStatus = findViewById(R.id.tvStatus);
        tvLocation = findViewById(R.id.tvLocation);
        tvGender = findViewById(R.id.tvGender);

        imageDownloader = SimpleServiceLocator.getInstance().getImageDownloader();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        CharacterResult characterDetails = (CharacterResult) getIntent().getSerializableExtra(CHARACTER_DETAILS_ARG);
        presenter.onInit(characterDetails);
    }

    @Override
    public void showCharacterDetails(CharacterResult characterDetails) {
        tvName.setText(characterDetails.getName());
        tvLocation.setText(characterDetails.getLocation().getName());
        tvGender.setText(characterDetails.getGender());
        tvSpecies.setText(characterDetails.getSpecies());
        if (characterDetails.getIsKilledByUser() != null && characterDetails.getIsKilledByUser()) {
            tvStatus.setText(getString(R.string.killed_by));
            imgDeadStamp.setVisibility(View.VISIBLE);
        } else {
            if (characterDetails.getStatus().toLowerCase().equals(getString(R.string.dead))) {
                imgDeadStamp.setVisibility(View.VISIBLE);
            }
            tvStatus.setText(characterDetails.getStatus());
        }
        imageDownloader.loadImage(characterDetails.getImage(), imgCharacter);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(CharacterDetailActivity.this, message, Toast.LENGTH_SHORT).show();
        Log.i("R&M", message);
    }
}
