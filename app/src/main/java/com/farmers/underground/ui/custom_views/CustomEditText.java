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
import com.farmers.underground.ui.utils.TypefaceManager;


/**
 * @author Oleg Bondarenko on 5/11/2015.
 */
public class CustomEditText extends EditText {

    private static final String fontDir = "Fonts/";

    public CustomEditText(Context context) { super(context); }

    public CustomEditText(Context context, AttributeSet attrs) {

        super(context, attrs);
        defineTypeface(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {

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
                R.styleable.CustomEditText,
                0, 0);

        try {
            int font = a.getInteger(R.styleable.CustomEditText_customFont, 0);
            setTypefaceBasedOnInt(font);
        }
        finally {
            a.recycle();
        }
    }

    public void setFontStyle(CustomFontStyle style){
        setTypefaceBasedOnInt(style.ordinal());
    }

    private void setTypefaceBasedOnInt(int fontEnum){
        Typeface typeface = null;
        AssetManager am = getContext().getAssets();

        switch (fontEnum){
            default:
            case 0:
                typeface = TypefaceManager.getInstance().getArial();
                break;
            case 1:
                typeface = TypefaceManager.getInstance().getArialBold();
                break;
            case 2:
                typeface = TypefaceManager.getInstance().getAvenir();
                break;

        }
        setTypeface(typeface);
    }
}
