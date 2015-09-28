package com.farmers.underground.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.farmers.underground.R;
import com.farmers.underground.ui.models.TutorialItemDataHolder;
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
    protected TextView tv_TutorialText;

    @Bind(R.id.fl_TutorialNext)
    protected FrameLayout fl_nextButton;

    private TutorialItemDataHolder tutorialItemDataHolder;
    private Callback callback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tutorialItemDataHolder = (TutorialItemDataHolder) getArguments().getSerializable(TutorialActivity.KEY_DATA);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (Callback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
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
        animateNextButton();
    }

    private void setData() {
        iv_TutorialItemBG.setImageResource(tutorialItemDataHolder.getContentImageRes());
        tv_TutorialText.setText(getString(tutorialItemDataHolder.getContentTextRes()));
    }

    @OnClick(R.id.fl_TutorialNext)
    protected void onNextClikcked() {
        callback.onNextClicked();
    }

    @OnClick(R.id.tv_TutorialTitle)
    protected void OnTitleClicked() {
        callback.onSkipClicked();
    }

    public void animateNextButton() {
        if (fl_nextButton != null) {
            fl_nextButton.setTranslationY(1000);
            fl_nextButton.animate().translationYBy(1000)
                    .translationY(0)
                    .setDuration(getResources().getInteger(android.R.integer.config_longAnimTime));
        }
    }

    public interface Callback {
        void onNextClicked();

        void onSkipClicked();
    }


}
