package com.gustlogix.rickandmorty.view.episode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.gustlogix.rickandmorty.R;
import com.gustlogix.rickandmorty.SimpleServiceLocator;
import com.gustlogix.rickandmorty.dto.episode.EpisodeResult;
import com.gustlogix.rickandmorty.view.EndlessScrollListener;
import com.gustlogix.rickandmorty.view.characters.CharactersActivity;

import java.util.ArrayList;
import java.util.List;

public class EpisodesActivity extends Activity implements EpisodesView {

    LinearLayout llProgressBar;
    ListView lstEpisodes;

    private EpisodesPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episodes);

        llProgressBar = findViewById(R.id.llProgressbar);
        lstEpisodes = findViewById(R.id.lstEpisodes);

        presenter = SimpleServiceLocator.getInstance().getEpisodePresenter(this);

        lstEpisodes.setOnScrollListener(new EndlessScrollListener(5, 0) {
            @Override
            public boolean onLoadNextPage(int page, int totalItemsCount) {

                presenter.onNewPageOfDataRequested();

                return true;
            }
        });


        presenter.onInit();

        presenter.onNewPageOfDataRequested();
    }

    @Override
    public void hideProgress() {
        llProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showProgress() {
        llProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void addNewEpisodesToView(List<EpisodeResult> episodes) {

        llProgressBar.setVisibility(View.INVISIBLE);
        if (lstEpisodes.getAdapter() == null) {
            lstEpisodes.setAdapter(new EpisodesAdapter(EpisodesActivity.this, episodes, new EpisodesAdapter.OnItemClickListener() {
                @Override
                public void onItemClicked(EpisodeResult episodeResult) {
                    presenter.onEpisodeClicked(episodeResult);

                }
            }));
        } else {
            ((EpisodesAdapter) lstEpisodes.getAdapter()).addEpisodes(episodes);
        }
    }

    @Override
    public void navigateToCharactersView(ArrayList<Integer> characterIds) {
        Intent intent = new Intent(EpisodesActivity.this, CharactersActivity.class);
        intent.putIntegerArrayListExtra(CharactersActivity.IDS_LIST_ARG, characterIds);
        startActivity(intent);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(EpisodesActivity.this, message, Toast.LENGTH_LONG).show();
        Log.i("R&M", message);
    }
}
