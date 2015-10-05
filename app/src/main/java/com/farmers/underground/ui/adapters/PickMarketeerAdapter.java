package com.farmers.underground.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.farmers.underground.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by samson on 29.09.15.
 */
public class PickMarketeerAdapter extends BaseAdapter {

    private Context context;
    private List<String> list;
    private List<String> fullList;
    private OnFindMarketerListener listener;

    public PickMarketeerAdapter(Context context, List<String> list, OnFindMarketerListener listener) {
        this.context = context;
        this.list = list;
        this.fullList = list;
        this.listener = listener;
    }

    public void findMarketeer(String name){
        if(name.length() != 0){
            List<String> tempList = new ArrayList<>();
            for(String marketeer : fullList){
                if(marketeer.contains(name)){
                    tempList.add(marketeer);
                }
            }
            tempList.add(context.getString(R.string.select_marketeer_add_name) + " \"" + name + "\"");
            list = tempList;
        } else {
            list = fullList;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        listener.onFind(list.size());
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_pick_marketer, parent, false);
            holder.init(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.setData(getItem(position));

        if(isAddItem(position)){
            holder.select();
        } else {
            holder.diselect();
        }

        return convertView;
    }

    public boolean isAddItem(int position){
        return position == list.size() - 1 && list.size() != fullList.size();
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

        public void diselect(){
            tvNameMarketer.setTextColor(context.getResources().getColor(R.color.white));
        }
    }

    public interface OnFindMarketerListener{
        public void onFind(int countMarketer);
    }
}
