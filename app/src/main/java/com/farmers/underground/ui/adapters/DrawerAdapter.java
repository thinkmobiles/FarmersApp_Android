package com.farmers.underground.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.farmers.underground.R;
import com.farmers.underground.ui.models.DrawerItem;

import java.util.List;

/**
 * Created by omar on 9/28/15.
 */
public class DrawerAdapter extends BaseAdapter implements View.OnClickListener {
    private Context mContext;
    private LayoutInflater inflater;
    private List<DrawerItem> mListItems;
    private DrawerCallback drawerCallback;

    public DrawerAdapter(List<DrawerItem> mListItems, Context mContext) {
        this.mContext = mContext;
        this.mListItems = mListItems;
        try {
            this.drawerCallback = (DrawerCallback) mContext;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mListItems.size();
    }

    @Override
    public DrawerItem getItem(int i) {
        return mListItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        switch (mListItems.get(i).item_type) {
            case CONTENT_ITEM:
                return createContentView(i, convertView, viewGroup);
            case HEADER:
                return createHeaderView(i, convertView, viewGroup);
            case DEVIDER:
                return createDeviderView(i, convertView, viewGroup);
            default:  return null;
        }
    }

    @Override
    public void onClick(View view) {
        if (drawerCallback != null) drawerCallback.onSettingsClicked();
    }

    static class ViewHolder {
        TextView tvContentTitle;
        TextView tvUserName;
        ImageView ivUserIcon;
        ImageView ivContentIcon;
    }

    View createHeaderView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        view = inflater.inflate(R.layout.drawer_header, parent, false);

        viewHolder = new ViewHolder();
        viewHolder.ivUserIcon = (ImageView) view.findViewById(R.id.iv_DrawerUserIcon);
        viewHolder.tvUserName = (TextView) view.findViewById(R.id.tv_DrawerUserName);
        viewHolder.ivContentIcon = (ImageView) view.findViewById(R.id.iv_DrawerSettingIcon);
        viewHolder.tvUserName.setText(mListItems.get(position).userName);
        viewHolder.ivContentIcon.setTag(position);
        viewHolder.ivContentIcon.setOnClickListener(this);
        return view;
    }

    View createContentView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        view = inflater.inflate(R.layout.drawer_item_data, parent, false);

        viewHolder = new ViewHolder();
        viewHolder.tvContentTitle = (TextView) view.findViewById(R.id.tv_DrawerItemContent);
        viewHolder.ivContentIcon = (ImageView) view.findViewById(R.id.iv_DrawerItemIcon);
        viewHolder.tvContentTitle.setText(mContext.getString(mListItems.get(position).contentName));
        viewHolder.ivContentIcon.setImageResource(mListItems.get(position).icon);
        return view;
    }

    View createDeviderView(int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.drawer_devider, parent, false);
        return view;
    }


    public interface DrawerCallback {
        void onSettingsClicked();
    }


}





