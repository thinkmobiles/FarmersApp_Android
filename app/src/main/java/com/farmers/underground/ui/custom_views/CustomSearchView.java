package com.farmers.underground.ui.custom_views;

import android.content.Context;
import android.support.v7.appcompat.R;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by omar on 9/30/15.
 */
public class CustomSearchView extends SearchView {
    public CustomSearchView(Context context) {
        super(context);
        reverseOrderEditArea();
    }

    public CustomSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        reverseOrderEditArea();

    }

    public CustomSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        reverseOrderEditArea();
    }

    private void reverseOrderEditArea() {
        LinearLayout llSearchholder = (LinearLayout) this.findViewById(R.id.search_plate);
        ArrayList<View> views = new ArrayList<>();
        for (int x = 0; x < llSearchholder.getChildCount(); x++) {
            views.add(llSearchholder.getChildAt(x));
        }
        llSearchholder.removeAllViews();
        for (int x = views.size() - 1; x >= 0; x--) {

            llSearchholder.addView(views.get(x));
        }
    }


    @Override
    public boolean isIconified() {
        View imgSearch;
        imgSearch = this.findViewById(R.id.search_src_text);
        ((SearchAutoComplete) imgSearch).setGravity(Gravity.RIGHT);
        ((SearchAutoComplete) imgSearch).setHintTextColor(getResources().getColor(com.farmers.underground.R.color.text_white));
        return super.isIconified();
    }

}
