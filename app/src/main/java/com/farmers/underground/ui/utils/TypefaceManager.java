package com.farmers.underground.ui.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

/**
 * Created by omar on 10/1/15.
 */
public class TypefaceManager {
    private static TypefaceManager typefaceManager;

    private static final String fontDir = "Fonts/";
    private final Typeface arialMTBold;
    private final Typeface arialMT ;
    private final Typeface avenir;
    private  AssetManager am;

    public TypefaceManager(Context context) {
        am = context.getAssets();
        arialMT =   Typeface.createFromAsset(am, fontDir + "ArialMT.ttf");
        arialMTBold = Typeface.createFromAsset(am, fontDir + "ArialMTBold.otf");
        avenir = Typeface.createFromAsset(am, fontDir + "Avenir.otf");

    }

    public static TypefaceManager getInstance() {

        return typefaceManager;
    }
    public static TypefaceManager init(Context context) {
        typefaceManager = new TypefaceManager(context);
        return typefaceManager;
    }

    public Typeface getArial(){
        return arialMT;
    }


    public Typeface getArialBold(){
        return arialMTBold;
    }

    public Typeface getAvenir(){
        return avenir;
    }
 }
