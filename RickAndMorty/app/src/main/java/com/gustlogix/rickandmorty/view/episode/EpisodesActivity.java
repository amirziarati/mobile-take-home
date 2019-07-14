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
import com.gustlogix.rickandmorty.dto.episode.AllEpisodeResponse;
import com.gustlogix.rickandmorty.dto.episode.EpisodeResult;
import com.gustlogix.rickandmorty.repo.EpisodeRepositoryImpl;
import com.gustlogix.rickandmorty.repo.local.db.DbHelperImpl;
import com.gustlogix.rickandmorty.repo.local.episode.EpisodeLocalServiceImpl;
import com.gustlogix.rickandmorty.repo.remote.episode.EpisodeRemoteServiceImpl;
import com.gustlogix.rickandmorty.repo.remote.episode.deserializer.AllEpisodeDeserializerImpl;
import com.gustlogix.rickandmorty.repo.remote.episode.deserializer.MultipleEpisodeDeserializerImpl;
import com.gustlogix.rickandmorty.repo.remote.episode.deserializer.SingleEpisodeDeserializerImpl;
import com.gustlogix.rickandmorty.repo.remote.network.NetworkServiceImpl;
import com.gustlogix.rickandmorty.view.EndlessScrollListener;
import com.gustlogix.rickandmorty.view.characters.CharactersActivity;

import java.util.ArrayList;
import java.util.List;

public class EpisodesActivity extends Activity implements EpisodesView {

    LinearLayout llProgressBar;
    ListView lstEpisodes;

    private final SingleEpisodeDeserializerImpl singleEpisodeDeserializer = new SingleEpisodeDeserializerImpl();
    private final NetworkServiceImpl<EpisodeResult> singleNetworkService = new NetworkServiceImpl<>(singleEpisodeDeserializer);
    private final MultipleEpisodeDeserializerImpl multipleEpisodeDeserializer = new MultipleEpisodeDeserializerImpl(new SingleEpisodeDeserializerImpl());
    private final AllEpisodeDeserializerImpl allEpisodeDeserializer = new AllEpisodeDeserializerImpl(new MultipleEpisodeDeserializerImpl(new SingleEpisodeDeserializerImpl()));
    private final NetworkServiceImpl<AllEpisodeResponse> allNetworkService = new NetworkServiceImpl<>(allEpisodeDeserializer);
    private final NetworkServiceImpl<List<EpisodeResult>> multipleNetworkService = new NetworkServiceImpl<>(multipleEpisodeDeserializer);
    private final EpisodeRemoteServiceImpl episodeRemoteService = new EpisodeRemoteServiceImpl(singleNetworkService, multipleNetworkService, allNetworkService);
    private EpisodeLocalServiceImpl episodeLocalService;
    private EpisodeRepositoryImpl episodeRepository;
    private EpisodesPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episodes);

        llProgressBar = findViewById(R.id.llProgressbar);
        lstEpisodes = findViewById(R.id.lstEpisodes);

        episodeLocalService = new EpisodeLocalServiceImpl(new DbHelperImpl(getApplicationContext()));
        episodeRepository = new EpisodeRepositoryImpl(episodeRemoteService, episodeLocalService);
        presenter = new EpisodesPresenterImpl(this, episodeRepository);

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
