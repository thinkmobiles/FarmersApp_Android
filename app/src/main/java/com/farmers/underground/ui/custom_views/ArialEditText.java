/*
 * Copyright © HBO Central Europe Zrt. 2010 - 2015
 * @author Digital Media Experience LLC
 */
package com.farmers.underground.ui.custom_views;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import com.farmers.underground.R;


/**
 * @author Oleg Bondarenko on 5/11/2015.
 */
public class ArialEditText extends EditText {

    private static final String fontDir = "Fonts/";

    public ArialEditText(Context context) { super(context); }

    public ArialEditText(Context context, AttributeSet attrs) {

        super(context, attrs);
        defineTypeface(context, attrs);
    }

    public ArialEditText(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        defineTypeface(context, attrs);
    }

    private void defineTypeface(Context context, AttributeSet attrs){
        if(isInEditMode()){
            return;
        }

        setImeOptions(EditorInfo.IME_ACTION_DONE);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ArialEditText,
                0, 0);

        try {
            int font = a.getInteger(R.styleable.ArialEditText_customFont, 0);
            setTypefaceBasedOnInt(font);
        }
        finally {
            a.recycle();
        }
    }

    public void setFontStyle(ArialFontStyle style){
        setTypefaceBasedOnInt(style.ordinal());
    }

    private void setTypefaceBasedOnInt(int fontEnum){
        Typeface typeface = null;
        AssetManager am = getContext().getAssets();

        switch (fontEnum){
            default:
            case 0:
                typeface = Typeface.createFromAsset(am, fontDir + "ArialMT.ttf");
                break;

            case 1:
                typeface = Typeface.createFromAsset(am, fontDir + "ArialMTBold.otf");
                break;

        }
        setTypeface(typeface);
    }
}
