package com.farmers.underground.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.farmers.underground.R;
import com.farmers.underground.ui.models.QualityPriceItemModel;

import java.util.List;


/**
 * Created by tZpace
 * on 11-Oct-15.
 */
public class CropQualityPiecesAdapter extends BaseAdapter {

    private Context mContext;
    private List<QualityPriceItemModel> mListItems;

    static class ViewHolder {
        TextView tvPrice;   //tv_Price_crop_qualities_prieces_list
        TextView tvTitle;   //tv_title_crop_qualities_prieces_list
        TextView tvSubitle; //tv_subtitle_crop_qualities_prieces_list
    }

    public CropQualityPiecesAdapter(List<QualityPriceItemModel> mListItems, Context mContext) {
        this.mContext = mContext;
        this.mListItems = mListItems;
    }

    @Override
    public int getCount() {
        return mListItems.size();
    }

    @Override
    public QualityPriceItemModel getItem(int i) {
        return mListItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_crop_qualities_prieces_list, viewGroup, false);
            holder = new ViewHolder();

            holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_Price_crop_qualities_prieces_list);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title_crop_qualities_prieces_list);
            holder.tvSubitle = (TextView) convertView.findViewById(R.id.tv_subtitle_crop_qualities_prieces_list);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //set data here

        holder.tvPrice.setText(String.valueOf(mListItems.get(i).getPrice()));
        holder.tvTitle.setText(mListItems.get(i).getCropName());
        holder.tvSubitle.setText(mListItems.get(i).getQuality());


        return convertView;
    }


}





