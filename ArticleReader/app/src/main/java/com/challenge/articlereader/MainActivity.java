package com.challenge.articlereader;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements DownloadListener {

    Realm mRealm;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Realm.init(this);
        mRealm=Realm.getDefaultInstance();

        /*Shows a loading dialog during the download if there is internet*/
        if (isNetworkConnected()) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();

            makeRetrofitCalls();
        }
        /*Otherwise shows a dialog informing the lack of connectivity with an ok button.*/
        else {
            new AlertDialog.Builder(this)
                    .setTitle("No Internet Connection")
                    .setMessage("It looks like your internet connection is off. "+
                            "Couldn't sync for new articles")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which) {}
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert).show();
        }
    }



    /*Check the downloaded info with the local DB and insert the new items*/
    @Override
    public void downloadComplete(ArrayList<Article> articles) {
        RealmResults dbArticles =mRealm.where(Article.class).findAll();

        if(dbArticles.size()>0){
            for(int i=0;i<articles.size();i++) {
                if (mRealm.where(Article.class).equalTo("title", articles.get(i)
                        .getTitle()).findFirst() == null) {

                    mRealm.where(Article.class).equalTo("title", articles.get(i)
                            .getTitle()).findFirst();
                }
            }
        }
        else{
            for(int i=0;i<articles.size();i++) {
                mRealm.beginTransaction();
                mRealm.copyToRealmOrUpdate(articles.get(i));
                mRealm.commitTransaction();
            }
        }
    }


    /*Method to verify internet connection*/
    private boolean isNetworkConnected() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    /*Method responsible for the retrofit operation and conversion from the JSON model to the
    * Article model created*/
    private void makeRetrofitCalls() {

        /*Definition of the Date format in order to make the converter automatically identify
        * the dates as Date objects instead of normal Strings*/
        Gson gson = new GsonBuilder()
                .setDateFormat("MM/dd/yyyy")
                .create();

        /*Using default GsonConverterFactory*/
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.ckl.io")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ArticleApi ArticleAPI = retrofit.create(ArticleApi.class);

        Call<ArrayList<Article>> call = ArticleAPI.retrieveArticles();

        call.enqueue(new Callback<ArrayList<Article>>() {
            @Override
            public void onResponse(Call<ArrayList<Article>> call,
                                   Response<ArrayList<Article>> response) {
                downloadComplete(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Article>> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }
}
