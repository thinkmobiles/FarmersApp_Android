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
 * Created by samson
 * on 29.09.15.
 */
public class PickMarketeerAdapter extends BaseAdapter {

    private Context context;
    private List<String> list;
    private List<String> fullList;
    private OnFindMarketerListener listener;
    private boolean isFirst;

    public PickMarketeerAdapter(Context context, List<String> list, OnFindMarketerListener listener) {
        this.context = context;
        this.fullList = list;
        this.listener = listener;
        this.list = fullList.subList(0, 10);
        this.isFirst = true;
    }

    public void findMarketeer(String name){
        isFirst = false;
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
            list = fullList.subList(0, 10);
            isFirst = true;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(isFirst) {
            listener.onFind(list.size() + 1);
        } else {
            listener.onFind(list.size());
        }
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
        return position == list.size() - 1 && list.size() != fullList.size() && !isFirst;
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
        void onFind(int countMarketer);
    }
}
