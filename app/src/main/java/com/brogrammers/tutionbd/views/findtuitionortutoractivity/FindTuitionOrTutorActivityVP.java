package com.brogrammers.tutionbd.views.findtuitionortutoractivity;

import android.content.Intent;

import com.brogrammers.tutionbd.BasePresenter;
import com.brogrammers.tutionbd.BaseView;
import com.brogrammers.tutionbd.beans.HighlightItem;

import java.util.List;

public interface FindTuitionOrTutorActivityVP {
    interface View extends BaseView<Presenter>{
        void onSetupProfileUi();
        void showSnackBarMessage(String message);
        void navigateToActivity(Intent intent);
        void onShowHighlightedItems(List<HighlightItem> items);
        void onShowUpdateDialogBox(String tittle, String buttonName, String childKey);
        void onLoadingDialog(boolean isLoading);
        void onCheckLocationStatus();
    }

    interface Presenter extends BasePresenter{
        void onPostButtonClick();
        void onHighlightItemClicked(HighlightItem highlightItem);
        void onAddOrUpdateHighlightedItem(String value, String childKey);
    }
}
