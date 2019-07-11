package com.gustlogix.rickandmorty;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.gustlogix.rickandmorty.dto.CharacterResponse;
import com.gustlogix.rickandmorty.repo.remote.character.CharacterJsonDeserializerImpl;
import com.gustlogix.rickandmorty.repo.remote.character.CharacterNetworkService;
import com.gustlogix.rickandmorty.repo.remote.network.NetworkRequest;
import com.gustlogix.rickandmorty.repo.remote.network.NetworkServiceCallback;

public class MainActivity extends Activity {

    Button btnTest;
    ProgressBar prgTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTest = findViewById(R.id.btnTest);
        prgTest = findViewById(R.id.prgTest);

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prgTest.setVisibility(View.VISIBLE);

                NetworkRequest networkRequest = new NetworkRequest.Builder()
                        .setUrl("https://rickandmortyapi.com/api/character/")
                        .setQueryParameters(new NetworkRequest.QueryParameter("page", "2"))
                        .build();

                new CharacterNetworkService(new CharacterJsonDeserializerImpl()).call(networkRequest, new NetworkServiceCallback<CharacterResponse>() {
                    @Override
                    public void onResponse(CharacterResponse response) {
                        prgTest.setVisibility(View.INVISIBLE);
                        System.out.println(response.toString());
                        Log.i("R&T", response.toString());
                    }

                    @Override
                    public void onError(Exception e) {
                        prgTest.setVisibility(View.INVISIBLE);
                        System.out.println(e.toString());
                        Log.e("R&T", e.toString());
                    }
                });
            }
        });


    }
}
