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
        ButterKnife.bind(this, view);
        prepareListMonth();
        setAdapter();
        tvTitle.setText(getArguments().getString(KEY_TITLE));
        tvSelectedMonth.setText(listMonth.get(getArguments().getInt(KEY_POSITION)));
    }

    private void prepareListMonth(){
        listMonth = new ArrayList<>();
        listMonth.add(getString(R.string.month1));
        listMonth.add(getString(R.string.month2));
        listMonth.add(getString(R.string.month3));
        listMonth.add(getString(R.string.month4));
        listMonth.add(getString(R.string.month5));
        listMonth.add(getString(R.string.month6));
        listMonth.add(getString(R.string.month7));
        listMonth.add(getString(R.string.month8));
        listMonth.add(getString(R.string.month9));
        listMonth.add(getString(R.string.month10));
        listMonth.add(getString(R.string.month11));
        listMonth.add(getString(R.string.month12));
    }

    private void setAdapter(){
        adapter = new MonthPickerAdapter(getHostActivity(), listMonth);
        lvMonths.setAdapter(adapter);
        adapter.setSelectedPos(getArguments().getInt(KEY_POSITION));
    }

    @SuppressWarnings("unused")
    @OnItemClick(R.id.lvMonths_MP)
    protected void onClickMonth(int pos) {
        tvSelectedMonth.setText(listMonth.get(pos));
        adapter.setSelectedPos(pos);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.tvOk_MP)
    protected void onClickOk(){
        Intent intent = new Intent();
        intent.putExtra(ProjectConstants.KEY_DATA, listMonth.get(adapter.getSelectedPos()));
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
