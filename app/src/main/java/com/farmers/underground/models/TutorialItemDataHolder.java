package com.farmers.underground.models;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import java.io.Serializable;

/**
 * Created by omar
 * on 9/24/15.
 */
public class TutorialItemDataHolder implements Serializable {
    @DrawableRes
    private int contentImageRes;
    @StringRes
    private int contentText;

    public TutorialItemDataHolder( int contentText, int contentImageRes) {
        this.contentImageRes = contentImageRes;
        this.contentText = contentText;
    }


    public int getContentImageRes() {
        return contentImageRes;
    }

    public int getContentTextRes() {
        return contentText;
    }
}
