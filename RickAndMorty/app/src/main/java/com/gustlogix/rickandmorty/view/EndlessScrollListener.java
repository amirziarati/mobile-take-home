package com.gustlogix.rickandmorty.view;

import android.widget.AbsListView;

public abstract class EndlessScrollListener implements AbsListView.OnScrollListener {
    private int previousItemCount = 0;
    private int currentPage = 0;
    private int threshold = 5;
    private boolean isLoading = true;
    private int startPage = 0;

    protected EndlessScrollListener(int threshold, int startPage) {
        this.threshold = threshold;
        this.startPage = startPage;
        this.currentPage = startPage;
    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
        if (totalItemCount < previousItemCount) {
            this.currentPage = this.startPage;
            this.previousItemCount = totalItemCount;
            if (totalItemCount == 0) { this.isLoading = true; }
        }
        if (isLoading && (totalItemCount > previousItemCount)) {
            isLoading = false;
            previousItemCount = totalItemCount;
            currentPage++;
        }
        if (!isLoading && (firstVisibleItem + visibleItemCount + threshold) >= totalItemCount ) {
            isLoading = onLoadNextPage(currentPage + 1, totalItemCount);
        }
    }

    public abstract boolean onLoadNextPage(int page, int totalItemsCount);

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }
}
