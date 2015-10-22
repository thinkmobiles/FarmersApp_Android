package com.farmers.underground.ui.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.farmers.underground.R;
import com.farmers.underground.ui.activities.AddPriceActivity;
import com.farmers.underground.ui.base.BaseFragment;
import com.farmers.underground.ui.utils.ValidationUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by samson
 * on 22.10.15.
 */
public class AddPriceFragment extends BaseFragment<AddPriceActivity> implements AddPriceActivity.OnChangeDateListener {

    private static final int LIMIT = 8;

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

    private List<EditText> listPrice = new ArrayList<>();
    private List<EditText> listQuality = new ArrayList<>();
    private List<TextView> listError = new ArrayList<>();

    private int counter = -1;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_add_price;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        getHostActivity().setOnChangeDateListener(this);
        setHints();
        setDate();
    }

    public void setDate() {
        tvDate.setText(getHostActivity().getDate());
    }

    public void checkCorrection(){
        if(!(!etPrice.getText().toString().isEmpty() && !etQuality.getText().toString().isEmpty() && tvError.getVisibility() != View.VISIBLE)){
            getHostActivity().setEnableDone(false);
        } else {
            for(int i = 0; i <= counter; ++i){
                if((!listPrice.get(i).getText().toString().isEmpty()
                        && !listQuality.get(i).getText().toString().isEmpty())
                        || (listPrice.get(i).getText().toString().isEmpty()
                        && listQuality.get(i).getText().toString().isEmpty()) ){
                    if(listError.get(i).getVisibility() == View.VISIBLE){
                        getHostActivity().setEnableDone(false);
                        return;
                    }
                } else {
                    getHostActivity().setEnableDone(false);
                    return;
                }
            }
            getHostActivity().setEnableDone(true);
        }
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
        if(counter <= LIMIT) {
            ++counter;
            View view = LayoutInflater.from(getHostActivity()).inflate(R.layout.item_price_type, null);
            EditText editText = (EditText) view.findViewById(R.id.etPrice_FAP);
            editText.setHint(Html.fromHtml("<small><small><small>" + getHostActivity().getString(R.string.add_price_hint_price) + "</small></small></small>"));
            editText.addTextChangedListener(getPriceWatcher(counter));
            listPrice.add(editText);
            editText = (EditText) view.findViewById(R.id.etQuality_FAP);
            editText.addTextChangedListener(getQualityWatcher());
            listQuality.add(editText);
            listError.add((TextView) view.findViewById(R.id.tvPriceError_FAP));
            container.addView(view);
        }
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

    private TextWatcher getPriceWatcher(final int position){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(ValidationUtil.isValidPrice(s.toString())){
                    listError.get(position).setVisibility(View.INVISIBLE);
                } else {
                    listError.get(position).setVisibility(View.VISIBLE);
                }
                if(s.toString().isEmpty()){
                    listError.get(position).setVisibility(View.INVISIBLE);
                }
                checkCorrection();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    private TextWatcher getQualityWatcher(){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkCorrection();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

}
