package com.gustlogix.rickandmorty.repo.local.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gustlogix.rickandmorty.dto.character.AllCharacterResponse;
import com.gustlogix.rickandmorty.dto.character.CharacterResult;
import com.gustlogix.rickandmorty.dto.character.Info;
import com.gustlogix.rickandmorty.dto.character.Location;
import com.gustlogix.rickandmorty.dto.character.Origin;
import com.gustlogix.rickandmorty.dto.episode.AllEpisodeResponse;
import com.gustlogix.rickandmorty.dto.episode.EpisodeResult;
import com.gustlogix.rickandmorty.repo.local.downloadcache.FileCacheEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DbHelperImpl extends SQLiteOpenHelper implements DbHelper {

    public DbHelperImpl(Context context) {
        super(context, "RickAndMortyDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB.CHARACTER.CREATE_TABLE_COMMAND);
        db.execSQL(DB.EPISODE.CREATE_TABLE_COMMAND);
        db.execSQL(DB.FILECACHE.CREATE_TABLE_COMMAND);
    }

    @Override
    public CharacterResult fetchSingleCharacter(int id) {
        CharacterResult characterResult = null;
        SQLiteDatabase db = getReadableDatabase();
        try {
            String[] selectionArgs = {"" + id};
            Cursor cursor = db.query(
                    DB.CHARACTER.TABLE_NAME,
                    getCharacterProjection(),
                    getCharacterSelection(),
                    selectionArgs,
                    null,
                    null,
                    getCharacterSortOrder()
            );
            if (cursor.moveToFirst()) {
                characterResult = extractCharacterResult(cursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return characterResult;
    }

    @Override
    public List<CharacterResult> fetchMultipleCharacter(List<Integer> characterIds) {
        List<CharacterResult> characterResult = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        try {
            if (characterIds.size() > 0) {
                String[] selectionArgs = new String[characterIds.size()];
                StringBuilder selectionPlaceHolders = new StringBuilder();
                selectionPlaceHolders.append("(");
                for (int i = 0; i < characterIds.size(); i++) {
                    selectionArgs[i] = characterIds.get(i).toString();
                    selectionPlaceHolders.append("?,");
                }
                selectionPlaceHolders.replace(selectionPlaceHolders.length() - 1, selectionPlaceHolders.length(), ")");

                Cursor cursor = db.query(
                        DB.CHARACTER.TABLE_NAME,
                        getCharacterProjection(),
                        DB.CHARACTER.C_ID + " IN " + selectionPlaceHolders.toString(),
                        selectionArgs,
                        null,
                        null,
                        getCharacterSortOrder()
                );
                while (cursor.moveToNext()) {
                    characterResult.add(extractCharacterResult(cursor));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return characterResult;
    }

    @Override
    public AllCharacterResponse fetchAllCharacters(int page, int pageSize) {
        AllCharacterResponse allCharacterResponse = new AllCharacterResponse();
        SQLiteDatabase db = getReadableDatabase();
        try {
            List<CharacterResult> characterResult = new ArrayList<>();
            Cursor cursor = db.query(
                    DB.CHARACTER.TABLE_NAME,
                    getCharacterProjection(),
                    null,
                    null,
                    null,
                    null,
                    getCharacterSortOrder()
            );
            while (cursor.moveToNext()) {
                characterResult.add(extractCharacterResult(cursor));
            }
            Info info = new Info();
            info.setCount(characterResult.size());
            info.setPages(1);
            info.setNext("");
            info.setPrev("");
            allCharacterResponse.setInfo(info);
            allCharacterResponse.setResults(getPage(characterResult, page, pageSize));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return allCharacterResponse;
    }

    @Override
    public EpisodeResult fetchSingleEpisode(int id) {
        EpisodeResult episodeResult = null;
        SQLiteDatabase db = getReadableDatabase();
        try {
            String[] selectionArgs = {"" + id};
            Cursor cursor = db.query(
                    DB.EPISODE.TABLE_NAME,
                    getEpisodeProjection(),
                    getEpisodeSelection(),
                    selectionArgs,
                    null,
                    null,
                    getEpisodeSortOrder()
            );
            if (cursor.moveToFirst()) {
                episodeResult = extractEpisodeResult(cursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return episodeResult;
    }

    @Override
    public List<EpisodeResult> fetchMultipleEpisode(List<Integer> characterIds) {
        List<EpisodeResult> episodes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        try {
            if (characterIds.size() > 0) {
                String[] selectionArgs = new String[characterIds.size()];
                for (int i = 0; i < characterIds.size(); i++) {
                    selectionArgs[i] = characterIds.get(i).toString();
                }
                Cursor cursor = db.query(
                        DB.EPISODE.TABLE_NAME,
                        getEpisodeProjection(),
                        getEpisodeSelection(),
                        selectionArgs,
                        null,
                        null,
                        getEpisodeSortOrder()
                );
                while (cursor.moveToNext()) {
                    episodes.add(extractEpisodeResult(cursor));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return episodes;
    }

    @Override
    public AllEpisodeResponse fetchAllEpisodes(int page, int pageSize) {
        AllEpisodeResponse allEpisodeResponse = new AllEpisodeResponse();
        SQLiteDatabase db = getReadableDatabase();
        try {
            List<EpisodeResult> episodes = new ArrayList<>();
            Cursor cursor = db.query(
                    DB.EPISODE.TABLE_NAME,
                    getEpisodeProjection(),
                    null,
                    null,
                    null,
                    null,
                    getEpisodeSortOrder()
            );
            while (cursor.moveToNext()) {
                episodes.add(extractEpisodeResult(cursor));
            }
            com.gustlogix.rickandmorty.dto.episode.Info info = new com.gustlogix.rickandmorty.dto.episode.Info();
            info.setCount(episodes.size());
            info.setPages(1);
            info.setNext("");
            info.setPrev("");
            allEpisodeResponse.setInfo(info);
            allEpisodeResponse.setResults(getPage(episodes, page, pageSize));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return allEpisodeResponse;
    }

    @Override
    public long insertCharacter(CharacterResult characterResult) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(DB.CHARACTER.C_ID, characterResult.getId());
            values.put(DB.CHARACTER.C_NAME, characterResult.getName());
            values.put(DB.CHARACTER.C_STATUS, characterResult.getStatus());
            values.put(DB.CHARACTER.C_SPECIES, characterResult.getSpecies());
            values.put(DB.CHARACTER.C_TYPE, characterResult.getType());
            values.put(DB.CHARACTER.C_GENDER, characterResult.getGender());
            values.put(DB.CHARACTER.C_ORIGIN_NAME, characterResult.getOrigin().getName());
            values.put(DB.CHARACTER.C_ORIGIN_URL, characterResult.getOrigin().getUrl());
            values.put(DB.CHARACTER.C_LOCATION_NAME, characterResult.getLocation().getName());
            values.put(DB.CHARACTER.C_LOCATION_URL, characterResult.getLocation().getUrl());
            values.put(DB.CHARACTER.C_COMMA_SEPARATED_EPISODES, getCommaSeparatedEpisodes(characterResult));
            values.put(DB.CHARACTER.C_URL, characterResult.getUrl());
            values.put(DB.CHARACTER.C_IMAGE, characterResult.getImage());
            values.put(DB.CHARACTER.C_CREATED, characterResult.getCreated());
            long id = db.insertWithOnConflict(DB.CHARACTER.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            if (id == -1) {
                id = db.update(DB.CHARACTER.TABLE_NAME, values, getCharacterSelection(), new String[]{characterResult.getId().toString()});
            }
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            db.close();
        }
    }

    @Override
    public void insertCharacters(List<CharacterResult> characterResults) {
        for (CharacterResult characterResult : characterResults) {
            insertCharacter(characterResult);
        }
    }

    @Override
    public long insertEpisode(EpisodeResult episodeResult) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(DB.EPISODE.C_ID, episodeResult.getId());
            values.put(DB.EPISODE.C_NAME, episodeResult.getName());
            values.put(DB.EPISODE.C_AIR_DATE, episodeResult.getAirDate());
            values.put(DB.EPISODE.C_EPISODE, episodeResult.getEpisode());
            values.put(DB.EPISODE.C_COMMA_SEPARATED_CHARACTERS, getCommaSeparatedCharacters(episodeResult));
            values.put(DB.EPISODE.C_URL, episodeResult.getUrl());
            values.put(DB.EPISODE.C_CREATED, episodeResult.getCreated());
            long id = db.insertWithOnConflict(DB.EPISODE.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            if (id == -1) {
                id = db.update(DB.EPISODE.TABLE_NAME, values, getEpisodeSelection(), new String[]{episodeResult.getId().toString()});
            }
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            db.close();
        }
    }

    @Override
    public void insertEpisodes(List<EpisodeResult> episodeResults) {
        for (EpisodeResult episodeResult : episodeResults) {
            insertEpisode(episodeResult);
        }
    }

    @Override
    public long insertFileCache(FileCacheEntry fileCacheEntry) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(DB.FILECACHE.C_URL, fileCacheEntry.getUrl());
            values.put(DB.FILECACHE.C_FILE_NAME, fileCacheEntry.getFileName());
            values.put(DB.FILECACHE.C_LAST_RETRIEVED, fileCacheEntry.getLastRetrievedTimeStamp());
            long id = db.insertWithOnConflict(DB.FILECACHE.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            if (id == -1) {
                id = db.update(DB.FILECACHE.TABLE_NAME, values, getFileCacheSelection(), new String[]{fileCacheEntry.getUrl()});
            }
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            db.close();
        }
    }

    @Override
    public long removeFileCache(FileCacheEntry fileCacheEntry) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            return db.delete(DB.FILECACHE.TABLE_NAME, DB.FILECACHE.C_URL + "=" + fileCacheEntry.getUrl(), null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            db.close();
        }
    }

    @Override
    public FileCacheEntry fetchFileCache(String url) {
        FileCacheEntry fileCacheEntry = null;
        SQLiteDatabase db = getReadableDatabase();
        try {
            String[] selectionArgs = {url};
            Cursor cursor = db.query(
                    DB.FILECACHE.TABLE_NAME,
                    getFileCacheProjection(),
                    getFileCacheSelection(),
                    selectionArgs,
                    null,
                    null,
                    getFileCacheSortOrder()
            );
            if (cursor.moveToFirst()) {
                fileCacheEntry = extractFileCache(cursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return fileCacheEntry;
    }

    @Override
    public List<FileCacheEntry> fetchFileCaches() {
        SQLiteDatabase db = getReadableDatabase();
        try {
            List<FileCacheEntry> fileCaches = new ArrayList<>();
            Cursor cursor = db.query(
                    DB.FILECACHE.TABLE_NAME,
                    getFileCacheProjection(),
                    null,
                    null,
                    null,
                    null,
                    getFileCacheSortOrder()
            );
            while (cursor.moveToNext()) {
                fileCaches.add(extractFileCache(cursor));
            }
            return fileCaches;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return null;
    }

    private String getCommaSeparatedEpisodes(CharacterResult characterResult) {
        StringBuilder sb = new StringBuilder();
        for (String episode : characterResult.getEpisode()) {
            sb.append(episode);
            sb.append(",");
        }
        String episodes = removeLastChar(sb.toString());
        return episodes;
    }

    private String getCommaSeparatedCharacters(EpisodeResult episodeResult) {
        StringBuilder sb = new StringBuilder();
        for (String chr : episodeResult.getCharacters()) {
            sb.append(chr);
            sb.append(",");
        }
        String characters = removeLastChar(sb.toString());
        return characters;
    }

    private CharacterResult extractCharacterResult(Cursor cursor) {
        CharacterResult characterResult = new CharacterResult();
        characterResult.setId(cursor.getInt(cursor.getColumnIndex(DB.CHARACTER.C_ID)));
        characterResult.setName(cursor.getString(cursor.getColumnIndex(DB.CHARACTER.C_NAME)));
        characterResult.setStatus(cursor.getString(cursor.getColumnIndex(DB.CHARACTER.C_STATUS)));
        characterResult.setSpecies(cursor.getString(cursor.getColumnIndex(DB.CHARACTER.C_SPECIES)));
        characterResult.setType(cursor.getString(cursor.getColumnIndex(DB.CHARACTER.C_TYPE)));
        characterResult.setGender(cursor.getString(cursor.getColumnIndex(DB.CHARACTER.C_GENDER)));
        characterResult.setOrigin(extractOrigin(cursor));
        characterResult.setLocation(extractLocation(cursor));
        characterResult.setImage(cursor.getString(cursor.getColumnIndex(DB.CHARACTER.C_IMAGE)));
        characterResult.setEpisode(extractEpisodes(cursor));
        characterResult.setUrl(cursor.getString(cursor.getColumnIndex(DB.CHARACTER.C_URL)));
        characterResult.setCreated(cursor.getString(cursor.getColumnIndex(DB.CHARACTER.C_CREATED)));
        return characterResult;
    }

    private EpisodeResult extractEpisodeResult(Cursor cursor) {
        EpisodeResult episodeResult = new EpisodeResult();
        episodeResult.setId(cursor.getInt(cursor.getColumnIndex(DB.EPISODE.C_ID)));
        episodeResult.setName(cursor.getString(cursor.getColumnIndex(DB.EPISODE.C_NAME)));
        episodeResult.setAirDate(cursor.getString(cursor.getColumnIndex(DB.EPISODE.C_AIR_DATE)));
        episodeResult.setEpisode(cursor.getString(cursor.getColumnIndex(DB.EPISODE.C_EPISODE)));
        episodeResult.setCharacters(extractCharacters(cursor));
        episodeResult.setUrl(cursor.getString(cursor.getColumnIndex(DB.EPISODE.C_URL)));
        episodeResult.setCreated(cursor.getString(cursor.getColumnIndex(DB.EPISODE.C_CREATED)));
        return episodeResult;
    }

    private FileCacheEntry extractFileCache(Cursor cursor) {
        return new FileCacheEntry(
                cursor.getString(cursor.getColumnIndex(DB.FILECACHE.C_URL)),
                cursor.getString(cursor.getColumnIndex(DB.FILECACHE.C_FILE_NAME)),
                cursor.getLong(cursor.getColumnIndex(DB.FILECACHE.C_LAST_RETRIEVED))
        );
    }

    private Origin extractOrigin(Cursor cursor) {
        Origin origin = new Origin();
        origin.setName(cursor.getString(cursor.getColumnIndex(DB.CHARACTER.C_ORIGIN_NAME)));
        origin.setUrl(cursor.getString(cursor.getColumnIndex(DB.CHARACTER.C_ORIGIN_URL)));
        return origin;
    }

    private Location extractLocation(Cursor cursor) {
        Location location = new Location();
        location.setName(cursor.getString(cursor.getColumnIndex(DB.CHARACTER.C_LOCATION_NAME)));
        location.setUrl(cursor.getString(cursor.getColumnIndex(DB.CHARACTER.C_LOCATION_URL)));
        return location;
    }

    private List<String> extractEpisodes(Cursor cursor) {
        List<String> episodes = new ArrayList<>();
        String[] commaSeparatedEpisodes = cursor.getString(cursor.getColumnIndex(DB.CHARACTER.C_IMAGE)).split(",");
        for (String episode : commaSeparatedEpisodes) {
            episodes.add(episode);
        }
        return episodes;
    }

    private List<String> extractCharacters(Cursor cursor) {
        List<String> characters = new ArrayList<>();
        String[] commaSeparatedCharacters = cursor.getString(cursor.getColumnIndex(DB.EPISODE.C_COMMA_SEPARATED_CHARACTERS)).split(",");
        characters.addAll(Arrays.asList(commaSeparatedCharacters));
        return characters;
    }

    private String[] getCharacterProjection() {
        String[] projection = {
                DB.CHARACTER.C_ID,
                DB.CHARACTER.C_NAME,
                DB.CHARACTER.C_STATUS,
                DB.CHARACTER.C_SPECIES,
                DB.CHARACTER.C_TYPE,
                DB.CHARACTER.C_GENDER,
                DB.CHARACTER.C_ORIGIN_NAME,
                DB.CHARACTER.C_ORIGIN_URL,
                DB.CHARACTER.C_LOCATION_NAME,
                DB.CHARACTER.C_LOCATION_URL,
                DB.CHARACTER.C_COMMA_SEPARATED_EPISODES,
                DB.CHARACTER.C_URL,
                DB.CHARACTER.C_CREATED,
                DB.CHARACTER.C_IMAGE
        };
        return projection;
    }

    private String[] getEpisodeProjection() {
        String[] projection = {
                DB.EPISODE.C_ID,
                DB.EPISODE.C_NAME,
                DB.EPISODE.C_AIR_DATE,
                DB.EPISODE.C_EPISODE,
                DB.EPISODE.C_COMMA_SEPARATED_CHARACTERS,
                DB.EPISODE.C_URL,
                DB.EPISODE.C_CREATED
        };
        return projection;
    }

    private String[] getFileCacheProjection() {
        String[] projection = {
                DB.FILECACHE.C_URL,
                DB.FILECACHE.C_FILE_NAME,
                DB.FILECACHE.C_LAST_RETRIEVED,
        };
        return projection;
    }

    private String getCharacterSelection() {
        return DB.CHARACTER.C_ID + " = ?";
    }

    private String getEpisodeSelection() {
        return DB.EPISODE.C_ID + " = ?";
    }

    private String getFileCacheSelection() {
        return DB.FILECACHE.C_URL + " = ?";
    }

    private String getCharacterSortOrder() {
        return DB.CHARACTER.C_ID + " ASC";
    }

    private String getEpisodeSortOrder() {
        return DB.CHARACTER.C_ID + " ASC";
    }

    private String getFileCacheSortOrder() {
        return DB.FILECACHE.C_LAST_RETRIEVED + " DESC";
    }

    private String removeLastChar(String str) {
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    private <T> List<T> getPage(List<T> sourceList, int page, int pageSize) {
        if (pageSize <= 0 || page <= 0) {
            throw new IllegalArgumentException("invalid page size: " + pageSize);
        }

        int fromIndex = (page - 1) * pageSize;
        if (sourceList == null || sourceList.size() < fromIndex) {
            return Collections.emptyList();
        }

        // toIndex exclusive
        return sourceList.subList(fromIndex, Math.min(fromIndex + pageSize, sourceList.size()));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    static class DB {

        static class CHARACTER {
            static final String CREATE_TABLE_COMMAND = "CREATE TABLE IF NOT EXISTS " + CHARACTER.TABLE_NAME +
                    " (" + CHARACTER.C_ID + " INTEGER PRIMARY KEY, " +
                    CHARACTER.C_NAME + " TEXT NOT NULL, " +
                    CHARACTER.C_STATUS + " TEXT ," +
                    CHARACTER.C_SPECIES + " TEXT ," +
                    CHARACTER.C_TYPE + " TEXT ," +
                    CHARACTER.C_GENDER + " TEXT ," +
                    CHARACTER.C_ORIGIN_NAME + " TEXT ," +
                    CHARACTER.C_ORIGIN_URL + " TEXT ," +
                    CHARACTER.C_LOCATION_NAME + " TEXT ," +
                    CHARACTER.C_LOCATION_URL + " TEXT ," +
                    CHARACTER.C_COMMA_SEPARATED_EPISODES + " TEXT ," +
                    CHARACTER.C_URL + " TEXT ," +
                    CHARACTER.C_IMAGE + " TEXT ," +
                    CHARACTER.C_CREATED + " TEXT);";
            static final String TABLE_NAME = "CHARACTER";
            static final String C_ID = "id";
            static final String C_NAME = "name";
            static final String C_STATUS = "status";
            static final String C_SPECIES = "species";
            static final String C_TYPE = "type";
            static final String C_GENDER = "gender";
            static final String C_ORIGIN_NAME = "origin_name";
            static final String C_ORIGIN_URL = "origin_url";
            static final String C_LOCATION_NAME = "location_name";
            static final String C_LOCATION_URL = "location_url";
            static final String C_COMMA_SEPARATED_EPISODES = "comma_separated_episodes";
            static final String C_URL = "url";
            static final String C_IMAGE = "image";
            static final String C_CREATED = "created";
        }

        static class EPISODE {
            static final String CREATE_TABLE_COMMAND = "CREATE TABLE IF NOT EXISTS " + EPISODE.TABLE_NAME +
                    "(" + EPISODE.C_ID + " INTEGER PRIMARY KEY, " +
                    EPISODE.C_NAME + " TEXT NOT NULL, " +
                    EPISODE.C_AIR_DATE + " TEXT ," +
                    EPISODE.C_EPISODE + " TEXT ," +
                    EPISODE.C_COMMA_SEPARATED_CHARACTERS + " TEXT ," +
                    EPISODE.C_URL + " TEXT ," +
                    EPISODE.C_CREATED + " TEXT);";
            static final String TABLE_NAME = "EPISODES";
            static final String C_ID = "id";
            static final String C_NAME = "name";
            static final String C_AIR_DATE = "airDate";
            static final String C_EPISODE = "episode";
            static final String C_COMMA_SEPARATED_CHARACTERS = "characters";
            static final String C_URL = "url";
            static final String C_CREATED = "created";
        }

        static class FILECACHE {
            static final String CREATE_TABLE_COMMAND = "CREATE TABLE IF NOT EXISTS " + FILECACHE.TABLE_NAME +
                    "(" + FILECACHE.C_URL + " TEXT PRIMARY KEY, " +
                    FILECACHE.C_FILE_NAME + " TEXT NOT NULL, " +
                    FILECACHE.C_LAST_RETRIEVED + " INTEGER);";
            static final String TABLE_NAME = "FILECACHE";
            static final String C_URL = "url";
            static final String C_FILE_NAME = "filename";
            static final String C_LAST_RETRIEVED = "airDate";
        }
    }
}
