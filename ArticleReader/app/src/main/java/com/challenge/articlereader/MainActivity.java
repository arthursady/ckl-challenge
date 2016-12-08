package com.challenge.articlereader;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
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

public class MainActivity extends AppCompatActivity implements ArticleAdapter.Interface,
        DownloadListener {

    Realm mRealm;
    ProgressDialog mProgressDialog;
    ListFragment mListFragment;

    /*Set the values for the sort methods identifiers*/
    private final int mSortDate=1;
    private final int mSortTitle=2;
    private final int mSortAuthor=3;
    private final int mSortWebsite=4;


    /*Initializes the DB, checks for connection and fetches the URL articles and show the article
    list or if offline shows the article list already in the DB*/
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

            /*When there are articles in the DB even in offline mode displays the list and enables
            * reading*/
            ArrayList<Article>dbList=new ArrayList<Article>();
            RealmResults realmResults=mRealm.where(Article.class).findAll();
            if(realmResults.size()>0){
                dbList.addAll(mRealm.where(Article.class).findAll().subList(0,realmResults.size()));
                showListFragment(dbList);
            }
        }
    }


    /*Closes the dataBase when destroing the fragment*/
    @Override
    public void onDestroy(){
        super.onDestroy();
        mRealm.close();
        mProgressDialog.dismiss();
    }


    /*########################         Tool Bar menu listener         ###########################*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            /*For each case, the list in the ListFragment is re-sorted and the view updated by
            * detaching and retaching the Fragment*/
            case R.id.byDate:
                mListFragment.sortList(mSortDate);
                getSupportFragmentManager().beginTransaction().detach(mListFragment).commit();
                getSupportFragmentManager().beginTransaction().attach(mListFragment).commit();
                return true;

            case R.id.byTitle:
                mListFragment.sortList(mSortTitle);
                getSupportFragmentManager().beginTransaction().detach(mListFragment).commit();
                getSupportFragmentManager().beginTransaction().attach(mListFragment).commit();
                return true;

            case R.id.byAuthor:
                mListFragment.sortList(mSortAuthor);
                getSupportFragmentManager().beginTransaction().detach(mListFragment).commit();
                getSupportFragmentManager().beginTransaction().attach(mListFragment).commit();
                return true;

            case R.id.byWebsite:
                mListFragment.sortList(mSortWebsite);
                getSupportFragmentManager().beginTransaction().detach(mListFragment).commit();
                getSupportFragmentManager().beginTransaction().attach(mListFragment).commit();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    /*##############################            Interfaces         ###############################*/
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

        ArrayList<Article>dbList=new ArrayList<Article>();
        RealmResults realmResults=mRealm.where(Article.class).findAll();
        dbList.addAll(mRealm.where(Article.class).findAll().subList(0,realmResults.size()));
        showListFragment(dbList);
        if (mProgressDialog != null) {
            mProgressDialog.hide();
        }
    }

    /*When the article in the list is clicked, an articles detail fragment is replaced in the
    * screen */
    @Override
    public void onArticleClicked(Article article) {
        mRealm.beginTransaction();
        article.setRead(true);
        mRealm.commitTransaction();

        final ArticleFragment detailsFragment =
                ArticleFragment.newInstance(article);
        ScreenUtility screen = new ScreenUtility(this);
        if(screen.getScreenSize() < 7){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.root_layout, detailsFragment, "articleDetails")
                    .addToBackStack(null)
                    .commit();
        }else{
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.root_layout,detailsFragment , "articleDetails")
                    .commit();
            getSupportFragmentManager().beginTransaction().detach(mListFragment).commit();
            getSupportFragmentManager().beginTransaction().attach(mListFragment).commit();
        }
    }
    /*When the article is LongClicked, it opens a popup menu that changes depending on the read
    * status of the List Item*/
    @Override
    public void onArticleSelected(final Article article, final View v, final Context context){
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(context, v);
        //Inflating the Popup using xml file
        if(article.getReadState()) {
            popup.getMenuInflater().inflate(R.menu.popup_menu_read, popup.getMenu());
        }
        else{
            popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        }

        popup.show();//showing popup menu

        /*Set listener for the button of the popup menu and toggles the read state when clicked*/
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(context,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                if(article.getReadState()) {
                    mRealm.beginTransaction();
                    article.setRead(false);
                    mRealm.commitTransaction();
                }
                else{
                    mRealm.beginTransaction();
                    article.setRead(true);
                    mRealm.commitTransaction();
                }

                getSupportFragmentManager().beginTransaction().detach(mListFragment).commit();
                getSupportFragmentManager().beginTransaction().attach(mListFragment).commit();

                return true;
            }
        });
    }

    /*#############################         Utility Methods        ###############################*/
    /*Shows the ListFragment in the activity*/
    private void showListFragment(ArrayList<Article> articles) {
        mListFragment = ListFragment.newInstance(articles);
        /*Checks the size of the screen in inches and for screens greater than 7 -> tablets set
        * a different layout for the fragments*/
        ScreenUtility screen = new ScreenUtility(this);
        if(screen.getScreenSize() > 7){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.list_container_largescreens,mListFragment , "articleList")
                    .commit();

        }else{
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.root_layout,mListFragment , "articleList")
                    .commit();
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
