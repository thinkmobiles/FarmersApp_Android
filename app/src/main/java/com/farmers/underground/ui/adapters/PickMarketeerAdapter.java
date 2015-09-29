package com.farmers.underground.ui.adapters;

import android.content.Context;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.farmers.underground.R;

import java.util.ArrayList;

/**
 * Created by samson on 29.09.15.
 */
public class PickMarketeerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> list;

    public PickMarketeerAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_pick_marketer, parent);
            holder.init(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.setData(getItem(position));

        if(position == list.size() - 1){
            holder.select();
        }

        return convertView;
    }

    class ViewHolder{
        TextView tvNameMarketer;

        public void init(View view){
            tvNameMarketer = (TextView) view.findViewById(R.id.tvNameMarketer);
        }

        public void setData(String name){
            tvNameMarketer.setText(name);
        }

        public void select(){
            tvNameMarketer.setTextColor(context.getResources().getColor(R.color.text_aqua));
        }
    }
}
