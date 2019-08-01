package com.example.testapp4k.Search;

import com.example.testapp4k.Base.BasePresenter;

public interface SearchPresenterInt extends BasePresenter<SearchViewInt> {
    void searchRepository(String searchQuery, int page);
}
