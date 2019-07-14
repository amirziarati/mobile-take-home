package com.gustlogix.rickandmorty.repo;

import com.gustlogix.rickandmorty.dto.Response;

public interface RepositoryCallback<T> {

    void onSuccess(Response<T> response);

    void onError(Exception e);
}
