package com.brogrammers.tutionbd.listeners;

public interface OnDataDownloadListener<T> {
    void onStarted();
    void onFinished();
    void onDownloading(T t);
    void onError();
}
