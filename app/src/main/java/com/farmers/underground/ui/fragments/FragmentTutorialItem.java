package com.farmers.underground.ui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.Bind;
import com.farmers.underground.R;


/**
 * Created by omar on 9/24/15.
 */
public class FragmentTutorialItem extends Fragment {

    @Bind(R.id.iv_TutorialItemBG)
    ImageView iv_TutorialItemBG;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_tutorial, container, false);
        return view;
    }
}
