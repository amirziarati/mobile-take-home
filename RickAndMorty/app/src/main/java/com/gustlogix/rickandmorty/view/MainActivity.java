package com.gustlogix.rickandmorty.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gustlogix.rickandmorty.R;
import com.gustlogix.rickandmorty.dto.episode.AllEpisodeResponse;
import com.gustlogix.rickandmorty.dto.episode.EpisodeResult;
import com.gustlogix.rickandmorty.repo.RepositoryCallback;
import com.gustlogix.rickandmorty.repo.remote.episode.EpisodeRemoteService;
import com.gustlogix.rickandmorty.repo.remote.episode.EpisodeRemoteServiceImpl;
import com.gustlogix.rickandmorty.repo.remote.episode.deserializer.AllEpisodeDeserializerImpl;
import com.gustlogix.rickandmorty.repo.remote.episode.deserializer.MultipleEpisodeDeserializerImpl;
import com.gustlogix.rickandmorty.repo.remote.episode.deserializer.SingleEpisodeDeserializerImpl;
import com.gustlogix.rickandmorty.repo.remote.network.NetworkServiceImpl;

import java.util.List;

public class MainActivity extends Activity {

    Button btnTest;
    ProgressBar prgTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTest = findViewById(R.id.btnTest);
        prgTest = findViewById(R.id.prgTest);

//        final CharacterRemoteService characterRemoteService = new CharacterRemoteServiceImpl(new NetworkServiceImpl<CharacterResult>(new SingleCharacterDeserializerImpl()), new NetworkServiceImpl<List<CharacterResult>>(new MultipleCharacterDeserializerImpl(new SingleCharacterDeserializerImpl())));
//
//        btnTest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                prgTest.setVisibility(View.VISIBLE);
//
//                List<Integer> ids = new ArrayList<>();
//                ids.add(2);
//                ids.add(183);
//                ids.add(12);
//
//                characterRemoteService.fetchMultipleCharacter(ids, new RepositoryCallback<List<CharacterResult>>() {
//
//                    @Override
//                    public void onSuccess(List<CharacterResult> data) {
//                        prgTest.setVisibility(View.INVISIBLE);
//                        showToast(data.size() + "");
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//                        prgTest.setVisibility(View.INVISIBLE);
//                        showToast(e.toString());
//                    }
//
//                });
//
//            }
//        });


        final EpisodeRemoteService episodeRemoteService = new EpisodeRemoteServiceImpl(new NetworkServiceImpl<EpisodeResult>(new SingleEpisodeDeserializerImpl()), new NetworkServiceImpl<List<EpisodeResult>>(new MultipleEpisodeDeserializerImpl(new SingleEpisodeDeserializerImpl())), new NetworkServiceImpl<AllEpisodeResponse>(new AllEpisodeDeserializerImpl(new MultipleEpisodeDeserializerImpl(new SingleEpisodeDeserializerImpl()))));
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prgTest.setVisibility(View.VISIBLE);

                episodeRemoteService.fetchAllEpisodes(1, new RepositoryCallback<AllEpisodeResponse>() {
                    @Override
                    public void onSuccess(AllEpisodeResponse data) {
                        prgTest.setVisibility(View.INVISIBLE);
                        showToast(data.getResults().size() + "");
                    }

                    @Override
                    public void onError(Exception e) {
                        prgTest.setVisibility(View.INVISIBLE);
                        showToast(e.toString());
                    }
                });

            }
        });


    }


    private void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
        Log.i("R&M", message);
    }
}
