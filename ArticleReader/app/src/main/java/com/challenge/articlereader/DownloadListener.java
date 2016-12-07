package com.challenge.articlereader;

import java.util.ArrayList;

/**
 * Created by arthur on 07/12/16.
 */

public interface DownloadListener {
    void downloadComplete(ArrayList<Article> articles);
}
