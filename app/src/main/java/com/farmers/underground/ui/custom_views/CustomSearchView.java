package com.farmers.underground.ui.custom_views;

import android.content.Context;
import android.support.v7.appcompat.R;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by omar on 9/30/15.
 */
public class CustomSearchView extends SearchView implements View.OnClickListener {
    LinearLayout llSearchbar = (LinearLayout) this.findViewById(R.id.search_bar);

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
        TextView tv =  (TextView)LayoutInflater.from(llSearchbar.getContext()).inflate(com.farmers.underground.R
                .layout.search_hint,null);
        tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setTag("lalala");
        tv.setClickable(true);
        tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.search_button).callOnClick();
            }
        });
        llSearchbar.addView(tv);
    }


    @Override
    public boolean isIconified() {
        if (super.isIconified()) {
            llSearchbar.findViewWithTag("lalala").setVisibility(VISIBLE);
        } else {
            llSearchbar.findViewWithTag("lalala").setVisibility(GONE);
        }
        View  imgSearch = this.findViewById(R.id.search_src_text);
        
        ((SearchAutoComplete) imgSearch).setGravity(Gravity.RIGHT);
        ((SearchAutoComplete) imgSearch).setHintTextColor(getResources().getColor(com.farmers.underground.R.color.text_white));
        imgSearch.setOnClickListener(this);
        return super.isIconified();
    }

    @Override
    public void onClick(View view) {
        this.callOnClick();
    }
}
