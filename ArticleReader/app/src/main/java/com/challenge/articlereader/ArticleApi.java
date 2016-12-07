package com.challenge.articlereader;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by arthur on 07/12/16.
 */

public interface ArticleApi {
    @GET("/challenge")
    Call<ArrayList<Article>> retrieveArticles();
}
