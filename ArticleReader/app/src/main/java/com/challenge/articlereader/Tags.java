package com.challenge.articlereader;

import io.realm.RealmObject;

/**
 * Created by arthur on 07/12/16.
 */

public class Tags extends RealmObject {

    /*JSON model converted*/
    private Integer id;
    private String label;

    public Tags(){
    }

    /*############################################################################################*/
    /*Getters*/
    public Integer getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    /*############################################################################################*/
    /*Setters*/

    public void setId(Integer id) {
        this.id = id;
    }

    public void setLabel(String label) {this.label = label;}

    
    @Override
    public String toString() {
        return label;
    }
}
