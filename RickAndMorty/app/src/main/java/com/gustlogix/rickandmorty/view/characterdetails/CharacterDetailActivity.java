package com.gustlogix.rickandmorty.view.characterdetails;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.gustlogix.rickandmorty.R;
import com.gustlogix.rickandmorty.dto.character.CharacterResult;
import com.gustlogix.rickandmorty.repo.RepositoryCallback;
import com.gustlogix.rickandmorty.repo.local.downloadcache.CacheEntryNotFoundException;
import com.gustlogix.rickandmorty.repo.local.downloadcache.FileCacheEntry;
import com.gustlogix.rickandmorty.repo.local.downloadcache.FileCacheLocalRepo;
import com.gustlogix.rickandmorty.repo.local.downloadcache.FileCacheManagerImpl;
import com.gustlogix.rickandmorty.repo.remote.imagedownloader.DownloadHelperImpl;
import com.gustlogix.rickandmorty.repo.remote.imagedownloader.ImageDownloader;
import com.gustlogix.rickandmorty.repo.remote.imagedownloader.ImageDownloaderImpl;

import java.util.List;

public class CharacterDetailActivity extends Activity {

    public static final String CHARACTER_DETAILS_ARG = "character_details";
    ImageView imgCharacter;
    TextView tvName;
    TextView tvSpecies;
    TextView tvStatus;
    TextView tvGender;
    TextView tvLocation;

    ImageDownloader imageDownloader;

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

        tvName.setText(characterDetails.getName());
        tvLocation.setText(characterDetails.getLocation().getName());
        tvGender.setText(characterDetails.getGender());
        tvSpecies.setText(characterDetails.getSpecies());
        tvStatus.setText(characterDetails.getStatus());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }
        }

        imageDownloader = new ImageDownloaderImpl(DownloadHelperImpl.getInstance(this.getExternalCacheDir() + "downloads", new FileCacheManagerImpl(new FileCacheLocalRepo() {
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
            public void retrieve(String url, RepositoryCallback<FileCacheEntry> callback) {
                callback.onError(new CacheEntryNotFoundException());
            }

            @Override
            public void retrieveAllSortedByLastRetrieved(RepositoryCallback<List<FileCacheEntry>> callback) {
//            callback.onError(new CacheEntryNotFoundException());
            }
        }, this.getExternalCacheDir() + "downloads", 20)));

        imageDownloader.loadImage(CharacterDetailActivity.this, characterDetails.getImage(), imgCharacter);
    }

//    @Override
//    public void showProgress() {
//    }
//
//    @Override
//    public void hideProgress() {
//    }
//
//    @Override
//    public void showMessage(String message) {
//        Toast.makeText(CharacterDetailActivity.this, message, Toast.LENGTH_LONG).show();
//        Log.i("R&M", message);
//    }

}
