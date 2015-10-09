package com.farmers.underground.ui.custom_views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.appcompat.R;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.farmers.underground.ui.utils.ResourceRetriever;

import java.util.ArrayList;

/**
 * Created by omar
 * on 9/30/15.
 */
public class CustomSearchView extends SearchView implements View.OnClickListener {

    LinearLayout llSearchBar = (LinearLayout) this.findViewById(R.id.search_bar);

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
        TextView tv = (TextView) LayoutInflater.from(context).inflate(com.farmers.underground.R
                .layout.search_hint, null);
        tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setTag("lalala");
        tv.setClickable(true);
        tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.search_button).callOnClick();
            }
        });
        tv.setPadding(0,0,0, ResourceRetriever.dpToPx(this.getContext(),4));
        llSearchBar.addView(tv);
    }


    @Override
    public boolean isIconified() {
        if (super.isIconified()) {
            llSearchBar.findViewWithTag("lalala").setVisibility(VISIBLE);
        } else {
            llSearchBar.findViewWithTag("lalala").setVisibility(GONE);
        }
        SearchAutoComplete  imgSearch = (SearchAutoComplete) this.findViewById(R.id.search_src_text);
        imgSearch.setGravity(Gravity.RIGHT);
        imgSearch.setHintTextColor(getResources().getColor(com.farmers.underground.R.color.text_white));
        imgSearch.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "Fonts/ArialMT.ttf"));
        imgSearch.setOnClickListener(this);
        return super.isIconified();
    }

    @Override
    public void onClick(View view) {
        this.callOnClick();
    }
}
