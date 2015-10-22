package com.farmers.underground.ui.fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.farmers.underground.R;
import com.farmers.underground.ui.activities.AddPriceActivity;
import com.farmers.underground.ui.adapters.PriceQualityAdapter;
import com.farmers.underground.ui.base.BaseFragment;
import com.farmers.underground.ui.utils.ValidationUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 *
 * Created by samson on 09.10.15.
 */
public class AddPriceFragment extends BaseFragment<AddPriceActivity> implements AddPriceActivity.OnChangeDateListener, PriceQualityAdapter.OnCorrectionListener {

    private static final String KEY_ID_MARKETER = "id_marketer";

    @Bind(R.id.etPrice_FAP)
    protected EditText etPrice;

    @Bind(R.id.etQuality_FAP)
    protected EditText etQuality;

    @Bind(R.id.tvDate_FAP)
    protected TextView tvDate;

    @Bind(R.id.tvPriceError_FAP)
    protected TextView tvError;

    @Bind(R.id.llContainerPrices)
    protected LinearLayout container;

    @Bind(R.id.listPriceQuality_FAP)
    protected ListView listPriceQuality;

    private PriceQualityAdapter adapter;

    private boolean isFilledFirsrtPrice = false;

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
        setAdapter();
    }

    private void setAdapter(){
        adapter = new PriceQualityAdapter(getHostActivity(), this);
        listPriceQuality.setAdapter(adapter);
    }

    public void setDate() {
        tvDate.setText(getHostActivity().getDate());
    }

    public void checkCorrection(){
        isFilledFirsrtPrice = !etPrice.getText().toString().isEmpty()
                && !etQuality.getText().toString().isEmpty()
                && tvError.getVisibility() != View.VISIBLE;
        adapter.checkCorrection();
    }

    private void setHints() {
        etPrice.setHint(Html.fromHtml("<small><small><small>" + getHostActivity().getString(R.string.add_price_hint_price) + "</small></small></small>"));
    }

    @OnClick(R.id.tvChangeDate_FAP)
    protected void showDatePicker(){
        getHostActivity().showDatePicker();
    }

    @OnClick(R.id.tvDate_FAP)
    protected void showDate(){
        showDatePicker();
    }

    @OnClick(R.id.tvAddQuality_FAP)
    protected void addNewQuality(){
        adapter.addBlock();
    }

    @Override
    public void onChangeDate() {
        setDate();
    }

    @OnTextChanged(R.id.etPrice_FAP)
    protected void checkValidPrice(CharSequence text){
        checkValidation(text.toString(), tvError);
        checkCorrection();
    }

    @OnTextChanged(R.id.etQuality_FAP)
    protected void changeQuality(){
        checkCorrection();
    }

    private void checkValidation(String price, TextView textViewError){
        if(ValidationUtil.isValidPrice(price)){
            textViewError.setVisibility(View.INVISIBLE);
        } else {
            textViewError.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onCorrect(boolean isCorrectly) {
        getHostActivity().setEnableDone(isCorrectly && isFilledFirsrtPrice);
    }
}
