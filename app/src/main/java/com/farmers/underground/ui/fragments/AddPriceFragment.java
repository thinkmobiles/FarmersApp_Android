package com.farmers.underground.ui.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.farmers.underground.FarmersApp;
import com.farmers.underground.R;
import com.farmers.underground.remote.models.UserPriceQualityModel;
import com.farmers.underground.ui.activities.AddPriceActivity;
import com.farmers.underground.ui.base.BaseFragment;
import com.farmers.underground.ui.utils.StringFormaterUtil;
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

    private static final int MAX_AMOUNT_PRICES = 9;

    @Bind(R.id.etPrice_FAP)
    protected EditText etPrice;

    @Bind(R.id.etQuality_FAP)
    protected EditText etQuality;

    @Bind(R.id.tvDate_FAP)
    protected TextView tvDate;

    @Bind(R.id.tvPriceError_FAP)
    protected TextView tvError;

    @Bind(R.id.tvNameMarketer_FAP)
    protected TextView nameMarketer;

    @Bind(R.id.tvLogo_FAP)
    protected TextView tvLogo;

    @Bind(R.id.llContainerPrices)
    protected LinearLayout container;

    private List<EditText> listPrice;
    private List<EditText> listQuality;
    private List<TextView> listError;

    private int counter;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_add_price;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        getHostActivity().setOnChangeDateListener(this);
        getHostActivity().setChildFragment(this);
        setMarketer();
        setHint(etPrice);
        setDate();
        initLists();
    }

    public void setDate() {
        tvDate.setText(getHostActivity().getDate());
    }

    public void refresh(){
        etPrice.setText("");
        etQuality.setText("");
        tvError.setVisibility(View.INVISIBLE);
        container.removeViews(2, ++counter);
        initLists();
    }

    public ArrayList<UserPriceQualityModel> getPriceList(){
        ArrayList<UserPriceQualityModel> list = new ArrayList<>();
        list.add(new UserPriceQualityModel(Double.valueOf(etPrice.getText().toString()), etQuality.getText().toString()));
        for(int i = 0; i <= counter; ++i){
            if(!TextUtils.isEmpty(listPrice.get(i).getText().toString())){
                list.add(new UserPriceQualityModel(Double.valueOf(listPrice.get(i).getText().toString()), listQuality.get(i).getText().toString()));
            }
        }
        return list;
    }

    private void initLists(){
        listPrice = new ArrayList<>();
        listQuality = new ArrayList<>();
        listError = new ArrayList<>();

        counter = -1;
    }

    private void setMarketer(){
        String name = FarmersApp.getInstance().getCurrentMarketer().getFullName();
        if(name != null) {
            nameMarketer.setText(name);
            tvLogo.setText(StringFormaterUtil.getLettersForLogo(name));
        }
    }

    private void checkCorrection(){
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

    private void setHint(EditText editText) {
        editText.setHint(Html.fromHtml("<small><small><small>" + getHostActivity().getString(R.string.add_price_hint_price) + "</small></small></small>"));
    }

    @OnClick(R.id.tvChangeDate_FAP)
    protected void showDatePicker(){
        getHostActivity().showDatePicker();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.tvDate_FAP)
    protected void showDate(){
        showDatePicker();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.tvAddQuality_FAP)
    protected void addNewQuality(){
        if(counter < MAX_AMOUNT_PRICES) {
            ++counter;
            View view = LayoutInflater.from(getHostActivity()).inflate(R.layout.item_price_type, null);
            EditText editTextP = (EditText) view.findViewById(R.id.etPrice_FAP);
            setHint(editTextP);
            editTextP.addTextChangedListener(getPriceWatcher(counter));
            listPrice.add(editTextP);
            EditText editTextQ = (EditText) view.findViewById(R.id.etQuality_FAP);
            editTextQ.addTextChangedListener(getQualityWatcher());
            listQuality.add(editTextQ);
            listError.add((TextView) view.findViewById(R.id.tvPriceError_FAP));
            container.addView(view);
//            container.requestLayout();
//            editTextP.requestFocus();
        }
    }

    @Override
    public void onChangeDate() {
        setDate();
    }

    @SuppressWarnings("unused")
    @OnTextChanged(R.id.etPrice_FAP)
    protected void checkValidPrice(CharSequence text){
        checkValidation(text.toString(), tvError);
        checkCorrection();
    }

    @SuppressWarnings("unused")
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
