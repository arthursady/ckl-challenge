package com.challenge.articlereader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by arthur on 07/12/16.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private LayoutInflater mLayoutInflater;
    private ArrayList<Article> mArticleList;
    private Interface mListener;
    private Context mContext;

    /*Define the read state as true and false for unread, however since "getItemView is an int
    method booleans couldn't be used"*/
    private final int Read = 1;
    private final int Unread=0;

    /*The adapter receives the Article list in order to populate the elements in the
    * RecyclerView created by the ListFragment*/
    public ArticleAdapter(Context context, ArrayList<Article> List) {
        mLayoutInflater = LayoutInflater.from(context);
        mArticleList=List;
        mContext=context;
    }

    /*Gets the viewType based in the Read state variable of the Article*/
    @Override
    public int getItemViewType(int position) {
        if (mArticleList.get(position).getReadState()) {
            return Read;
        } else  {
            return Unread;
        }
    }

    @Override
    public int getItemCount() {
        return mArticleList.size();
    }
    

    /*Creates the viewHolders based in the read state. If the article is read the layout used
    *is different(Still not implemented, needs a new layout)*/
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View itemView = mLayoutInflater
                .inflate(R.layout.list_element, viewGroup, false);
        switch (viewType) {
            case Read:
                itemView  = mLayoutInflater.inflate(R.layout.list_element, viewGroup, false);
                break;
            case Unread:
                itemView = mLayoutInflater.inflate(R.layout.list_element, viewGroup, false);
                break;
        }
        return  new ViewHolder(itemView);
    }

    /*#########################         ViewHolder Bind           ########################*/
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {

        /*invokes the setData method to populate the list elements*/
        viewHolder.setData(mArticleList.get(position));

        /*Create a listener for normal clicks on the items of the list in order to open the
        * clicked article*/
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onArticleClicked(mArticleList.get(viewHolder.getAdapterPosition()));
            }
        });

        /*Create a listener for longClicks on the elements in order to provide the mark as read
        * or mark as unread functionallity*/
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mListener.onArticleSelected(mArticleList.get(viewHolder.getAdapterPosition()),
                        v,
                        mContext);
                return true;
            }
        });
    }

    /*#########################         ViewHolder Definition           ########################*/
    /*Uses ButterKnife to bind the Articles from the List to the item view layout for each element
    * in the article list that will be displayed*/
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Views
        @BindView(R.id.title)
        TextView mTitleTextView;
        @BindView(R.id.authors) TextView mAuthorsTextView;
        @BindView(R.id.website) TextView mWebsiteTextView;
        @BindView(R.id.date) TextView mDateTextView;
        @BindView(R.id.list_image) ImageView mImageView;

        /*Use butterknife to Bind the views*/
        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        /*Sets the articles information in the list elements*/
        private void setData(Article article) {
            mTitleTextView.setText(mContext.getString(R.string.title_tag) + article.getTitle());
            mAuthorsTextView.setText(mContext.getString(R.string.authors_tag)+article.getAuthors());
            mWebsiteTextView.setText(mContext.getString(R.string.website_tag)+article.getWebsite());

            /*Sets the Date format to match the same format in the URL for the articles*/
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            mDateTextView.setText(mContext.getString(R.string.date_tag) +
                    dateFormat.format(article.getDate()));

            /*If the article has an image it searches for it and sets the imageview o the
            * list elemente, otherwise it sets the default placeHolder*/
            if((article.getImage()!=null) && !(article.getImage().equals("null"))){
                mImageView.setImageResource( mContext.getResources().getIdentifier(article.getImage(),
                        "drawable",mContext.getPackageName()));
            }
            else{
                mImageView.setImageResource(mContext.getResources().getIdentifier("placeholder"
                        ,"drawable",mContext.getPackageName()));
            }
        }
    }

    /*############################      Interface      ##################################*/
    /*Interface for the two click listeners created to be inplemented in the MainActivity*/
    public interface Interface {
        void onArticleClicked(Article article);
        void onArticleSelected(Article article,View v,Context context);
    }

}
