package com.gustlogix.rickandmorty;

import com.gustlogix.rickandmorty.dto.character.AllCharacterResponse;
import com.gustlogix.rickandmorty.dto.character.CharacterResult;
import com.gustlogix.rickandmorty.dto.character.Location;
import com.gustlogix.rickandmorty.dto.character.Origin;
import com.gustlogix.rickandmorty.dto.episode.AllEpisodeResponse;
import com.gustlogix.rickandmorty.dto.episode.EpisodeResult;
import com.gustlogix.rickandmorty.repo.local.db.DbHelper;
import com.gustlogix.rickandmorty.repo.local.db.DbHelperImpl;
import com.gustlogix.rickandmorty.repo.local.downloadcache.FileCacheEntry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
public class DbHelperImplTest {

    private DbHelper dbHelper = new DbHelperImpl(RuntimeEnvironment.application);


    @Before
    public void clearDb() {
        dbHelper.deleteAllCharacters();
        dbHelper.deleteAllEpisodes();
        dbHelper.deleteAllCacheEntries();
    }


    @Test
    public void testInsertAndFetchSingleCharacter() {
        // Given
        CharacterResult input = provideCharacter("1");

        // When
        dbHelper.insertCharacter(input);
        CharacterResult result = dbHelper.fetchSingleCharacter(input.getId());

        // Then
        assertTwoCharactersAreEqual(input, result);
    }

    @Test
    public void testInsertAndFetchSingleCharacterWithEmptyEpisodesList() {
        // Given
        CharacterResult input = provideCharacterWithEmptyEpisodesList();

        // When
        dbHelper.insertCharacter(input);
        CharacterResult result = dbHelper.fetchSingleCharacter(input.getId());

        // Then
        assertNotNull(result.getEpisode());
        assertEquals(0, result.getEpisode().size());
    }

    @Test
    public void testInsertAndFetchSingleCharacterWithoutOrigin() {
        // Given
        CharacterResult input = provideCharacterWithoutOrigin();

        // When
        dbHelper.insertCharacter(input);
        CharacterResult result = dbHelper.fetchSingleCharacter(input.getId());

        // Then
        assertNull(result.getOrigin());
        assertTwoCharactersAreEqual(input, result);
    }

    @Test
    public void testInsertAndFetchSingleCharacterWithoutLocation() {
        // Given
        CharacterResult input = provideCharacterWithoutLocation();

        // When
        dbHelper.insertCharacter(input);
        CharacterResult result = dbHelper.fetchSingleCharacter(input.getId());

        // Then
        assertNull(result.getLocation());
        assertTwoCharactersAreEqual(input, result);
    }

    @Test
    public void testInsertAndFetchMultipleCharacter() {
        // Given
        CharacterResult input2 = provideCharacter("2");
        input2.setId(2);
        CharacterResult input1 = provideCharacter("1");
        input1.setId(1);

        // When
        dbHelper.insertCharacter(input1);
        dbHelper.insertCharacter(input2);
        List<Integer> ids = new ArrayList<>();
        ids.add(input1.getId());
        ids.add(input2.getId());
        List<CharacterResult> result = dbHelper.fetchMultipleCharacter(ids);

        // Then
        assertEquals(2, result.size());
        assertTwoCharactersAreEqual(input1, result.get(0));//test the order and content
        assertTwoCharactersAreEqual(input2, result.get(1));
    }

    @Test
    public void testInsertAndFetchAllCharacters() {
        // Given
        CharacterResult input4 = provideCharacter("4");
        input4.setId(4);
        CharacterResult input2 = provideCharacter("2");
        input2.setId(2);
        CharacterResult input3 = provideCharacter("3");
        input3.setId(3);
        CharacterResult input1 = provideCharacter("1");
        input1.setId(1);

        // When
        dbHelper.insertCharacter(input1);
        dbHelper.insertCharacter(input2);
        dbHelper.insertCharacter(input3);
        dbHelper.insertCharacter(input4);
        AllCharacterResponse result = dbHelper.fetchAllCharacters(1, 20);

        // Then
        assertEquals(4, result.getResults().size());
        assertTwoCharactersAreEqual(input1, result.getResults().get(0));//test the order and content
        assertTwoCharactersAreEqual(input2, result.getResults().get(1));
        assertTwoCharactersAreEqual(input3, result.getResults().get(2));
        assertTwoCharactersAreEqual(input4, result.getResults().get(3));
    }

    @Test
    public void testInsertAndFetchSingleEpisode() {
        // Given
        EpisodeResult input = provideEpisode("1");

        // When
        dbHelper.insertEpisode(input);
        EpisodeResult result = dbHelper.fetchSingleEpisode(input.getId());

        // Then
        assertTwoEpisodesAreEqual(input, result);
    }

    @Test
    public void testInsertAndFetchSingleEpisodeWithEmptyCharactersList() {
        // Given
        EpisodeResult input = provideEpisodeWithoutCharacters();

        // When
        dbHelper.insertEpisode(input);
        EpisodeResult result = dbHelper.fetchSingleEpisode(input.getId());

        // Then
        assertNotNull(result.getCharacters());
        assertEquals(0, result.getCharacters().size());
        assertTwoEpisodesAreEqual(input, result);
    }

    @Test
    public void testInsertAndFetchMultipleEpisode() {
        // Given
        EpisodeResult input2 = provideEpisode("2");
        input2.setId(2);
        EpisodeResult input1 = provideEpisode("1");
        input1.setId(1);


        // When
        dbHelper.insertEpisode(input1);
        dbHelper.insertEpisode(input2);
        List<Integer> ids = new ArrayList<>();
        ids.add(input1.getId());
        ids.add(input2.getId());
        List<EpisodeResult> result = dbHelper.fetchMultipleEpisode(ids);

        // Then
        assertEquals(2, result.size());
        assertTwoEpisodesAreEqual(input1, result.get(0));
        assertTwoEpisodesAreEqual(input2, result.get(1));
    }

    @Test
    public void testInsertAndFetchAllEpisodes() {
        // Given
        EpisodeResult input1 = provideEpisode("1");
        input1.setId(1);
        EpisodeResult input2 = provideEpisode("2");
        input2.setId(2);
        EpisodeResult input3 = provideEpisode("3");
        input3.setId(3);
        EpisodeResult input4 = provideEpisode("4");
        input4.setId(4);

        // When
        dbHelper.insertEpisode(input1);
        dbHelper.insertEpisode(input2);
        dbHelper.insertEpisode(input3);
        dbHelper.insertEpisode(input4);
        AllEpisodeResponse result = dbHelper.fetchAllEpisodes(1, 20);

        // Then
        assertEquals(4, result.getResults().size());
        assertTwoEpisodesAreEqual(input1, result.getResults().get(0));
        assertTwoEpisodesAreEqual(input2, result.getResults().get(1));
        assertTwoEpisodesAreEqual(input3, result.getResults().get(2));
        assertTwoEpisodesAreEqual(input4, result.getResults().get(3));
    }

    @Test
    public void testInsertCharacter() {
        // Given
        CharacterResult input = provideCharacter("1");

        // When
        dbHelper.insertCharacter(input);

        // Then
        CharacterResult result = dbHelper.fetchSingleCharacter(input.getId());
        assertEquals(input.getId(), result.getId());
    }

    @Test
    public void testInsertCharacters() {
        // Given
        CharacterResult input1 = provideCharacter("1");
        input1.setId(1);
        CharacterResult input2 = provideCharacter("2");
        input2.setId(2);

        // When
        dbHelper.insertCharacter(input1);
        dbHelper.insertCharacter(input2);

        // Then
        AllCharacterResponse result = dbHelper.fetchAllCharacters(1, 20);
        assertEquals(2, result.getResults().size());
    }

    @Test
    public void testInsertAndFetchFileCache() {
        // Given
        FileCacheEntry input = provideFileCacheEntry();

        // When
        dbHelper.insertFileCache(input);
        List<FileCacheEntry> result = dbHelper.fetchFileCaches();

        // Then
        assertEquals(1, result.size());
        assertTwoFileCachesAreEqual(input, result.get(0));
    }

    @Test
    public void testRemoveFileCache() {
        // Given
        FileCacheEntry input = provideFileCacheEntry();
        dbHelper.insertFileCache(input);

        // When
        dbHelper.removeFileCache(input);

        // Then
        List<FileCacheEntry> result = dbHelper.fetchFileCaches();
        assertEquals(0, result.size());
    }


    @Test
    public void testInsertAndFetchFileCaches() {
        // Given
        FileCacheEntry input1 = provideFileCacheEntry();
        FileCacheEntry input2 = provideFileCacheEntry2();

        // When
        dbHelper.insertFileCache(input1);
        dbHelper.insertFileCache(input2);
        List<FileCacheEntry> result = dbHelper.fetchFileCaches();

        // Then
        assertEquals(2, result.size());
        assertTwoFileCachesAreEqual(input1, result.get(0));
        assertTwoFileCachesAreEqual(input2, result.get(1));
    }

    @Test
    public void testDeleteAllCharacters() {
        // Given
        CharacterResult input1 = provideCharacter("1");
        CharacterResult input2 = provideCharacter("2");
        dbHelper.insertCharacter(input1);
        dbHelper.insertCharacter(input2);

        // When
        dbHelper.deleteAllCharacters();

        // Then
        AllCharacterResponse result = dbHelper.fetchAllCharacters(1, 20);
        assertEquals(0, result.getResults().size());
    }

    @Test
    public void testDeleteAllEpisodes() {
        // Given
        CharacterResult input4 = provideCharacter("4");
        input4.setId(4);
        dbHelper.insertCharacter(input4);
        CharacterResult input1 = provideCharacter("1");
        input1.setId(1);
        dbHelper.insertCharacter(input1);
        CharacterResult input3 = provideCharacter("3");
        input3.setId(3);
        dbHelper.insertCharacter(input3);
        CharacterResult input2 = provideCharacter("2");
        input2.setId(2);
        dbHelper.insertCharacter(input2);

        // When
        dbHelper.deleteAllEpisodes();

        // Then
        AllEpisodeResponse result = dbHelper.fetchAllEpisodes(1, 20);
        assertEquals(0, result.getResults().size());
    }

    private CharacterResult provideCharacter(String dummyDataPostfix) {
        CharacterResult result = new CharacterResult();
        result.setId(new Random().nextInt());
        result.setName("A" + dummyDataPostfix);
        result.setCreated("B" + dummyDataPostfix);
        result.setUrl("C" + dummyDataPostfix);
        result.setGender("D" + dummyDataPostfix);
        result.setImage("E" + dummyDataPostfix);
        result.setSpecies("F" + dummyDataPostfix);
        result.setStatus("G" + dummyDataPostfix);
        result.setType("H" + dummyDataPostfix);
        Origin origin = new Origin();
        origin.setName("I" + dummyDataPostfix);
        origin.setUrl("J" + dummyDataPostfix);
        result.setOrigin(origin);
        Location location = new Location();
        location.setName("K" + dummyDataPostfix);
        location.setUrl("M" + dummyDataPostfix);
        result.setLocation(location);
        ArrayList<String> episodes = new ArrayList<>();
        episodes.add("N" + dummyDataPostfix);
        episodes.add("P" + dummyDataPostfix);
        result.setEpisode(episodes);
        return result;
    }

    private void assertTwoCharactersAreEqual(CharacterResult expected, CharacterResult actual) {
        assertEquals(expected.getCreated(), actual.getCreated());
        assertEquals(expected.getImage(), actual.getImage());
        assertEquals(expected.getGender(), actual.getGender());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getSpecies(), actual.getSpecies());
        assertEquals(expected.getStatus(), actual.getStatus());
        assertEquals(expected.getUrl(), actual.getUrl());
        assertEquals(expected.getType(), actual.getType());

        if (expected.getLocation() == null) {
            assertNull(actual.getLocation());
        } else {
            assertEquals(expected.getLocation().getName(), actual.getLocation().getName());
            assertEquals(expected.getLocation().getUrl(), actual.getLocation().getUrl());
        }

        if (expected.getOrigin() == null) {
            assertNull(actual.getOrigin());
        } else {
            assertEquals(expected.getOrigin().getName(), actual.getOrigin().getName());
            assertEquals(expected.getOrigin().getUrl(), actual.getOrigin().getUrl());
        }

        if (expected.getEpisode() == null) {
            assertNull(actual.getEpisode());
        } else {
            for (int i = 0; i < expected.getEpisode().size(); i++) {
                assertEquals(expected.getEpisode().get(i), actual.getEpisode().get(i));
            }
        }
    }

    private CharacterResult provideCharacterWithEmptyEpisodesList() {
        CharacterResult result = provideCharacter("1");
        result.setEpisode(new ArrayList<String>());
        return result;
    }


    private CharacterResult provideCharacterWithoutOrigin() {
        CharacterResult result = provideCharacter("1");
        result.setOrigin(null);
        return result;
    }

    private CharacterResult provideCharacterWithoutLocation() {
        CharacterResult result = provideCharacter("1");
        result.setLocation(null);
        return result;
    }

    private EpisodeResult provideEpisode(String dummyDataPostfix) {
        EpisodeResult episode = new EpisodeResult();
        episode.setId(new Random().nextInt());
        episode.setName("A" + dummyDataPostfix);
        episode.setAirDate("B" + dummyDataPostfix);
        episode.setCreated("C" + dummyDataPostfix);
        episode.setEpisode("D" + dummyDataPostfix);
        episode.setUrl("E" + dummyDataPostfix);
        ArrayList<String> characters = new ArrayList<>();
        characters.add("F" + dummyDataPostfix);
        characters.add("G" + dummyDataPostfix);
        episode.setCharacters(characters);
        return episode;
    }

    private void assertTwoEpisodesAreEqual(EpisodeResult expected, EpisodeResult actual) {
        assertEquals(expected.getAirDate(), actual.getAirDate());
        assertEquals(expected.getCreated(), actual.getCreated());
        assertEquals(expected.getEpisode(), actual.getEpisode());
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getUrl(), actual.getUrl());

        for (int i = 0; i < expected.getCharacters().size(); i++) {
            assertEquals(expected.getCharacters().get(i), actual.getCharacters().get(i));
        }

    }

    private EpisodeResult provideEpisodeWithoutCharacters() {
        EpisodeResult episode = provideEpisode("1");
        episode.setCharacters(new ArrayList<String>());
        return episode;
    }

    private FileCacheEntry provideFileCacheEntry2() {
        FileCacheEntry cacheEntry = new FileCacheEntry("XXXXXX", "YYYYY", 0);
        return cacheEntry;
    }

    private FileCacheEntry provideFileCacheEntry() {
        FileCacheEntry cacheEntry = new FileCacheEntry("", "", Long.MAX_VALUE);
        return cacheEntry;
    }

    private void assertTwoFileCachesAreEqual(FileCacheEntry expected, FileCacheEntry actual) {
        assertEquals(expected.getFileName(), actual.getFileName());
        assertEquals(expected.getUrl(), actual.getUrl());
        assertEquals(expected.getLastRetrievedTimeStamp(), actual.getLastRetrievedTimeStamp());
    }
}