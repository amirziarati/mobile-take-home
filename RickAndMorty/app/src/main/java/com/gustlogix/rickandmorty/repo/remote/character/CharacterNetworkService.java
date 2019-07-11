package com.gustlogix.rickandmorty.repo.remote.character;


import com.gustlogix.rickandmorty.dto.CharacterResponse;
import com.gustlogix.rickandmorty.repo.remote.network.AbsNetworkService;

public class CharacterNetworkService extends AbsNetworkService<CharacterResponse> {

    public CharacterNetworkService(CharacterJsonDeserializer jsonDeserializer) {
        super(jsonDeserializer, CharacterResponse.class);
    }
}
