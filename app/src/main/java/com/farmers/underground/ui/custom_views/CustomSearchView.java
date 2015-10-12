package com.farmers.underground.ui.custom_views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.appcompat.R;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by omar
 * on 9/30/15.
 */
public class CustomSearchView extends SearchView implements View.OnClickListener {

    LinearLayout llSearchBar = (LinearLayout) this.findViewById(R.id.search_bar);
    SearchAutoComplete searchEditArea;


    public CustomSearchView(Context context) {
        super(context);
        reverseOrderEditArea(context);
    }

    public CustomSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        reverseOrderEditArea(context);
    }

    public CustomSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        reverseOrderEditArea(context);
    }

    private void reverseOrderEditArea(Context context) {
        LinearLayout llSearchHolder = (LinearLayout) this.findViewById(R.id.search_plate);
        ArrayList<View> views = new ArrayList<>();
        for (int x = 0; x < llSearchHolder.getChildCount(); x++) {
            views.add(llSearchHolder.getChildAt(x));
        }
        llSearchHolder.removeAllViews();
        for (int x = views.size() - 1; x >= 0; x--) {
            llSearchHolder.addView(views.get(x));
        }


        searchEditArea = (SearchAutoComplete) this.findViewById(R.id.search_src_text);
        searchEditArea.setGravity(Gravity.RIGHT);
        searchEditArea.setHintTextColor(getResources().getColor(com.farmers.underground.R.color.text_white));
        searchEditArea.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "Fonts/ArialMT.ttf"));
        searchEditArea.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        this.callOnClick();
    }

    public SearchAutoComplete getSearchEditArea() {
        return searchEditArea;
    }
}
