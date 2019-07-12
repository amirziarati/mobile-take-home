package com.gustlogix.rickandmorty.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gustlogix.rickandmorty.R;
import com.gustlogix.rickandmorty.dto.CharacterResult;
import com.gustlogix.rickandmorty.repo.remote.character.CharacterRemoteService;
import com.gustlogix.rickandmorty.repo.RepositoryCallback;
import com.gustlogix.rickandmorty.repo.remote.character.CharacterRemoteServiceImpl;
import com.gustlogix.rickandmorty.repo.remote.character.deserializer.MultipleCharacterDeserializerImpl;
import com.gustlogix.rickandmorty.repo.remote.character.deserializer.SingleCharacterDeserializerImpl;

import java.util.ArrayList;
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

        final CharacterRemoteService characterRemoteService = new CharacterRemoteServiceImpl(new SingleCharacterDeserializerImpl(), new MultipleCharacterDeserializerImpl(new SingleCharacterDeserializerImpl()));

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prgTest.setVisibility(View.VISIBLE);

                List<Integer> ids = new ArrayList<>();
                ids.add(2);
                ids.add(183);
                ids.add(12);

                prgTest.setVisibility(View.VISIBLE);
                characterRemoteService.fetchMultipleCharacter(ids, new RepositoryCallback<List<CharacterResult>>() {

                    @Override
                    public void onSuccess(List<CharacterResult> data) {
                        prgTest.setVisibility(View.INVISIBLE);
                        showToast(data.size() + "");
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
