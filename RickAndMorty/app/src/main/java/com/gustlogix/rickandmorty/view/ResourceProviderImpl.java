package com.gustlogix.rickandmorty.view;

import android.content.Context;

public class ResourceProviderImpl implements ResourceProvider {

    Context context;

    public ResourceProviderImpl(Context context) {
        this.context = context;
    }

    public String getString(int id) {
        return context.getString(id);
    }
}
