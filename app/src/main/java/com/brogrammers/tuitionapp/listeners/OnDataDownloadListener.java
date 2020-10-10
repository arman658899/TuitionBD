package com.brogrammers.tuitionapp.listeners;

public interface OnDataDownloadListener<T> {
    void onStarted();
    void onFinished();
    void onDownloading(T t);
    void onError();
}
