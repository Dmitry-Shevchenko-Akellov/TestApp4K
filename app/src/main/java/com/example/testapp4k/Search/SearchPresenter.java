package com.example.testapp4k.Search;

import android.os.Handler;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.testapp4k.Base.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import static com.example.testapp4k.Base.AppController.TAG;

public class SearchPresenter implements SearchPresenterInt {

    private SearchViewInt searchViewInt;
    private JSONArray[] jArr = new JSONArray[1];
    private ArrayList<String> repos_Photo, repos_Name, repos_Descr, repos_Forks;
    private int pages = 0;

    @Override
    public void attachView(SearchViewInt view) {
        searchViewInt = view;
    }

    @Override
    public void detachView() {
        searchViewInt = null ;
    }

    @Override
    public void searchRepository(String searchQuery, int page) {
        String newQuery = null;
        try {
            newQuery = URLEncoder.encode(searchQuery, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (searchQuery.equals("")) {
            searchViewInt.errorSearch(true);
        }
        else if (searchQuery.length()<3){
            searchViewInt.errorSearch(false);
        }
        else {
            String tag_string_req = "req_login";
            searchViewInt.setProgressVisibility(true);
            searchViewInt.setBackgroundVisibility(true);
            final StringRequest strReq = new StringRequest(Request.Method.GET,
                    ("https://api.github.com/search/repositories?q=" + newQuery + "&page=" + page), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jObj = new JSONObject(response);
                        if (!jObj.getBoolean("incomplete_results")) {
                            double reposCount = jObj.getInt("total_count");
                            if (reposCount==0) {
                                searchViewInt.setNoResult();
                            }
                            pages = (int)Math.ceil(reposCount / 30);
                            repos_Name = new ArrayList<>();
                            repos_Descr = new ArrayList<>();
                            repos_Forks = new ArrayList<>();
                            repos_Photo = new ArrayList<>();
                            jArr[0] = jObj.getJSONArray("items");
                            Log.d("DEBUG = ", "write data");
                            Log.d("REFACTOR == ", "Search  count: " + pages);
                            for (int x = 0; x < jArr[0].length(); x++) {
                                JSONObject j = jArr[0].getJSONObject(x);
                                JSONObject image = j.getJSONObject("owner");
                                repos_Name.add(j.getString("name"));
                                repos_Descr.add(j.getString("description"));
                                repos_Forks.add(j.getString("forks"));
                                repos_Photo.add(image.getString("avatar_url"));
                            }
                            Log.d("DEBUG = ", "ShowRep");
                            searchViewInt.showRepository(repos_Name, repos_Photo, repos_Descr, repos_Forks);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    searchViewInt.refreshButton(pages);
                                    searchViewInt.setProgressVisibility(false);
                                }
                            },1000);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "Search data Response: " + response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Search Error: " + error.getMessage());
                    if (error.getMessage() == null) {
                        searchViewInt.errorNull();
                    }
                }
            });
            Log.d(TAG, "QUERY: " + strReq);
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
    }
}
