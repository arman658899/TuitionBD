package com.brogrammers.tutionbd.managers;

import android.content.Context;

import com.brogrammers.tutionbd.ApplicationHelper;
import com.brogrammers.tutionbd.DatabaseHelper;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseListenerManager {

    Map<Context, List<ListenerRegistration>> activeListeners = new HashMap<>();

    void addListenerToMap(Context context, ListenerRegistration valueEventListener) {
        if (activeListeners.containsKey(context)) {
            activeListeners.get(context).add(valueEventListener);
        } else {
            List<ListenerRegistration> valueEventListeners = new ArrayList<>();
            valueEventListeners.add(valueEventListener);
            activeListeners.put(context, valueEventListeners);
        }
    }

    public void closeListeners(Context context) {
        DatabaseHelper databaseHelper = ApplicationHelper.getDatabaseHelper();
        if (activeListeners.containsKey(context)) {
            for (ListenerRegistration listener : activeListeners.get(context)) {
                listener.remove();
            }
            activeListeners.remove(context);
        }
    }
}
