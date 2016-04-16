package me.tim.org.yahtzee;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nekkyou on 16-4-2016.
 */
public class MyAdapter extends BaseAdapter {
    private final ArrayList mData;

    public MyAdapter(Map<String, String> map) {
        mData = new ArrayList();
        mData.addAll(map.entrySet());
    }

    public MyAdapter() {
        mData = new ArrayList();
        HashMap<String, String> map = new HashMap<>();
        map.put("Ones", "");
        map.put("Twos", "");
        map.put("Threes", "");
        map.put("Fours", "");
        map.put("Fives", "");
        map.put("Sixes", "");

        mData.addAll(map.entrySet());
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Map.Entry<String, String> getItem(int position) {
        return (Map.Entry<String, String>) mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;

        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_score, parent, false);
        } else {
            result = convertView;
        }

        Map.Entry<String, String> item = getItem(position);

        // TODO replace findViewById by ViewHolder
        ((TextView) result.findViewById(R.id.textKey)).setText(item.getKey());
        ((TextView) result.findViewById(R.id.textValue)).setText(item.getValue());

        return result;
    }
}
