package com.example.testapp4k.Search;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapp4k.Base.BaseActivity;
import com.example.testapp4k.R;

import java.util.ArrayList;

public class SaerchActivity extends BaseActivity implements SearchViewInt, View.OnClickListener {

    private SearchPresenterInt searchPresenter;

    private ProgressBar progressBar;
    private ImageView searchButton, backgroundTransparent;
    private ImageButton nextButton, prevButton;
    private EditText searchQuery;
    private TextView waiting, noResult;
    private GridView searchResult;
    private int page=0, pages=0;
    private String saveQuery;
    RepositoryListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        searchPresenter = new SearchPresenter();
        searchPresenter.attachView(this);

        searchButton = (ImageView)findViewById(R.id.search_button);
        nextButton = (ImageButton)findViewById(R.id.fabNext);
        prevButton = (ImageButton)findViewById(R.id.fabPrev);
        searchQuery = (EditText)findViewById(R.id.search_query);
        searchResult = (GridView)findViewById(R.id.search_list);

        searchButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);

        noResult =findViewById(R.id.noResult);
        waiting = findViewById(R.id.waiting);
        backgroundTransparent = findViewById(R.id.transparent_background);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        refreshButton(pages);
        setNoResult();
    }

    @Override
    public void setNoResult() {
        noResult.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_button:
                noResult.setVisibility(View.GONE);
                nextButton.setClickable(false);
                prevButton.setClickable(false);
                searchButton.setEnabled(false);
                searchResult.setEnabled(false);
                saveQuery = searchQuery.getText().toString().trim();
                page = 1;
                searchPresenter.searchRepository(searchQuery.getText().toString().trim(), page);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        searchButton.setEnabled(true);
                    }
                },2000);
                break;
            case R.id.fabPrev:
                if (page==1){

                }
                else {
                    page--;
                    noResult.setVisibility(View.GONE);
                    searchQuery.setText(saveQuery);
                    nextButton.setClickable(false);
                    prevButton.setClickable(false);
                    searchButton.setEnabled(false);
                    searchResult.setEnabled(false);
                    searchPresenter.searchRepository(saveQuery, page);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            searchButton.setEnabled(true);
                        }
                    },2000);
                }
                break;
            case R.id.fabNext:
                if (page<=pages||page!=0){
                    page++;
                    noResult.setVisibility(View.GONE);
                    searchQuery.setText(saveQuery);
                    nextButton.setClickable(false);
                    prevButton.setClickable(false);
                    searchButton.setEnabled(false);
                    searchResult.setEnabled(false);
                    searchPresenter.searchRepository(saveQuery, page);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            searchButton.setEnabled(true);
                        }
                    },2000);
                }
                else {
                }
                break;
        }
    }

    @Override
    public void errorNull() {
        searchPresenter.searchRepository(saveQuery, page);
    }

    @Override
    public void refreshButton(int pagesCount) {
        setBackgroundVisibility(false);
        searchResult.setEnabled(true);
        if (page==0) {
            searchButton.setEnabled(true);
            nextButton.setVisibility(View.GONE);
            prevButton.setVisibility(View.GONE);
        }

        if (page==1) {
            if(pagesCount==0) {
                searchButton.setEnabled(true);
                nextButton.setVisibility(View.GONE);
                prevButton.setVisibility(View.GONE);
            }
            if(pagesCount>1) {
                searchButton.setEnabled(true);
                nextButton.setClickable(true);
                nextButton.setVisibility(View.VISIBLE);
                prevButton.setVisibility(View.GONE);
            }
            if (pagesCount==1) {
                searchButton.setEnabled(true);
                nextButton.setVisibility(View.GONE);
                prevButton.setVisibility(View.GONE);
            }
        }
        if (page>1) {
            if (page==pagesCount) {
                searchButton.setEnabled(true);
                prevButton.setClickable(true);
                nextButton.setVisibility(View.GONE);
                prevButton.setVisibility(View.VISIBLE);
            }
        }
        if (page!=1&page!=pagesCount) {
            searchButton.setEnabled(true);
            nextButton.setClickable(true);
            prevButton.setClickable(true);
            nextButton.setVisibility(View.VISIBLE);
            prevButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setProgressVisibility(boolean visibility) {
        if (visibility){
            waiting.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }
        else{
            waiting.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void setBackgroundVisibility(boolean visibility) {
        if (visibility)
            backgroundTransparent.setVisibility(View.VISIBLE);
        else
            backgroundTransparent.setVisibility(View.GONE);
    }

    @Override
    public void showRepository(ArrayList<String> repos_Name, ArrayList<String> repos_Photo,
                               ArrayList<String> repos_Descr, ArrayList<String> repos_Forks) {
        adapter = new RepositoryListAdapter(getContext(),R.layout.list_item_search, repos_Name, repos_Photo, repos_Descr, repos_Forks);
        searchResult.setAdapter(adapter);
        Log.d("DEBUG = ", "setAdapter");
    }

    @Override
    public void errorSearch(boolean error) {
        searchButton.setClickable(true);
        if (error)
            Toast.makeText(getContext(), "Error! Please enter a query.", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getContext(), "Error! Length < 3!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        searchPresenter.detachView();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public Context getContext() {
        return this;
    }

}
