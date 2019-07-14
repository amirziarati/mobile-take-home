package com.gustlogix.rickandmorty.view.characterdetails;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.gustlogix.rickandmorty.R;
import com.gustlogix.rickandmorty.dto.character.CharacterResult;
import com.gustlogix.rickandmorty.repo.remote.RemoteRepositoryCallback;
import com.gustlogix.rickandmorty.repo.local.downloadcache.CacheEntryNotFoundException;
import com.gustlogix.rickandmorty.repo.local.downloadcache.FileCacheEntry;
import com.gustlogix.rickandmorty.repo.local.downloadcache.FileCacheLocalService;
import com.gustlogix.rickandmorty.repo.local.downloadcache.FileCacheManagerImpl;
import com.gustlogix.rickandmorty.repo.remote.imagedownloader.DownloadHelperImpl;
import com.gustlogix.rickandmorty.repo.remote.imagedownloader.ImageDownloader;
import com.gustlogix.rickandmorty.repo.remote.imagedownloader.ImageDownloaderImpl;

import java.util.List;

public class CharacterDetailActivity extends Activity implements CharacterDetailsView {

    public static final String CHARACTER_DETAILS_ARG = "character_details";
    ImageView imgCharacter;
    TextView tvName;
    TextView tvSpecies;
    TextView tvStatus;
    TextView tvGender;
    TextView tvLocation;

    ImageDownloader imageDownloader;
    CharacterDetailsPresenter presenter = new CharacterDetailsPresenterImpl(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_details);

        CharacterResult characterDetails = (CharacterResult) getIntent().getSerializableExtra(CHARACTER_DETAILS_ARG);

        imgCharacter = findViewById(R.id.imgCharacter);
        tvName = findViewById(R.id.tvName);
        tvSpecies = findViewById(R.id.tvSpecies);
        tvStatus = findViewById(R.id.tvStatus);
        tvLocation = findViewById(R.id.tvLocation);
        tvGender = findViewById(R.id.tvGender);

        imageDownloader = new ImageDownloaderImpl(DownloadHelperImpl.getInstance(this.getCacheDir() + "/downloads", new FileCacheManagerImpl(new FileCacheLocalService() {
            @Override
            public void insert(FileCacheEntry fileCacheEntry) {

            }

            @Override
            public void update(FileCacheEntry fileCacheEntry) {

            }

            @Override
            public void remove(FileCacheEntry fileCacheEntry) {

            }

            @Override
            public void retrieve(String url, RemoteRepositoryCallback<FileCacheEntry> callback) {
                callback.onError(new CacheEntryNotFoundException());
            }

            @Override
            public void retrieveAllSortedByLastRetrieved(RemoteRepositoryCallback<List<FileCacheEntry>> callback) {
//            callback.onError(new CacheEntryNotFoundException());
            }
        }, this.getCacheDir() + "/downloads", 20)));


        presenter.onInit(characterDetails);
    }

    @Override
    public void showCharacterDetails(CharacterResult characterDetails) {
        tvName.setText(characterDetails.getName());
        tvLocation.setText(characterDetails.getLocation().getName());
        tvGender.setText(characterDetails.getGender());
        tvSpecies.setText(characterDetails.getSpecies());
        tvStatus.setText(characterDetails.getStatus());
        imageDownloader.loadImage(CharacterDetailActivity.this, characterDetails.getImage(), imgCharacter);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showMessage(String message) {

    }
}
