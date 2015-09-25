package com.farmers.underground.ui.custom_views;

import android.view.View;

/**
 * Created by omar on 9/25/15.
 */
public class Span {

    private String _text;
    private int _color;
    private View.OnClickListener _listener;

    public Span(String text, int color, View.OnClickListener listener){
        _text = text;
        _color = color;
        _listener = listener;
    }

    public Span(String text){
        _text = text;
        _color = 0xFFFFFFFF;
        _listener = null;
    }

    public Span(String text, int color){
        _text = text;
        _color = color;
        _listener = null;
    }

    public String getText(){ return _text; }

    public int getColor(){ return _color; }

    public View.OnClickListener getListener(){ return _listener; }

}