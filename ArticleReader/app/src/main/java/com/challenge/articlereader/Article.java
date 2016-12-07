package com.challenge.articlereader;

import java.util.Comparator;
import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by arthur on 07/12/16.
 */

public class Article extends RealmObject {

    /*State control variable*/
    private boolean read=false;

    /*JSON model converted variables*/
    @PrimaryKey
    private String title;

    private String website;
    private String authors;
    private Date date;
    private String content;
    private RealmList<Tags> tags = new RealmList<Tags>();
    private String image;

    public Article(){
    }


    /*##########################################################################################*/
    /*Getters*/
    public boolean getReadState(){return read; }

    public String getTitle(){
        return title;
    }

    public String getAuthors(){
        return authors;
    }

    public String getWebsite(){
        return website;
    }

    public Date getDate(){
        return date;
    }

    public String getContent(){return content;}

    public RealmList<Tags> getTags(){return tags;}

    public String getImage() {
        return image;
    }

    /*##########################################################################################*/
    /*Setters*/
    public void setRead(boolean ReadState){read=ReadState;}

    public void setTitle(String newTitle){
        title= newTitle;
    }

    public void setAuthors(String newAuthors){
        authors= newAuthors;
    }

    public void setWebsite(String newWebsite){
        website= newWebsite;
    }

    public void setDate(Date newDate){
        date= newDate;
    }

    public void setContent(String Content){content=Content;}

    public void setTags(RealmList<Tags> Tags){tags=Tags;}

    public void setImage(String newImage){image=newImage;}

    /*##########################################################################################*/
    /*Sorting method comparators*/
    public static Comparator<Article> DateComparator = new Comparator<Article>(){
        @Override
        public int compare(Article A1, Article A2) {
            return A1.getDate().compareTo(A2.getDate());
        }
    };

    public static Comparator<Article> TitleComparator = new Comparator<Article>(){
        @Override
        public int compare(Article A1, Article A2) {
            return A1.getTitle().compareTo(A2.getTitle());
        }
    };

    public static Comparator<Article> AuthorComparator = new Comparator<Article>(){
        @Override
        public int compare(Article A1, Article A2) {
            return A1.getAuthors().compareTo(A2.getAuthors());
        }
    };

    public static Comparator<Article> WebsiteComparator = new Comparator<Article>(){
        @Override
        public int compare(Article A1, Article A2) {
            return A1.getWebsite().compareTo(A2.getWebsite());
        }
    };
}
