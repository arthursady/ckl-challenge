package com.challenge.articlereader;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by arthur on 07/12/16.
 */

public class ArticleFragment extends Fragment {


    private Unbinder unbinder;
    private Article mArticle;


    /*The newInstance method initializes the mArticle passing this way all the content needed*/
    public static ArticleFragment newInstance(Article article) {
        final ArticleFragment fragment = new ArticleFragment();
        fragment.mArticle=article;
        return fragment;
    }

    public ArticleFragment() {
    }


    /*The onCreateView method binds the mArticle contents to the views of the article_details
     layout*/
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.article_details, container, false);


        unbinder = ButterKnife.bind(this,view);

        /*Creates and binds a view for each element of the layout using ButterKnife*/
        TextView titleTextView = ButterKnife.findById(view,R.id.title);
        TextView tagsTextView = ButterKnife.findById(view,R.id.tags_detail);
        ImageView imageView = ButterKnife.findById(view,R.id.image_details);
        TextView authorsTextView = ButterKnife.findById(view,R.id.authors_detail);
        TextView websiteTextView = ButterKnife.findById(view,R.id.website_detail);
        TextView contentTextView = ButterKnife.findById(view,R.id.content);
        TextView dateTextView = ButterKnife.findById(view,R.id.date_detail);


        /*If the article has no image, a default placeHolder image is assigned for that Article*/
        if((mArticle.getImage()!=null) && !(mArticle.getImage().equals("null"))){
            imageView.setImageResource(getResources().getIdentifier(mArticle.getImage(),
                    "drawable",getActivity().getPackageName()));
        }
        else{
            imageView.setImageResource(getResources().getIdentifier("placeholder",
                    "drawable",getActivity().getPackageName()));
        }

        /*For styling purposes, the list of tags in the article is organized in a string with
        * hashtags preceding each tag*/
        String tags="";
        for (int i=0; i<mArticle.getTags().size();i++){
            tags= tags +"#"+mArticle.getTags().get(0).toString()+" ";
        }

        /*Sets the contents of the views*/
        tagsTextView.setText(tags);
        titleTextView.setText(mArticle.getTitle());
        authorsTextView.setText(mArticle.getAuthors());
        websiteTextView.setText(mArticle.getWebsite());
        contentTextView.setText(mArticle.getContent());

        /*For the date format in order to preserve the same date format in the url it is necessary
        * to set it when passing the string*/
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        dateTextView.setText(dateFormat.format(mArticle.getDate()));
        return view;
    }

    /*Unbinds what is needed when the View is destroyed*/
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        unbinder.unbind();
    };

}
