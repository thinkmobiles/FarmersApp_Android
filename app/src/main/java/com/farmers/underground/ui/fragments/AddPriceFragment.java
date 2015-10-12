package com.farmers.underground.ui.fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.farmers.underground.R;
import com.farmers.underground.ui.activities.AddPriceActivity;
import com.farmers.underground.ui.base.BaseFragment;
import com.farmers.underground.ui.utils.ValidationUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *
 * Created by samson on 09.10.15.
 */
public class AddPriceFragment extends BaseFragment<AddPriceActivity> implements AddPriceActivity.OnChangeDateListener {

    private static final String KEY_ID_MARKETER = "id_marketer";

    @Bind(R.id.etPrice_FAP)
    protected EditText etPrice;

    @Bind(R.id.etQuality_FAP)
    protected EditText etQuality;

    @Bind(R.id.tvDate_FAP)
    protected TextView tvDate;

    @Bind(R.id.tvPriceError_FAP)
    protected TextView tvError;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_add_price;
    }

    public static AddPriceFragment newInstance(String idMarketer){
        AddPriceFragment fragment = new AddPriceFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ID_MARKETER, idMarketer);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        getHostActivity().setOnChangeDateListener(this);
        setHints();
        setDate();
    }

    public void setDate(){
        tvDate.setText(getHostActivity().getDate());
    }

    private void setHints(){
        etPrice.setHint(Html.fromHtml("<small><small><small>" + getHostActivity().getString(R.string.add_price_hint_price) + "</small></small></small>"));
    }

    @OnClick(R.id.tvChangeDate_FAP)
    protected void showDatePicker(){
        getHostActivity().showDatePicker();
    }

    @OnClick(R.id.tvAddQuality_FAP)
    protected void addNewQuality(){

    }

    @OnClick(R.id.tvAddPrice_FAP)
    protected void addPrice(){
        if(ValidationUtil.isValidPrice(etPrice.getText().toString())){
            //todo add_price request
            tvError.setVisibility(View.INVISIBLE);
        } else {
            tvError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onChangeDate() {
        setDate();
    }
}
