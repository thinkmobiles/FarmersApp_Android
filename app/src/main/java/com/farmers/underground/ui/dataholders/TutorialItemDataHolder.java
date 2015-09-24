package com.farmers.underground.ui.dataholders;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

/**
 * Created by omar on 9/24/15.
 */
public class TutorialItemDataHolder {
    @DrawableRes
    int contentImageRes;
    @StringRes
    int contentText;

    public TutorialItemDataHolder(int contentImageRes, int contentText) {
        this.contentImageRes = contentImageRes;
        this.contentText = contentText;
    }
}
