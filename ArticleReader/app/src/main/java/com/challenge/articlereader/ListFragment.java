package com.challenge.articlereader;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.app.Activity;

import java.util.ArrayList;
import java.util.Collections;


import static com.challenge.articlereader.R.layout.article_list;

/**
 * Created by arthur on 07/12/16.
 */

public class ListFragment extends Fragment {

    /*List of articles that will be used to be displayed and manipulated*/
    private ArrayList<Article> mArticleList = new ArrayList<Article>();

    /*The new instance of the ListFragment receives an article List that will be used to init
    * the article*/
    public static ListFragment newInstance(ArrayList<Article> articles) {
        final ListFragment fragment = new ListFragment();
        fragment.setArticleList(articles);
        return fragment;
    }

    public ListFragment() {
    }

    /*Setter method in case there is a necessity to change the list during operation*/
    public void setArticleList(ArrayList<Article> articles) {
        mArticleList=articles;
    }

    /*Method that sorts the mArticleList based on the four comparators created in the
    Article.class Date(1),Title(1),Authors(1),Website(4).*/
    public void sortList(int Method){
        switch (Method){
            case 1:
                Collections.sort(mArticleList,Article.DateComparator);
                return;
            case 2:
                Collections.sort(mArticleList,Article.TitleComparator);
                return;
            case 3:
                Collections.sort(mArticleList,Article.AuthorComparator);
                return;
            case 4:
                Collections.sort(mArticleList,Article.WebsiteComparator);
                return;
        }
    }

    /*Sets a RecyclerView for the ArticleList using the ArticleAdapter*/
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(article_list, container, false);
        final Activity activity = getActivity();
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(new ArticleAdapter(activity,mArticleList));
        return recyclerView;
    }

}