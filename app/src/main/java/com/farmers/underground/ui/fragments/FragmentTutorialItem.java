package com.farmers.underground.ui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.farmers.underground.R;
import com.farmers.underground.models.TutorialItemDataHolder;
import android.support.design.widget.Snackbar;


/**
 * Created by omar on 9/24/15.
 */
public class FragmentTutorialItem extends Fragment {

    @Bind(R.id.iv_TutorialItemBG)
    ImageView iv_TutorialItemBG;
    @Bind(R.id.tv_TutorialText)
    TextView tv_TutorialText;
    @Bind(R.id.tv_TutorialTitle)
    TextView tv_TutorialTitle;
    @Bind(R.id.snackbarPosition)
    View coordinatorLayoutView;

    private TutorialItemDataHolder tutorialItemDataHolder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tutorialItemDataHolder = (TutorialItemDataHolder) getArguments().getSerializable("data");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_tutorial, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setData();
    }

    private void setData() {
        iv_TutorialItemBG.setImageResource(tutorialItemDataHolder.getContentImageRes());
        tv_TutorialText.setText(getString(tutorialItemDataHolder.getContentTextRes()));
        tv_TutorialTitle.setText(getString(tutorialItemDataHolder.getContentTitleRes()));
    }

    @OnClick(R.id.iv_TutorialItemBG)
    void showSnackBar(){
        Snackbar
                .make(coordinatorLayoutView, R.string.tutorial_snackbar_text, Snackbar.LENGTH_LONG)
                .setAction(R.string.tutorial_snackbar_btn_text, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getActivity().onBackPressed();
                    }
                })
                .show();
    }
}
