package com.farmers.underground.ui.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.farmers.underground.R;
import com.farmers.underground.ui.utils.ValidationUtil;

/**
 * Created by samson
 * on 22.10.15.
 */
public class PriceQualityAdapter extends BaseAdapter {

    private static final int LIMIT = 9;

    private int counter;
    private Context context;
    private OnCorrectionListener onCorrectionListener;

    private boolean isCorrectly;

    public PriceQualityAdapter(Context context, OnCorrectionListener onCorrectionListener) {
        this.context = context;
        this.onCorrectionListener = onCorrectionListener;
        this.isCorrectly = true;
        this.counter = 0;
    }

    public void addBlock(){
        if(counter <= LIMIT) {
            ++counter;
            notifyDataSetChanged();
        }
    }

    public void checkCorrection(){
        onCorrectionListener.onCorrect(isCorrectly);
    }

    @Override
    public int getCount() {
        return counter;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_price_type, parent, false);
            holder.init(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.setListeners();

        return convertView;
    }

    class ViewHolder{
        private EditText etPrice;
        private EditText etQuality;
        private TextView tvError;

        public void init(View view){
            etPrice = (EditText) view.findViewById(R.id.etPrice_FAP);
            etQuality = (EditText) view.findViewById(R.id.etQuality_FAP);
            tvError = (TextView) view.findViewById(R.id.tvPriceError_FAP);

            etPrice.setHint(Html.fromHtml("<small><small><small>" + context.getString(R.string.add_price_hint_price) + "</small></small></small>"));
        }

        public void setListeners(){
            etPrice.addTextChangedListener(priceWatcher);
            etQuality.addTextChangedListener(qualityWatcher);
        }

        private TextWatcher priceWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count == 0){
                    isCorrectly = etQuality.getText().toString().isEmpty();
                    tvError.setVisibility(View.INVISIBLE);
                }else if(ValidationUtil.isValidPrice(s.toString())){
                    tvError.setVisibility(View.INVISIBLE);
                    isCorrectly = !etQuality.getText().toString().isEmpty();
                } else {
                    tvError.setVisibility(View.VISIBLE);
                    isCorrectly = false;
                }
                onCorrectionListener.onCorrect(isCorrectly);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        private TextWatcher qualityWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count > 0){
                    isCorrectly = tvError.getVisibility() != View.VISIBLE;
                } else {
                    isCorrectly = etPrice.getText().toString().isEmpty();
                }
                onCorrectionListener.onCorrect(isCorrectly);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    public interface OnCorrectionListener{
        void onCorrect(boolean isCorrectly);
    }
}
