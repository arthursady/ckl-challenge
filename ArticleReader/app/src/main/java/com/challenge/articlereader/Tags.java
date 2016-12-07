package com.challenge.articlereader;

import io.realm.RealmObject;

/**
 * Created by arthur on 07/12/16.
 */

public class Tags extends RealmObject {
    private Integer id;
    private String label;

    public Tags(){
    }
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label The label
     */
    public void setLabel(String label) {
        this.label = label;

    }
    @Override
    public String toString() {
        return label;
    }
}
