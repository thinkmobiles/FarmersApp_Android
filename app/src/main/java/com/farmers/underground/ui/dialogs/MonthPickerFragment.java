package com.farmers.underground.ui.dialogs;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.farmers.underground.R;
import com.farmers.underground.config.ProjectConstants;
import com.farmers.underground.ui.activities.TransparentActivity;
import com.farmers.underground.ui.adapters.MonthPickerAdapter;
import com.farmers.underground.ui.base.BaseFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by samson
 * on 19.11.15.
 */
public class MonthPickerFragment extends BaseFragment<TransparentActivity> {

    private static final String KEY_TITLE = "title";
    private static final String KEY_POSITION = "position";

    @Bind(R.id.lvMonths_MP)
    protected ListView lvMonths;

    @Bind(R.id.tvTitle_MP)
    protected TextView tvTitle;

    @Bind(R.id.tvSelectedMonth_MP)
    protected TextView tvSelectedMonth;

    private ArrayList<String> listMonth;
    private MonthPickerAdapter adapter;
    private int numMonth;

    public static MonthPickerFragment newInstanse(String title, int selectedPosition){
        MonthPickerFragment fragment = new MonthPickerFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TITLE, title);
        args.putInt(KEY_POSITION, selectedPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_dialog_month_picker;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        numMonth = getArguments().getInt(KEY_POSITION);
        ButterKnife.bind(this, view);
        prepareListMonth();
        setAdapter();
        tvTitle.setText(getArguments().getString(KEY_TITLE));
        tvSelectedMonth.setText(listMonth.get(numMonth));
    }

    private void prepareListMonth(){
        listMonth = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.all_month)));
    }

    private void setAdapter(){
        adapter = new MonthPickerAdapter(getHostActivity(), listMonth);
        lvMonths.setAdapter(adapter);
        adapter.setSelectedPos(numMonth);
        lvMonths.smoothScrollToPosition(numMonth);
    }

    @SuppressWarnings("unused")
    @OnItemClick(R.id.lvMonths_MP)
    protected void onClickMonth(int pos) {
        tvSelectedMonth.setText(listMonth.get(pos));
        adapter.setSelectedPos(pos);
        lvMonths.smoothScrollToPosition(pos);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.tvOk_MP)
    protected void onClickOk(){
        Intent intent = new Intent();
        intent.putExtra(ProjectConstants.KEY_POS, adapter.getSelectedPos());
        getHostActivity().setResult(Activity.RESULT_OK, intent);
        getHostActivity().finish();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.tvCancel_MP)
    protected void onClickCancel(){
        getHostActivity().finish();
    }
}
