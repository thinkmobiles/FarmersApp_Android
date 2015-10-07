/*
 * Copyright © HBO Central Europe Zrt. 2010 - 2015
 * @author Digital Media Experience LLC
 */
package com.farmers.underground.ui.custom_views;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.farmers.underground.R;
import com.farmers.underground.ui.utils.TypefaceManager;

/**
 * @author Oleg Bondarenko on 5/11/2015.
 */
public class CustomTextView extends TextView {
    
    private static final String fontDir = "Fonts/";
    
    public CustomTextView(Context context) { super(context); }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        defineTypeface(context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        defineTypeface(context, attrs);
    }

    private void defineTypeface(Context context, AttributeSet attrs){
        if(isInEditMode()){
            return;
        }



        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomTextView,
                0, 0);
        try {
            int font = a.getInteger(R.styleable.CustomTextView_customFont, 13);
            setTypefaceBasedOnInt(font);
        }
        finally {
            a.recycle();
        }
    }

    public void setFontStyle(CustomFontStyle style){
        setTypefaceBasedOnInt(style.ordinal());
    }

    public void setTypefaceBasedOnInt(int fontEnum) {
        Typeface typeface = null;
        AssetManager am = getContext().getAssets();

        switch (fontEnum){
            case 0:
                typeface = TypefaceManager.getInstance().getArial();
                break;
            case 1:
                typeface = TypefaceManager.getInstance().getArialBold();
                break;
            case 2:
                typeface = TypefaceManager.getInstance().getAvenir();
                break;
            case 3:
                typeface = TypefaceManager.getInstance().getRobotoLight();
                break;
        }
        setTypeface(typeface);
    }

    public void setText(Span[] textDataToSet){
        if(textDataToSet == null || textDataToSet.length == 0){
            return;
        }

        setMovementMethod(LinkMovementMethod.getInstance());

        String unifiedText = "";
        for(Span span : textDataToSet){
            if(!unifiedText.isEmpty()){
                unifiedText += " ";
            }
            unifiedText += span.getText();
        }

        SpannableString spannableString = new SpannableString(unifiedText);
        for(final Span span : textDataToSet){
            int index = unifiedText.indexOf(span.getText());
            int lastIndex = index + span.getText().length();
            if(span.getListener() != null){
                spannableString.setSpan(new ClickableSpan() {
                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setUnderlineText(false);
                    }

                    @Override
                    public void onClick(View widget) {
                        span.getListener().onClick(widget);
                    }
                }, index, lastIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            spannableString.setSpan(new ForegroundColorSpan(span.getColor()), index, lastIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        setText(spannableString);
    }
}
