package com.ftd.schaepher.wordbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ftd.schaepher.wordbook.R;

import java.util.List;

/**
 * Created by Schaepher on 2016/7/1.
 */
public class NavAdapter extends ArrayAdapter<String> {
    private int resourceId;

    public NavAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String word = getItem(position);
        View view;
        viewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new viewHolder();
            viewHolder.tvEnglish = (TextView) view.findViewById(R.id.tv_na_draw);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (NavAdapter.viewHolder) view.getTag();
        }
        viewHolder.tvEnglish.setText(word);
        return view;
    }

    class viewHolder {
        TextView tvEnglish;
    }

}
