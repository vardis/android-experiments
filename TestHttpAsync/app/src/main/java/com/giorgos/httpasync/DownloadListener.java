package com.giorgos.httpasync;

/**
 * Created by giorgos on 06/04/14.
 */
public interface DownloadListener<Progress, Result> {
    void onProgressUpdate(Progress p);

    void onDownloadComplete(Result res);
}
