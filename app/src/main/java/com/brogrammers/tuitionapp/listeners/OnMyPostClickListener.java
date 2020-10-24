package com.brogrammers.tuitionapp.listeners;

public interface OnMyPostClickListener<T> {
    void onItemSelected(T t);
    void onDelete(T t);
    void onUpdate(T t);
}
