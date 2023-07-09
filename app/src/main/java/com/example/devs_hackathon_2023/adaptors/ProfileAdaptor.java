package com.example.devs_hackathon_2023.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.devs_hackathon_2023.R;
import com.example.devs_hackathon_2023.User.MainPlayer;
import com.example.devs_hackathon_2023.User.Stat;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.List;

public class ProfileAdaptor extends BaseAdapter {
    private Context context;
    private List<Stat> statItemList;

    public ProfileAdaptor(Context context, List<Stat> statItemList) {
        this.context = context;
        this.statItemList = statItemList;
    }

    @Override
    public int getCount() {
        return statItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return statItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_stats, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.statValueTextView = convertView.findViewById(R.id.statValueTextView);
            viewHolder.statNameTextView = convertView.findViewById(R.id.statNameTextView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Stat statItem = statItemList.get(position);
        viewHolder.statValueTextView.setText(String.valueOf(statItem.getValue()));
        viewHolder.statNameTextView.setText(statItem.getName());

        return convertView;
    }

    private static class ViewHolder {
        TextView statValueTextView;
        TextView statNameTextView;

    }
}
