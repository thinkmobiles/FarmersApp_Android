package com.farmers.underground.ui.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.farmers.underground.FarmersApp;
import com.farmers.underground.config.ProjectConstants;
import com.farmers.underground.remote.models.SearchHint;
import com.farmers.underground.ui.models.SearchHintCollection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

/**
 * Created by omar on 9/30/15.
 */
public class SharedPrefHelper {

    public static final String SEARCH_HINTS = ProjectConstants.KEY_CURRENT_USER_SEARCH_HINTS;

    public static void saveSearchHint(Context context, SearchHint query) {
        Gson gson = new GsonBuilder().create();
        SearchHintCollection hint;
        String hintsJson = getPrivatePrefs(context).getString(SEARCH_HINTS, new String());
        if (!hintsJson.isEmpty()) hint = gson.fromJson(hintsJson, SearchHintCollection.class);
        else hint = new SearchHintCollection();
        hint.add(query);
        hintsJson = gson.toJson(hint);
        SharedPreferences.Editor editor = getPrivatePrefs(context).edit();
        editor.putString(SEARCH_HINTS, hintsJson);
        editor.apply();
    }

    public static List<SearchHint> getSearchHints(Context context) {
        SearchHintCollection hint;
        Gson gson = new GsonBuilder().create();
        String hintsJson = getPrivatePrefs(context).getString(SEARCH_HINTS, new String());
        if (!hintsJson.isEmpty()) hint = gson.fromJson(hintsJson, SearchHintCollection.class);
        else hint = new SearchHintCollection();
        return hint.getHintsList();
    }


    private static SharedPreferences getPrivatePrefs(Context context) {
        return FarmersApp.getUsrPreferences();
        //return context.getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
    }
}
