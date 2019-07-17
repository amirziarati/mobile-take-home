package com.gustlogix.rickandmorty;

import android.app.Application;

import com.gustlogix.rickandmorty.dto.character.CharacterResult;
import com.gustlogix.rickandmorty.dto.episode.AllEpisodeResponse;
import com.gustlogix.rickandmorty.dto.episode.EpisodeResult;
import com.gustlogix.rickandmorty.repo.CharacterRepository;
import com.gustlogix.rickandmorty.repo.CharacterRepositoryImpl;
import com.gustlogix.rickandmorty.repo.EpisodeRepository;
import com.gustlogix.rickandmorty.repo.EpisodeRepositoryImpl;
import com.gustlogix.rickandmorty.repo.local.character.CharacterLocalService;
import com.gustlogix.rickandmorty.repo.local.character.CharacterLocalServiceImpl;
import com.gustlogix.rickandmorty.repo.local.db.DbHelper;
import com.gustlogix.rickandmorty.repo.local.db.DbHelperImpl;
import com.gustlogix.rickandmorty.repo.local.downloadcache.FileCacheLocalService;
import com.gustlogix.rickandmorty.repo.local.downloadcache.FileCacheLocalServiceImpl;
import com.gustlogix.rickandmorty.repo.local.downloadcache.FileCacheManager;
import com.gustlogix.rickandmorty.repo.local.downloadcache.FileCacheManagerImpl;
import com.gustlogix.rickandmorty.repo.local.episode.EpisodeLocalService;
import com.gustlogix.rickandmorty.repo.local.episode.EpisodeLocalServiceImpl;
import com.gustlogix.rickandmorty.repo.remote.character.CharacterRemoteService;
import com.gustlogix.rickandmorty.repo.remote.character.CharacterRemoteServiceImpl;
import com.gustlogix.rickandmorty.repo.remote.character.deserializer.MultipleCharacterDeserializerImpl;
import com.gustlogix.rickandmorty.repo.remote.character.deserializer.SingleCharacterDeserializerImpl;
import com.gustlogix.rickandmorty.repo.remote.episode.EpisodeRemoteService;
import com.gustlogix.rickandmorty.repo.remote.episode.EpisodeRemoteServiceImpl;
import com.gustlogix.rickandmorty.repo.remote.episode.deserializer.AllEpisodeDeserializerImpl;
import com.gustlogix.rickandmorty.repo.remote.episode.deserializer.MultipleEpisodeDeserializerImpl;
import com.gustlogix.rickandmorty.repo.remote.episode.deserializer.SingleEpisodeDeserializerImpl;
import com.gustlogix.rickandmorty.repo.remote.imagedownloader.DownloadHelper;
import com.gustlogix.rickandmorty.repo.remote.imagedownloader.DownloadHelperImpl;
import com.gustlogix.rickandmorty.repo.remote.imagedownloader.ImageDownloader;
import com.gustlogix.rickandmorty.repo.remote.imagedownloader.ImageDownloaderImpl;
import com.gustlogix.rickandmorty.repo.remote.network.JsonDeserializer;
import com.gustlogix.rickandmorty.repo.remote.network.NetworkServiceImpl;
import com.gustlogix.rickandmorty.view.characterdetails.CharacterDetailsPresenter;
import com.gustlogix.rickandmorty.view.characterdetails.CharacterDetailsPresenterImpl;
import com.gustlogix.rickandmorty.view.characterdetails.CharacterDetailsView;
import com.gustlogix.rickandmorty.view.characters.CharactersPresenter;
import com.gustlogix.rickandmorty.view.characters.CharactersPresenterImpl;
import com.gustlogix.rickandmorty.view.characters.CharactersView;
import com.gustlogix.rickandmorty.view.episode.EpisodesPresenter;
import com.gustlogix.rickandmorty.view.episode.EpisodesPresenterImpl;
import com.gustlogix.rickandmorty.view.episode.EpisodesView;

import java.util.List;

public class SimpleServiceLocator {
    private static SimpleServiceLocator instance;

    private static Application context;

    private SimpleServiceLocator() {

    }

    public static SimpleServiceLocator getInstance() {
        if (context == null) {
            new IllegalStateException("the service locator has to be initialized in Application class' onCreate !!");
        }
        if (instance == null)
            instance = new SimpleServiceLocator();

        return instance;
    }

    static void init(Application application) {
        context = application;
    }

    public EpisodesPresenter getEpisodePresenter(EpisodesView episodesView) {
        return new EpisodesPresenterImpl(episodesView, getEpisodeRepository());
    }

    private EpisodeRepository getEpisodeRepository() {
        return new EpisodeRepositoryImpl(getEpisodeRemoteService(), getEpisodeLocalService());
    }

    private EpisodeLocalService getEpisodeLocalService() {
        return new EpisodeLocalServiceImpl(getDbHelper());
    }

    private DbHelper getDbHelper() {
        return new DbHelperImpl(getApplicationContext());
    }

    private Application getApplicationContext() {
        return context;
    }

    private EpisodeRemoteService getEpisodeRemoteService() {
        return new EpisodeRemoteServiceImpl(getSingleNetworkService(), getMultipleEpisodeNetworkService(), getAllEpisodesNetworkService());
    }

    private NetworkServiceImpl<AllEpisodeResponse> getAllEpisodesNetworkService() {
        return new NetworkServiceImpl<>(getAllEpisodeDeserializer());
    }

    private JsonDeserializer<AllEpisodeResponse> getAllEpisodeDeserializer() {
        return new AllEpisodeDeserializerImpl(new MultipleEpisodeDeserializerImpl(new SingleEpisodeDeserializerImpl()));
    }

    private NetworkServiceImpl<List<EpisodeResult>> getMultipleEpisodeNetworkService() {
        return new NetworkServiceImpl<>(getMultipleEpisodeDeserializer());
    }

    private JsonDeserializer<List<EpisodeResult>> getMultipleEpisodeDeserializer() {
        return new MultipleEpisodeDeserializerImpl(getSingleEpisodeDeserializer());
    }

    private NetworkServiceImpl<EpisodeResult> getSingleNetworkService() {
        return new NetworkServiceImpl<>(getSingleEpisodeDeserializer());
    }

    private JsonDeserializer<EpisodeResult> getSingleEpisodeDeserializer() {
        return new SingleEpisodeDeserializerImpl();
    }

    public CharactersPresenter getCharacterPresenter(CharactersView charactersView) {
        return new CharactersPresenterImpl(charactersView, getCharacterRepository());
    }

    private CharacterRepository getCharacterRepository() {
        return new CharacterRepositoryImpl(getCharacterRemoteService(), getCharacterLocalService());
    }

    private CharacterRemoteService getCharacterRemoteService() {
        return new CharacterRemoteServiceImpl(getSingleCharacterNetworkService(), getMultipleCharactersNetworkService());
    }

    private NetworkServiceImpl<CharacterResult> getSingleCharacterNetworkService() {
        return new NetworkServiceImpl<CharacterResult>(getSingleCharacterDeserializer());
    }

    private NetworkServiceImpl<List<CharacterResult>> getMultipleCharactersNetworkService() {
        return new NetworkServiceImpl<List<CharacterResult>>(getMultipleCharacterDeserializer());
    }

    private JsonDeserializer<List<CharacterResult>> getMultipleCharacterDeserializer() {
        return new MultipleCharacterDeserializerImpl(getSingleCharacterDeserializer());
    }

    private JsonDeserializer<CharacterResult> getSingleCharacterDeserializer() {
        return new SingleCharacterDeserializerImpl();
    }

    private CharacterLocalService getCharacterLocalService() {
        return new CharacterLocalServiceImpl(getDbHelper());
    }

    public CharacterDetailsPresenter getCharacterDetailsPresenter(CharacterDetailsView characterDetailsView) {
        return new CharacterDetailsPresenterImpl(characterDetailsView);
    }

    public ImageDownloader getImageDownloader() {
        return new ImageDownloaderImpl(getDownloadHelper());
    }

    private DownloadHelper getDownloadHelper() {
        return DownloadHelperImpl.getInstance(getDownloadPath(), getFileCacheManager());
    }

    private FileCacheManager getFileCacheManager() {
        return new FileCacheManagerImpl(getFileCacheLocalService(), getCacheDirectoryPath(), getCacheSizeInMegaBytes());
    }

    private FileCacheLocalService getFileCacheLocalService() {
        return new FileCacheLocalServiceImpl(new DbHelperImpl(getApplicationContext()));
    }

    private String getCacheDirectoryPath() {
        return getDownloadPath();
    }

    private String getDownloadPath() {
        return getApplicationContext().getCacheDir() + "/downloads";
    }

    private int getCacheSizeInMegaBytes() {
        return 20;
    }
}
