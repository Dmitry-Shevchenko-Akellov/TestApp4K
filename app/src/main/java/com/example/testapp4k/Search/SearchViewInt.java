package com.example.testapp4k.Search;

import com.example.testapp4k.Base.BaseView;

import java.util.ArrayList;

public interface SearchViewInt extends BaseView {

    void setProgressVisibility(boolean visibility);

    void setBackgroundVisibility(boolean visibility);

    void showRepository(ArrayList<String> repos_Name, ArrayList<String> repos_Photo,
                        ArrayList<String> repos_Descr, ArrayList<String> repos_Forks);

    void errorSearch(boolean error);

    void setNoResult();

    void errorNull();

    void refreshButton(int pagesCount);
}
