package com.farmers.underground.ui.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.farmers.underground.R;

import java.util.ArrayList;

/**
 * Created by samson on 19.11.15.
 */
public class MonthPickerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> listMonth;
    private int selectedPos;

    public MonthPickerAdapter(Context context, ArrayList<String> listMonth) {
        this.context = context;
        this.listMonth = listMonth;
        this.selectedPos = 0;
    }

    public void setSelectedPos(int selectedPos) {
        this.selectedPos = selectedPos;
        notifyDataSetChanged();
    }

    public int getSelectedPos() {
        return selectedPos;
    }

    @Override
    public int getCount() {
        return listMonth.size();
    }

    @Override
    public String getItem(int position) {
        return listMonth.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_month_picker,parent, false);
            holder.init(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.setData(getItem(position));

        if (position == selectedPos){
            holder.select();
        } else {
            holder.unselect();
        }

        return convertView;
    }

    class ViewHolder{
        private TextView tvMonth;
        private int colorSelected, colorNormal;
        private float sizeSelected, sizeNormal;

        public void init(View view){
            tvMonth = (TextView) view.findViewById(R.id.tvMonth_IMP);
            Resources res = context.getResources();
            colorNormal = res.getColor(R.color.text_dark_grey);
            colorSelected = res.getColor(R.color.text_aqua);
            sizeNormal = res.getDimension(R.dimen.text_month_normal);
            sizeSelected = res.getDimension(R.dimen.text_month_big);
        }

        public void setData(String month){
            tvMonth.setText(month);
        }

        public void select(){
            tvMonth.setTextColor(colorSelected);
            tvMonth.setTextSize(sizeSelected);
        }

        public void unselect(){
            tvMonth.setTextColor(colorNormal);
            tvMonth.setTextSize(sizeNormal);
        }
    }
}
