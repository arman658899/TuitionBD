package com.brogrammers.tuitionapp.listeners;

import java.util.List;

public interface OnMultipleDownloadListener<T> {
    void onStarted();
    void onFinished();
    void onDownloaded(List<T> list);
    void onError();
}
