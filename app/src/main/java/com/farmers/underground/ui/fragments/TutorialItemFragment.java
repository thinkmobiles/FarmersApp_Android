package com.farmers.underground.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.farmers.underground.R;
import com.farmers.underground.models.TutorialItemDataHolder;
import com.farmers.underground.ui.activities.TutorialActivity;
import com.farmers.underground.ui.base.BaseFragment;

/**
 * Created by omar
 * on 9/24/15.
 */
public class TutorialItemFragment extends BaseFragment<TutorialActivity> {

    @Bind(R.id.iv_TutorialItemBG)
    protected ImageView iv_TutorialItemBG;

    @Bind(R.id.tv_TutorialText)
    protected  TextView tv_TutorialText;

    @Bind(R.id.tv_TutorialTitle)
    protected  TextView tv_TutorialTitle;

    private TutorialItemDataHolder tutorialItemDataHolder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tutorialItemDataHolder = (TutorialItemDataHolder) getArguments().getSerializable(TutorialActivity.KEY_DATA);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.item_tutorial;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        setData();
    }

    private void setData() {
        iv_TutorialItemBG.setImageResource(tutorialItemDataHolder.getContentImageRes());
        tv_TutorialText.setText(getString(tutorialItemDataHolder.getContentTextRes()));
        tv_TutorialTitle.setText(getString(tutorialItemDataHolder.getContentTitleRes()));
    }
}
